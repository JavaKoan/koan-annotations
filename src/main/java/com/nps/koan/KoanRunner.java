package com.nps.koan;

import com.nps.koan.error.KoanError;
import com.nps.koan.io.KoanReader;
import com.nps.koan.io.KoanWriter;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.comments.Comment;
import japa.parser.ast.visitor.VoidVisitorAdapter;
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
 * Created by nicholas_smith on 02/01/14.
 */
public final class KoanRunner extends BlockJUnit4ClassRunner {

    private static final String START_MARKER = "(@_@)";
    private static final String END_MARKER = "(^_^)";

    public KoanRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    protected List<FrameworkMethod> computeTestMethods() {
        return getTestClass().getAnnotatedMethods(com.nps.koan.annotation.Koan.class);
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
            notifier.fireTestFailure(new Failure(description, new KoanError("Koan is missing start (@_@) and end (^_^) markers")));
            ignoreTest(notifier, description);
            return;
        }

        if (koanExecution.isToBeEnlightened()) {
            determineSolution(description.getTestClass(), koanExecution);
            updateKoanSource(koanExecution, description);
        }

        if (koanExecution.isToBeVexed()) {
            koanExecution.setSolution("");
            updateKoanSource(koanExecution, description);
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

    private static class MethodVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(MethodDeclaration method, Object arg) {

            KoanExecution koanExecution = (KoanExecution)arg;

            if(method.getName().equals(koanExecution.getMethod().getName())){
                List<Comment> comments = method.getAllContainedComments();
                for(Comment comment : comments){
                    if (comment.getContent().contains(START_MARKER)) {
                        koanExecution.setStartMarkerLine(comment.getBeginLine());
                     } else if (comment.getContent().contains(END_MARKER)) {
                        koanExecution.setEndMarkerLine(comment.getBeginLine());
                    }
                }
            }
        }
    }

    private void determineSolution(Class<?> testClass, KoanExecution koanExecution) {
        String solution = KoanReader.getSolutionFromFile(testClass, koanExecution.getMethod().getName());
        koanExecution.setSolution(solution);
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
        } catch (ParseException e) {
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
