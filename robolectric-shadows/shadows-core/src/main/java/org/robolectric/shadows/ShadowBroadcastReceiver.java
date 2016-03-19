package org.robolectric.shadows;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.internal.Shadow;
import org.robolectric.util.ReflectionHelpers.ClassParameter;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Shadow for {@link android.content.BroadcastReceiver}.
 */
@Implements(BroadcastReceiver.class)
public class ShadowBroadcastReceiver {
  @RealObject BroadcastReceiver receiver;

  private AtomicBoolean abort; // The abort state of the currently processed broadcast

  @Implementation
  public void abortBroadcast() {
    // TODO probably needs a check to prevent calling this method from ordinary Broadcasts
    abort.set(true);
  }

  @Implementation
  public void onReceive(Context context, Intent intent) {
    if (abort == null || !abort.get()) {
      Shadow.directlyOn(receiver, BroadcastReceiver.class, "onReceive",
          ClassParameter.fromComponentLists(
              new Class[]{Context.class, Intent.class}, new Object[]{context, intent}));
    }
  }

  public void onReceive(Context context, Intent intent, AtomicBoolean abort) {
    this.abort = abort;
    onReceive(context, intent);
    // If the underlying receiver has called goAsync(), we should not finish the pending result yet - they'll do that
    // for us.
    if (receiver.getPendingResult() != null) {
      receiver.getPendingResult().finish();
    }
  }
}
