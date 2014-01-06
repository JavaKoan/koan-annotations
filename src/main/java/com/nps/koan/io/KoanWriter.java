package com.nps.koan.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by nicholas_smith on 06/01/14.
 */
public class KoanWriter {

    public static void writeSourceToFile(Class<?> testClass, String newSource) {
        String packagePath = testClass.getPackage().getName();
        packagePath = packagePath.replace(".", "/");

        String className = testClass.getSimpleName();

        File file =  new File("src/koan/java/" + packagePath + "/" + className + ".java");

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
