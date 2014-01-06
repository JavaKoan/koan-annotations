package com.nps.koan.io;

import java.io.BufferedReader;
import java.io.File;
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
}
