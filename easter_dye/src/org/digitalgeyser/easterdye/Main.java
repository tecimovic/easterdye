package org.digitalgeyser.easterdye;

import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.digitalgeyser.easterdye.string.DeflateMethod;
import org.digitalgeyser.easterdye.string.StringObfuscator;

public class Main {

  private static void usage(final PrintStream ps) {
    ps.println("Usage: java -jar easter_dye.jar [arguments]");
    ps.println("Valid arguments:");
  }

  public static void main(final String[] args) {
    // Do it.
    Path inputFile = Paths.get(args[0]);
    Path outputJavaFile = Paths.get(args[1]);
    String packageName = args[2];
    StringObfuscator.obfuscate(inputFile, outputJavaFile, packageName, new DeflateMethod());
  }

}
