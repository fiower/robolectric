package org.robolectric.shadows.support.v4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.LinearLayout;

import org.robolectric.Robolectric;
import org.robolectric.ShadowsAdapter;
import org.robolectric.util.ActivityController;
import org.robolectric.util.ComponentController;

/**
 * Version of FragmentController that can be used for android.support.v4.Fragment. Only
 * necessary if more complex lifecycle management is needed, otherwise SupportFragmentTestUtil
 * should be sufficient.
 */
public class SupportFragmentController<F extends Fragment> extends ComponentController<SupportFragmentController<F>, F> {
  private final F fragment;
  private final ActivityController<? extends FragmentActivity> activityController;

  protected SupportFragmentController(ShadowsAdapter shadowsAdapter, F fragment, Class<? extends FragmentActivity> activityClass) {
    super(shadowsAdapter, fragment);
    this.fragment = fragment;
    this.activityController = Robolectric.buildActivity(activityClass);
  }

  public static <F extends Fragment> SupportFragmentController<F> of(F fragment) {
    return new SupportFragmentController<>(Robolectric.getShadowsAdapter(), fragment, FragmentControllerActivity.class);
  }

  public static <F extends Fragment> SupportFragmentController<F> of(F fragment, Class<? extends FragmentActivity> activityClass) {
    return new SupportFragmentController<>(Robolectric.getShadowsAdapter(), fragment, activityClass);
  }

  @Override
  public SupportFragmentController<F> attach() {
    activityController.attach();
    return this;
  }

  /**
   * Creates the activity with {@link Bundle} and adds the fragment to the view with ID {@code contentViewId}.
   */
  public SupportFragmentController<F> create(final int contentViewId, final Bundle bundle) {
    shadowMainLooper.runPaused(new Runnable() {
      @Override
      public void run() {
        if (!attached) attach();
        activityController.create(bundle).get().getSupportFragmentManager().beginTransaction().add(contentViewId, fragment).commit();
      }
    });
    return this;
  }

  /**
   * Creates the activity with {@link Bundle} and adds the fragment to it. Note that the fragment will be added to the view with ID 1.
   */
  public SupportFragmentController<F> create(final Bundle bundle) {
    return create(1, bundle);
  }

  @Override
  public SupportFragmentController<F> create() {
    return create(null);
  }

  @Override
  public SupportFragmentController<F> destroy() {
    shadowMainLooper.runPaused(new Runnable() {
      @Override
      public void run() {
        activityController.destroy();
      }
    });
    return this;
  }

  public SupportFragmentController<F> start() {
    shadowMainLooper.runPaused(new Runnable() {
      @Override
      public void run() {
        activityController.start();
      }
    });
    return this;
  }

  public SupportFragmentController<F> resume() {
    shadowMainLooper.runPaused(new Runnable() {
      @Override
      public void run() {
        activityController.resume();
      }
    });
    return this;
  }

  public SupportFragmentController<F> pause() {
    shadowMainLooper.runPaused(new Runnable() {
      @Override
      public void run() {
        activityController.pause();
      }
    });
    return this;
  }

  public SupportFragmentController<F> stop() {
    shadowMainLooper.runPaused(new Runnable() {
      @Override
      public void run() {
        activityController.stop();
      }
    });
    return this;
  }

  public SupportFragmentController<F> visible() {
    shadowMainLooper.runPaused(new Runnable() {
      @Override
      public void run() {
        activityController.visible();
      }
    });
    return this;
  }

  @Override
  public SupportFragmentController<F> withIntent(final Intent intent) {
    shadowMainLooper.runPaused(new Runnable() {
      @Override
      public void run() {
        activityController.withIntent(intent);
      }
    });
    return this;
  }

  private static class FragmentControllerActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      LinearLayout view = new LinearLayout(this);
      view.setId(1);

      setContentView(view);
    }
  }
}
