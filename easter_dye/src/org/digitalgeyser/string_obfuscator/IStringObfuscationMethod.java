// Copyright 2018 Silicon Laboratories, Inc.

package org.digitalgeyser.string_obfuscator;

import java.io.IOException;

public interface IStringObfuscationMethod {
  byte[] encodeData(final byte[] buffer, final int len) throws IOException;
  String[] classContentForDecoder(String className, byte[] data);
}
