package org.robolectric.internal.bytecode.testing;

import org.robolectric.annotation.internal.Instrument;

@SuppressWarnings("UnusedDeclaration")
@Instrument
public class AClassWithNativeMethodReturningPrimitive {
  public native int nativeMethod();
}
