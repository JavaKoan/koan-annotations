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
package com.javakoan.fixture.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The type Koan reader.
 *
 * Responsible for reading the content of files associate with Koans .java, .solution and .problem
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

    /**
     * Gets source for a class.
     *
     * @param koanClass the koan class
     * @return the source code as a string
     */
    public static String getSourceByClass(Class<?> koanClass) {

        File file = new File(getKoanFileLocation(koanClass));
   		StringBuilder contents = new StringBuilder();
   		BufferedReader reader = null;

   		try {
   			reader = new BufferedReader(new FileReader(file));
   			String text = null;

   			while ((text = reader.readLine()) != null) {
   				contents.append(text).append(System.lineSeparator());
   			}
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

    /**
     * Gets an input stream by class.
     *
     * @param koanClass the koan class
     * @return the input stream relating to the class file
     */
    public static FileInputStream getInputStreamByClass(Class<?> koanClass) {

        FileInputStream in = null;
        try {
            in = new FileInputStream(getKoanFileLocation(koanClass));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return in;
    }

    /**
     * Gets solution for a koan by method name.
     *
     * @param koanClass the koan class
     * @param methodName the method name of the solution required
     * @return the solution content to be inserted between the koan start and end markers
     */
    public static String getSolutionFromFile(Class<?> koanClass, String methodName){
        return getSourceFromFile(koanClass, methodName, SOLUTION_EXTENSION);
    }

    /**
     * Gets problem for a koan by method name.
     *
     * @param koanClass the koan class
     * @param methodName the method name of the problem required
     * @return the problem content to be inserted between the koan start and end markers
     */
    public static String getProblemFromFile(Class<?> koanClass, String methodName) {
        return getSourceFromFile(koanClass, methodName, PROBLEM_EXTENSION);
    }

    private static String getSourceFromFile(Class<?> testClass, String methodName, String fileType) {
        Path currentRelativePath = Paths.get("");
        String workingDirectory = currentRelativePath.toAbsolutePath().toString();

        String className = testClass.getSimpleName();

        File file = new File(workingDirectory + KOAN_RESOURCES_PATH + className + fileType);
        StringBuilder contents = new StringBuilder();
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
            contents.append("");
        } catch (IOException e) {
            System.out.println("There was an error while loading the problem file");
            contents.append("");
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

        return currentRelativePath.toAbsolutePath().toString() +
                KOAN_JAVA_PATH +
                testClass.getPackage().getName().replace(PACKAGE_SEPARATOR, PATH_SEPARATOR) +
                PATH_SEPARATOR +
                testClass.getSimpleName() +
                JAVA_EXTENSION;
    }

}
