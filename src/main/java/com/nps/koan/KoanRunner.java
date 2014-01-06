package com.nps.koan;

import com.nps.koan.annotation.Koan;
import com.nps.koan.error.KoanError;
import com.nps.koan.io.KoanReader;
import com.nps.koan.io.KoanWriter;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

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

        String classSource = KoanReader.getSourceByClass(description.getTestClass());
        KoanExecution koanExecution = new KoanExecution(method, classSource);

        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        parser.setSource(classSource.toCharArray());

        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        if (koanExecution.isToBeEnlightened() && koanExecution.isToBeVexed()) {
            notifier.fireTestFailure(new Failure(description, new KoanError("@Vex and @Enlighten are mutually exclusive")));
            ignoreTest(notifier, description);
            return;
        }

        if (!isValidKoan(koanExecution, description, cu)) {
            notifier.fireTestFailure(new Failure(description, new KoanError("Koan is missing start /** (@_@) */ and end /** (^_^) */ markers")));
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


    private boolean isValidKoan(final KoanExecution koanExecution, Description description, final CompilationUnit cu) {

        final String methodName = description.getMethodName();

        cu.accept(new ASTVisitor() {

            public boolean visit(MethodDeclaration node) {

                if (methodName.equals(node.getName().getIdentifier())) {
                    Block block = node.getBody();

                    koanExecution.setMethodStartPosition(block.getStartPosition());
                    koanExecution.setMethodLength(block.getLength());

                    //System.out.println("Start1: " + block.getStartPosition() );
                    //System.out.println("Start2 + length: " + (koanExecution.getClassSource().indexOf(methodName) + methodName.length() + 2 ));
                }
                return false;
            }
        });

        List comments = cu.getCommentList();

        for(int i = 0; i<comments.size(); i++){
            Object obj = comments.get(i);
            if (obj instanceof Javadoc){
                Javadoc javaDoc = (Javadoc)obj;
                if ( javaDoc.getStartPosition() > koanExecution.getMethodStartPosition()
                        && javaDoc.getStartPosition() < (koanExecution.getMethodStartPosition() + koanExecution.getMethodLength())) {

                    String javaDocText = javaDoc.tags().get(0).toString();

                    if(javaDocText.contains(Koan.START_MARKER)){
                        koanExecution.setStartMarkerPosition(javaDoc.getStartPosition());
                        koanExecution.setStartMarkerLength(javaDoc.getLength());
                    } else if (javaDocText.contains(Koan.END_MARKER)) {
                        koanExecution.setEndMarkerPosition(javaDoc.getStartPosition());
                    }
                }
            }
        }

        //TODO: Improve error handling
        if (koanExecution.getStartMarkerPosition() == 0 || koanExecution.getStartMarkerLength() == 0) {
            return false; // Invalid start marker
        }
        if (koanExecution.getEndMarkerPosition() == 0) {
            return false; // Invalid end marker
        }

        return true;
    }

    private void performEnlightenment(KoanExecution koanExecution, Description description) {
        int solutionStart = koanExecution.getStartMarkerPosition() + koanExecution.getStartMarkerLength();
        String classSource = koanExecution.getClassSource();

        String newSourceString = classSource.substring(0, solutionStart) + "\n\t\ti = 5;\n\t\t"
                + classSource.substring(koanExecution.getEndMarkerPosition(), classSource.length());

        KoanWriter.writeSourceToFile(description.getTestClass(), newSourceString);
    }

    private void removeSolutionInKoan(KoanExecution koanExecution, Description description){
        int solutionStart = koanExecution.getStartMarkerPosition() + koanExecution.getStartMarkerLength();
        String classSource = koanExecution.getClassSource();

        String newSourceString = classSource.substring(0, solutionStart) + "\n\t\t"
                + classSource.substring(koanExecution.getEndMarkerPosition(), classSource.length());

        KoanWriter.writeSourceToFile(description.getTestClass(), newSourceString);
    }
}
