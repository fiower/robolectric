package org.robolectric.shadows;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ShadowBroadcastReceiverTest {
  
  private static final String ACTION = "org.robolectric.shadows.TEST_ACTION";
  
  private BroadcastReceiver receiver;
  private ShadowBroadcastReceiver shReceiver;
  private boolean isRealObjectCalled;
  
  @Before
  public void setUp() {
    isRealObjectCalled = false;
    receiver = new BroadcastReceiver() {  
      @Override
      public void onReceive(Context context, Intent intent) {
        isRealObjectCalled = true;
      }
    };
    shReceiver = Shadows.shadowOf(receiver);
  }
  
  @Test
  public void testOnReceive() {
    shReceiver.onReceive(RuntimeEnvironment.application, new Intent(ACTION));
    Assert.assertTrue(isRealObjectCalled);
  }
}
