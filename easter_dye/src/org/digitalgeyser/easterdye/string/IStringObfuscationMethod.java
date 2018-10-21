// Copyright 2018 Digital Geyser.

package org.digitalgeyser.easterdye.string;

import java.io.IOException;

/**
 * Interface that describes a single string obfuscation mechanism.
 *
 * Created on Oct 20, 2018
 * @author Timotej Ecimovic
 */
public interface IStringObfuscationMethod {
  /**
   * Given the input buffer of data (which is typically read from the file
   * and a length used, the resulted byte[] is the encoding.
   *
   * @returns byte[]
   */
  public byte[] encodeData(final byte[] buffer) throws IOException;

  /**
   * This method outputs the source code for a decoder class.
   *
   * @returns String[]
   */
  public String[] classContentForDecoder(String packageName, String className, byte[] data);
}
