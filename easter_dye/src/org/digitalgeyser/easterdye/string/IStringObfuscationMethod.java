// Copyright 2018 Silicon Laboratories, Inc.

package org.digitalgeyser.easterdye.string;

import java.io.IOException;

public interface IStringObfuscationMethod {
  byte[] encodeData(final byte[] buffer, final int len) throws IOException;
  String[] classContentForDecoder(String packageName, String className, byte[] data);
}
