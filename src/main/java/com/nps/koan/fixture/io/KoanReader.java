package com.nps.koan.fixture.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by nicholas_smith on 06/01/14.
 */
public class KoanReader {

    private static final String KOAN_JAVA_PATH = "/src/koan/java/";
    private static final String KOAN_RESOURCES_PATH = "/src/koan/resources/";

    private static final String PATH_SEPARATOR = "/";
    private static final String PACKAGE_SEPARATOR = ".";

    private static final String JAVA_EXTENSION = ".java";
    private static final String SOLUTION_EXTENSION = ".solution";
    private static final String PROBLEM_EXTENSION = ".problem";

    private KoanReader(){} // Non-instantiable

    public static String getSourceByClass(Class<?> testClass) {

        File file = new File(getKoanFileLocation(testClass));
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

        FileInputStream in = null;
        try {
            in = new FileInputStream(getKoanFileLocation(testClass));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return in;
    }

    public static String getSolutionFromFile(Class<?> testClass, String methodName){
        return getSourceFromFile(testClass, methodName, SOLUTION_EXTENSION);
    }

    public static String getProblemFromFile(Class<?> testClass, String methodName) {
        return getSourceFromFile(testClass, methodName, PROBLEM_EXTENSION);
    }

    private static String getSourceFromFile(Class<?> testClass, String methodName, String fileType) {
        Path currentRelativePath = Paths.get("");
        String workingDirectory = currentRelativePath.toAbsolutePath().toString();

        String className = testClass.getSimpleName();

        File file = new File(workingDirectory + KOAN_RESOURCES_PATH + className + fileType);
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

    private static String getKoanFileLocation(Class<?> testClass) {
        Path currentRelativePath = Paths.get("");

        StringBuilder sb = new StringBuilder()
                .append(currentRelativePath.toAbsolutePath().toString())
                .append(KOAN_JAVA_PATH)
                .append(testClass.getPackage().getName().replace(PACKAGE_SEPARATOR, PATH_SEPARATOR))
                .append(PATH_SEPARATOR)
                .append(testClass.getSimpleName())
                .append(JAVA_EXTENSION);

        return sb.toString();
    }

}
