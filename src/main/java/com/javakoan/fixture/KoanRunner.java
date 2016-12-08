/**
 * Copyright 2014 Nicholas Smith
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.javakoan.fixture;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.javakoan.fixture.error.KoanError;
import com.javakoan.fixture.io.KoanReader;
import com.javakoan.fixture.io.KoanWriter;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * The type Koan runner.
 *
 * Class that provides mechanism to execute Koans within the JUnit framework.
 */
public final class KoanRunner extends BlockJUnit4ClassRunner {

    private static final String START_MARKER = "(@_@)";
    private static final String END_MARKER = "(^_^)";

    public KoanRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    protected List<FrameworkMethod> computeTestMethods() {
        return getTestClass().getAnnotatedMethods(com.javakoan.fixture.annotation.Koan.class);
    }


    @Override
    protected void runChild(final FrameworkMethod method, RunNotifier notifier) {

        Description description = describeChild(method);
        CompilationUnit compilationUnit = getCompilationUnit(description);

        if(compilationUnit == null){
            throw new KoanError("Failed to load Koan");
        }

        String classSource = KoanReader.getSourceByClass(description.getTestClass());
        KoanExecution koanExecution = new KoanExecution(method, classSource);

        if (koanExecution.isToBeEnlightened() && koanExecution.isToBeVexed()) {
            notifier.fireTestFailure(new Failure(description, new KoanError("@Vex and @Enlighten are mutually exclusive")));
            ignoreTest(notifier, description);
            return;
        }

        if (!isValidKoan(compilationUnit, koanExecution)) {
            notifier.fireTestFailure(new Failure(description, new KoanError("Koan is missing start " +START_MARKER+
                    " and end " +END_MARKER+ " markers")));
            ignoreTest(notifier, description);
            return;
        }

        if (koanExecution.isToBeEnlightened()) {
            determineSolution(description.getTestClass(), koanExecution);
            updateKoanSource(koanExecution, description);
            System.out.println("Koan "+method.getName()+" is ignored as currently marked with @Enlighten");
        }

        if (koanExecution.isToBeVexed()) {
            determineProblem(description.getTestClass(), koanExecution);
            updateKoanSource(koanExecution, description);
            System.out.println("Koan "+method.getName()+" is ignored as currently marked with @Vex");
        }

        if (koanExecution.isIgnored()) {
            ignoreTest(notifier, description);
        } else {
            runLeaf(methodBlock(method), description, notifier);
        }
    }


    private void ignoreTest(RunNotifier notifier, Description description) {
        notifier.fireTestIgnored(description);
    }


    private boolean isValidKoan(CompilationUnit compilationUnit, KoanExecution koanExecution) {

        new MethodVisitor().visit(compilationUnit, koanExecution);

        //TODO: Improve error handling
        if (koanExecution.getStartMarkerLine() == 0) {
            return false;
        }
        if (koanExecution.getEndMarkerLine() == 0) {
            return false;
        }

        return true;
    }

    private static class MethodVisitor extends VoidVisitorAdapter<KoanExecution> {

        @Override
        public void visit(MethodDeclaration method, KoanExecution koanExecution) {

            if(method.getNameAsString().equals(koanExecution.getMethod().getName())){
                List<Comment> comments = method.getAllContainedComments();
                for(Comment comment : comments){
                    final int commentLine = comment.getBegin().get().line;
                    if (comment.getContent().contains(START_MARKER)) {
                        koanExecution.setStartMarkerLine(commentLine);
                     } else if (comment.getContent().contains(END_MARKER)) {
                        koanExecution.setEndMarkerLine(commentLine);
                    }
                }
            }
        }
    }

    private void determineSolution(Class<?> testClass, KoanExecution koanExecution) {
        String solution = KoanReader.getSolutionFromFile(testClass, koanExecution.getMethod().getName());
        koanExecution.setSolution(solution);
    }

    private void determineProblem(Class<?> testClass, KoanExecution koanExecution) {
        String problem = KoanReader.getProblemFromFile(testClass, koanExecution.getMethod().getName());
        koanExecution.setSolution(problem);
    }

    private void updateKoanSource(KoanExecution koanExecution, Description description) {
        final String newLine = System.lineSeparator();

        String[] lines = koanExecution.getClassSource().split(newLine);

        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < koanExecution.getStartMarkerLine() ; i++){
            sb.append(lines[i]);
            sb.append(newLine);
        }
        sb.append(koanExecution.getSolution());
        for(int i = koanExecution.getEndMarkerLine() - 1; i < lines.length; i++){
            sb.append(lines[i]);
            sb.append(newLine);
        }

        KoanWriter.writeSourceToFile(description.getTestClass(), sb.toString());
    }

    private CompilationUnit getCompilationUnit(Description description) {
        CompilationUnit cu = null;
        FileInputStream in = null;

        try {
            in = KoanReader.getInputStreamByClass(description.getTestClass());
            cu = JavaParser.parse(in);
        } catch (ParseProblemException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cu;
    }
}
