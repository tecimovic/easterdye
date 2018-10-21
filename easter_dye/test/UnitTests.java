import org.digitalgeyser.easterdye.string.StringObfuscator;
public class UnitTests {

  public static void main(final String[] args) {
    try {
      testUtilities();
    } catch (Exception e) {
      System.err.println("FAILURE!");
      e.printStackTrace();
    }
  }

  private static void testUtilities() throws Exception {
    byte[] testBytes = { 45, (byte)234, 56, 76, 34, 23, 54, 56, 67, (byte)200 };
    int[] ints = StringObfuscator.byteArrayToIntArray(testBytes);
    byte[] bytes = StringObfuscator.intArrayToByteArray(ints);

    for ( byte b: testBytes ) {
      System.err.print(Integer.toHexString((b&0xFF))+ ",");
    }
    System.err.println();

    for ( int b: ints ) {
      System.err.print(Integer.toHexString((b))+ ",");
    }
    System.err.println();

    for ( byte b: bytes ) {
      System.err.print(Integer.toHexString((b&0xFF))+ ",");
    }
    System.err.println();

    if ( bytes.length != testBytes.length )
      throw new Exception("Lengths are different!");
    for ( int i=0; i<bytes.length; i++ )
      if ( bytes[i] != testBytes[i] )
        throw new Exception("Byte[" + i + "] is different!");

  }
}
