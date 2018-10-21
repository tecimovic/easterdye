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

  public static int bytesToInt(final byte[] bytes, final int offset) {
    int ret = 0;
    for (int i=0; i<4 && i+offset<bytes.length; i++) {
      ret <<= 8;
      ret |= (bytes[i+offset] & 0x000000FF);
    }
    return ret;
  }

  /** Creates a length-prefixed int array */
  public static int[] byteArrayToIntArray(final byte[] bytes) {
    int len;
    boolean even;
    if ( bytes.length%4 == 0 ) {
      len = bytes.length/4 + 1; // Extra one for length
      even = true;
    } else {
      len = bytes.length/4 + 2; // Extra one for length and for leftover bytes.
      even = false;
    }
    int[] ints = new int[len];
    ints[0] = bytes.length;  // Store length

    for ( int i=0; i<(ints.length-1-(even?0:1)); i++ ) {
      int x = (bytes[4*i]&0x000000ff) << 24
              | (bytes[4*i+1]&0x000000ff) << 16
              | (bytes[4*i+2]&0x000000ff) << 8
              | (bytes[4*i+3]&0x000000ff);
      ints[i+1] = x;
    }
    if ( !even ) {
      int x = 0;
      for ( int i=0; i<bytes.length%4; i++ ) {
        x |= ((bytes[4*(ints.length-2) + i]&0x000000ff) << (3-i)*8);
      }
      ints[ints.length-1] = x;
    }
    return ints;
  }

  /** Reads from length-prefixed int array to byte array. */
  public static byte[] intArrayToByteArray(final int[] ints) {
    int length = ints[0]; // That's where length was
    byte[] bytes = new byte[length];
    for ( int i=0; i<length; i++ ) {
      bytes[i] = (byte)((ints[i/4+1] >> (8*(3-i%4))) & (0x000000FF));
    }
    return bytes;
  }

  public static String printIntArray(final byte[] bytes, final int ident, final int perRow) {
    StringBuilder barr = new StringBuilder();
    int n = 0;
    StringBuilder identS = new StringBuilder();
    for ( int i=0; i<ident; i++ )
      identS.append(' ');
    barr.append(identS);

    int[] ints = byteArrayToIntArray(bytes);
    for (int j : ints) {
      barr.append(j + ",");
      n++;
      if ( n%perRow == 0 ) {
        barr.append("\n").append(identS);
      }
    }
    return barr.toString();
  }


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
