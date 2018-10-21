// Copyright 2018 Digital Geyser.

import java.lang.reflect.Method;
import java.util.Random;

import org.digitalgeyser.easterdye.Utility;

// Unit tests. Any method that start with 'test' is a test method.
// I'm too lazy to pull in junit.
public class UnitTests {

  public static void main(final String[] args) {
    try {
      UnitTests ut = new UnitTests();
      for ( Method m: UnitTests.class.getMethods() ) {
        if ( m.getName().startsWith("test") && m.getParameterCount() == 0 ) {
          System.out.print("[" + m.getName());
          m.invoke(ut);
          System.out.println("....]");
        }
      }
    } catch (Exception e) {
      System.err.println("FAILURE!");
      e.printStackTrace();
    }
  }

  private void assertSameByteArrays(final byte[] a, final byte[] b) throws Exception {
    if ( a.length != a.length )
      throw new Exception("Lengths are different!");
    for ( int i=0; i<a.length; i++ )
      if ( a[i] != b[i] )
        throw new Exception("Byte[" + i + "] is different!");
  }

  public void testSimpleByte() throws Exception {
    byte[] testBytes = { 45, (byte)234, 56, 76, 34, 23, 54, 56, 67, (byte)200 };
    int[] ints = Utility.byteArrayToIntArray(testBytes);
    byte[] bytes = Utility.intArrayToByteArray(ints);
    assertSameByteArrays(testBytes, bytes);
  }

  public void testLongArrays() throws Exception {
    Random rnd = new Random(2430958);
    for ( int s=100; s<2000; s+=47 ) {
      byte[] b = new byte[s];
      rnd.nextBytes(b);
      byte[] b2 = Utility.intArrayToByteArray(Utility.byteArrayToIntArray(b));
      assertSameByteArrays(b, b2);
    }
  }
}
