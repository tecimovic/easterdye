// Copyright 2018 Silicon Laboratories, Inc.

package org.digitalgeyser.easterdye.string;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
public class ZipMethod implements IStringObfuscationMethod {

  @Override
  public byte[] encodeData(final byte[] buffer, final int len) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ZipOutputStream zos = new ZipOutputStream(baos);
    zos.setLevel(Deflater.BEST_COMPRESSION);
    ZipEntry newEntry = new ZipEntry("");
    newEntry.setTime(12345);
    zos.putNextEntry(newEntry);
    for ( int i=0; i<len; i++ ) {
      zos.write(buffer[i]);
    }
    zos.closeEntry();
    zos.close();
    byte[] transformedArray = baos.toByteArray();
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
"import java.io.ByteArrayInputStream;",
"import java.util.zip.ZipInputStream;",
"",
"public class " + className + " {",
"  private static final byte[] x = {",
 printByteArray(data, 4, 20),
"  };",
"",
"  public static String x() {",
"    try {",
"      StringBuilder s=new StringBuilder();",
"      for(int i=0; i<x.length; i++)",
"        x[i] ^= (0x13+i%10);",
"      ZipInputStream z = new ZipInputStream(new ByteArrayInputStream(x));",
"      z.getNextEntry();",
"      int b;",
"      while((b=z.read())!=-1) {",
"        s.append((char)b);",
"      }",
"      z.close();",
"      return s.toString();",
"    } catch(Exception e) {",
"      throw new IllegalStateException();",
"    }",
"  }",
"}"
    };
    return code;
  }

}
