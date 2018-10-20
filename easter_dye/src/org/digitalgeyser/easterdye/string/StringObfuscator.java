// Copyright 2018 Silicon Laboratories, Inc.

package org.digitalgeyser.easterdye.string;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StringObfuscator {

  public static void main(final String[] args) {
    // Do it.
    String myLoc = "/home/timotej/git/easterdye/easter_dye/";
    Path inputFile = Paths.get(myLoc + "pony.txt");
    Path outputJavaFile = Paths.get(myLoc + "src/org/digitalgeyser/easterdye/string/X.java");
    obfuscate(inputFile, outputJavaFile, new ZipMethod());

    // Test it.
    System.err.println(X.x());
  }

  private static void obfuscate(final Path inputTextFile,
                                final Path outputJavaFile,
                                final IStringObfuscationMethod method) {
    try {
      String className = outputJavaFile.getFileName().toString().replace(".java", "");
      // Read
      byte[] buffer = Files.readAllBytes(inputTextFile);
      // Transform
      byte[] transformedArray = method.encodeData(buffer, buffer.length);
      // Create output code
      String[] code = method.classContentForDecoder(className, transformedArray);

      try (PrintWriter pw = new PrintWriter(outputJavaFile.toFile())) {


        // Write out
        for ( String line: code ) {
          pw.println(line);
        }
        System.out.println("Lenght of original array: " + buffer.length);
        System.out.println("Length of obfuscated array: " + transformedArray.length);
        pw.flush();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
