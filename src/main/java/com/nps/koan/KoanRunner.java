package com.nps.koan;

import com.nps.koan.annotation.Koan;
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
public class KoanRunner extends BlockJUnit4ClassRunner {


    public KoanRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    protected List<FrameworkMethod> computeTestMethods() {
        return getTestClass().getAnnotatedMethods(com.nps.koan.annotation.Koan.class);
    }


    @Override
    protected void runChild(final FrameworkMethod method, RunNotifier notifier) {

        Description description = describeChild(method);

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

        if(cu == null){
            throw new KoanError("Failed to load Koan");
        }

        String classSource = KoanReader.getSourceByClass(description.getTestClass());
        KoanExecution koanExecution = new KoanExecution(method, classSource);


        if (koanExecution.isToBeEnlightened() && koanExecution.isToBeVexed()) {
            notifier.fireTestFailure(new Failure(description, new KoanError("@Vex and @Enlighten are mutually exclusive")));
            ignoreTest(notifier, description);
            return;
        }

        if (!isValidKoan(koanExecution, cu)) {
            notifier.fireTestFailure(new Failure(description, new KoanError("Koan is missing start /* (@_@) */ and end /* (^_^) */ markers")));
            ignoreTest(notifier, description);
            return;
        }

        if (koanExecution.isToBeEnlightened()) {
            performEnlightenment(koanExecution, description);
        }

        if (koanExecution.isToBeVexed()) {
            removeSolutionInKoan(koanExecution, description);
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


    private boolean isValidKoan(KoanExecution koanExecution, CompilationUnit cu) {

        new MethodVisitor().visit(cu, koanExecution);

        //TODO: Improve error handling
        if (koanExecution.getStartMarkerPosition() == 0) {
            return false; // Invalid start marker
        }
        if (koanExecution.getEndMarkerPosition() == 0) {
            return false; // Invalid end marker
        }

        return true;
    }

    private static class MethodVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(MethodDeclaration n, Object arg) {

            KoanExecution koanExecution = (KoanExecution)arg;

            if(n.getName().equals(koanExecution.getMethod().getName())){
                List<Comment> comments = n.getAllContainedComments();
                for(Comment c : comments){
                    if (c.getContent().contains(Koan.START_MARKER)) {
                        koanExecution.setStartMarkerPosition(c.getBeginLine());
                     } else if (c.getContent().contains(Koan.END_MARKER)) {
                        koanExecution.setEndMarkerPosition(c.getBeginLine());
                    }
                }
            }
        }
    }

    private void performEnlightenment(KoanExecution koanExecution, Description description) {
        String[] lines = koanExecution.getClassSource().split(System.getProperty("line.separator"));

        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < koanExecution.getStartMarkerPosition() ; i++){
            sb.append(lines[i]);
            sb.append(System.getProperty("line.separator"));
        }
        sb.append("\t\ti = 5;\n");
        for(int i = koanExecution.getEndMarkerPosition() - 1; i < lines.length; i++){
            sb.append(lines[i]);
            sb.append(System.getProperty("line.separator"));
        }

        KoanWriter.writeSourceToFile(description.getTestClass(), sb.toString());
    }

    private void removeSolutionInKoan(KoanExecution koanExecution, Description description) {
        String[] lines = koanExecution.getClassSource().split(System.getProperty("line.separator"));

        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < koanExecution.getStartMarkerPosition() ; i++){
            sb.append(lines[i]);
            sb.append(System.getProperty("line.separator"));
        }
        for(int i = koanExecution.getEndMarkerPosition() - 1; i < lines.length; i++){
            sb.append(lines[i]);
            sb.append(System.getProperty("line.separator"));
        }

        KoanWriter.writeSourceToFile(description.getTestClass(), sb.toString());
    }
}
