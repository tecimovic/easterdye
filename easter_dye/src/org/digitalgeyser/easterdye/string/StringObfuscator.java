// Copyright 2018 Silicon Laboratories, Inc.

package org.digitalgeyser.easterdye.string;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Toplevel API for string embedding and obfuscation.
 * This is how you get strings into the java code without being obvious.
 *
 * Created on Oct 20, 2018
 * @author Timotej Ecimovic
 */
public class StringObfuscator {

  public static void obfuscate(final Path inputTextFile,
                               final Path outputJavaFile,
                               final String packageName,
                               final IStringObfuscationMethod method) {
    try {
      String className = outputJavaFile.getFileName().toString().replace(".java", "");
      // Read
      byte[] buffer = Files.readAllBytes(inputTextFile);
      // Transform
      byte[] transformedArray = method.encodeData(buffer);
      //System.err.println("Input: " + buffer.length + ", output: " + transformedArray.length);
      // Create output code
      String[] code = method.classContentForDecoder(packageName, className, transformedArray);

      try (PrintWriter pw = new PrintWriter(outputJavaFile.toFile())) {


        // Write out
        for ( String line: code ) {
          pw.println(line);
        }
        pw.flush();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
