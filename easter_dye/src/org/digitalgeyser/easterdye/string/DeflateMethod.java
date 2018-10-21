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
    return transformedArray;
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
"  private static final int[] x = {",
StringObfuscator.printIntArray(data, 4, 6),
"  };",
"",
"  public static String x() {",
"    try {",
"      int l = x[0];",
"      byte[] b = new byte[l];",
"      for ( int i=0; i<l; i++ ) {",
"        b[i] = (byte)((x[i/4+1] >> (8*(3-i%4))) & (0x000000FF));",
"      }",
"      Inflater d = new Inflater();",
"      byte[] out = new byte[b.length * 10];",
"      d.setInput(b);",
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
