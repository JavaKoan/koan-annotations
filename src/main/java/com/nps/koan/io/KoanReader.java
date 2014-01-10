package com.nps.koan.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by nicholas_smith on 06/01/14.
 */
public class KoanReader {

    private static final String KOAN_JAVA_PATH = "src/koan/java/";
    private static final String KOAN_RESOURCES_PATH = "src/koan/resources/";

    private static final String PATH_SEPARATOR = "/";
    private static final String PACKAGE_SEPARATOR = ".";

    private static final String JAVA_EXTENSION = ".java";
    private static final String SOLUTION_EXTENSION = ".solution";

    private KoanReader(){} // Non-instantiable

    public static String getSourceByClass(Class<?> testClass) {
        String packagePath = testClass.getPackage().getName();
        packagePath = packagePath.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);

        String className = testClass.getSimpleName();

        File file = new File(KOAN_JAVA_PATH + packagePath + PATH_SEPARATOR + className + JAVA_EXTENSION);
   		StringBuffer contents = new StringBuffer();
   		BufferedReader reader = null;

   		try {
   			reader = new BufferedReader(new FileReader(file));
   			String text = null;

   			while ((text = reader.readLine()) != null) {
   				contents.append(text).append(System.lineSeparator());
   			}
   		} catch (FileNotFoundException e) {
   			e.printStackTrace();
   		} catch (IOException e) {
   			e.printStackTrace();
   		} finally {
   			try {
   				if (reader != null) {
   					reader.close();
   				}
   			} catch (IOException e) {
   				e.printStackTrace();
   			}
   		}
   		return contents.toString();
   	}

    public static FileInputStream getInputStreamByClass(Class<?> testClass) {
        String packagePath = testClass.getPackage().getName();
        packagePath = packagePath.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);

        String className = testClass.getSimpleName();

        FileInputStream in = null;
        try {
            in = new FileInputStream(KOAN_JAVA_PATH + packagePath + PATH_SEPARATOR + className + JAVA_EXTENSION);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return in;
    }

    public static String getSolutionFromFile(Class<?> testClass, String methodName){
        String className = testClass.getSimpleName();

        File file = new File(KOAN_RESOURCES_PATH + className + SOLUTION_EXTENSION);
        StringBuffer contents = new StringBuffer();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String text;

            while ((text = reader.readLine()) != null) {
                if(text.equalsIgnoreCase(methodName)){
                    reader.readLine(); // ignore [
                    while (!(text = reader.readLine()).equalsIgnoreCase("]")) {
                        contents.append(text).append(System.lineSeparator());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return contents.toString();
    }
}
