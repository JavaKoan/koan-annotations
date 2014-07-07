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

        String path = currentRelativePath.toAbsolutePath().toString() +
                KOAN_JAVA_PATH +
                testClass.getPackage().getName().replace(PACKAGE_SEPARATOR, PATH_SEPARATOR) +
                PATH_SEPARATOR +
                testClass.getSimpleName() +
                JAVA_EXTENSION;

        return path;
    }

}
