package com.nps.koan.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by nicholas_smith on 06/01/14.
 */
public class KoanWriter {

    private static final String KOAN_JAVA_PATH = "src/koan/java/";
    private static final String JAVA_EXTENSION = ".java";

    private static final String PATH_SEPARATOR = "/";
    private static final String PACKAGE_SEPARATOR = ".";

    private KoanWriter(){} // Non-instantiable

    public static void writeSourceToFile(Class<?> testClass, String newSource) {
        String packagePath = testClass.getPackage().getName();
        packagePath = packagePath.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);

        String className = testClass.getSimpleName();

        File file =  new File(KOAN_JAVA_PATH + packagePath + PATH_SEPARATOR + className + JAVA_EXTENSION);

        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(newSource);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
