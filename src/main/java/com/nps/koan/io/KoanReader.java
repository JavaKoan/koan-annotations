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

    public static String getSourceByClass(Class<?> testClass) {
        String packagePath = testClass.getPackage().getName();
        packagePath = packagePath.replace(".", "/");

        String className = testClass.getSimpleName();

        File file = new File("src/koan/java/" + packagePath + "/" + className + ".java");
   		StringBuffer contents = new StringBuffer();
   		BufferedReader reader = null;

   		try {
   			reader = new BufferedReader(new FileReader(file));
   			String text = null;

   			while ((text = reader.readLine()) != null) {
   				contents.append(text).append(System.getProperty("line.separator"));
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
        packagePath = packagePath.replace(".", "/");

        String className = testClass.getSimpleName();

        FileInputStream in = null;
        try {
            in = new FileInputStream("src/koan/java/" + packagePath + "/" + className + ".java");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return in;
    }

    public static String getSolutionFromFile(Class<?> testClass, String methodName){
        String className = testClass.getSimpleName();

        File file = new File("src/koan/resources/" + className + ".Enlighten");
        StringBuffer contents = new StringBuffer();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String text;

            while ((text = reader.readLine()) != null) {
                if(text.equalsIgnoreCase(methodName)){
                    reader.readLine(); // ignore [

                    while (!(text = reader.readLine()).equalsIgnoreCase("]")) {
                        contents.append(text).append(System.getProperty("line.separator"));
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
