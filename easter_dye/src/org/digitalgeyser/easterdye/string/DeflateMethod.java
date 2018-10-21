// Copyright 2018 Silicon Laboratories, Inc.

package org.digitalgeyser.easterdye.string;

import java.io.IOException;
import java.util.zip.Deflater;

/**
 * Mechanism for obfuscating that essentially zips up the string, obfuscates it
 * with a simple one-byte hash, and stores it as a byte[] block of the resulting
 * data.
 *
 * Deobfuscator simply reverts the process.
 *
 * Created on Oct 20, 2018
 * @author Timotej Ecimovic
 */
public class DeflateMethod implements IStringObfuscationMethod {

  @Override
  public byte[] encodeData(final byte[] buffer) throws IOException {
    Deflater d = new Deflater();
    d.setInput(buffer);
    d.finish();
    byte[] output = new byte[buffer.length];
    int n = d.deflate(output);
    byte[] transformedArray = new byte[n];
    System.arraycopy(output, 0, transformedArray, 0, n);
    for ( int i=0; i<transformedArray.length; i++ ) {
      transformedArray[i] ^= (0x13+i%10);
    }
    return transformedArray;
  }

  private static String printByteArray(final byte[] bytes, final int ident, final int perRow) {
    StringBuilder barr = new StringBuilder();
    int n = 0;
    StringBuilder identS = new StringBuilder();
    for ( int i=0; i<ident; i++ )
      identS.append(' ');
    barr.append(identS);
    for ( byte b: bytes ) {
      barr.append(b + ",");
      n++;
      if ( n%perRow == 0 ) {
        barr.append("\n");
        barr.append(identS);
      }
    }
    return barr.toString();
  }


  @Override
  public String[] classContentForDecoder(final String packageName,
                                         final String className,
                                         final byte[] data) {
    String [] code = {
(packageName != null ? "package " + packageName + ";" : ""),
"",
"import java.util.zip.Inflater;",
"",
"public class " + className + " {",
"  private static final byte[] x = {",
 printByteArray(data, 4, 20),
"  };",
"",
"  public static String x() {",
"    try {",
"      for(int i=0; i<x.length; i++)",
"        x[i] ^= (0x13+i%10);",
"      Inflater d = new Inflater();",
"      byte[] out = new byte[x.length * 10];",
"      d.setInput(x);",
"      d.inflate(out);",
"      d.end();",
"      return new String(out);",
"    } catch(Exception e) {",
"      throw new IllegalStateException();",
"    }",
"  }",
"}"
    };
    return code;
  }

}
