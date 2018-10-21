// Copyright 2018 Digital Geyser.

import org.digitalgeyser.easterdye.Main;

import gen.X;
public class MainTest {
  public static void main(final String[] args) {
    // Do it.
    String myLoc = "/home/timotej/git/easterdye/easter_dye/";
    String inputFile = myLoc + "test/pony.txt";
    String outputJavaFile = myLoc + "test/gen/X.java";

    Main.main(new String[] {inputFile, outputJavaFile, "gen"});
    System.out.println(X.x());
  }
}
