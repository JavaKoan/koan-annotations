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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The type Koan writer.
 *
 * Responsible for writing koan files to disk
 */
public class KoanWriter {

    private static final String KOAN_JAVA_PATH = "/src/koan/java/";
    private static final String JAVA_EXTENSION = ".java";

    private static final String PATH_SEPARATOR = "/";
    private static final String PACKAGE_SEPARATOR = ".";

    private KoanWriter(){} // Non-instantiable

    /**
     * Write source to file.
     *
     * @param koanClass the koan class
     * @param newSource the source code to be written
     */
    public static void writeSourceToFile(Class<?> koanClass, String newSource) {
        Path currentRelativePath = Paths.get("");
        String workingDirectory = currentRelativePath.toAbsolutePath().toString();

        String packagePath = koanClass.getPackage().getName();
        packagePath = packagePath.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);

        String className = koanClass.getSimpleName();

        File file =  new File(workingDirectory + KOAN_JAVA_PATH + packagePath + PATH_SEPARATOR + className + JAVA_EXTENSION);

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
