package com.androxue.login.widget;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.androxue.login.R;

/**
 * Created by JimCharles on 2017/10/8.
 */

public class SplashScreen {

    public final static int SLIDE_LEFT = 1;
    private final static int SLIDE_UP = 2;
    private final static int FADE_OUT = 3;

    private Activity activity;
    private Dialog splashDialog;

    public SplashScreen(Activity activity) {
        this.activity = activity;
    }

    public void show(final int imageResource, final int animation) {
        Runnable runnable = new Runnable() {
            public void run() {
                // Get reference to display
                DisplayMetrics metrics = new DisplayMetrics();

                // Create the layout for the dialog
                LinearLayout root = new LinearLayout(activity);
                root.setMinimumHeight(metrics.heightPixels);
                root.setMinimumWidth(metrics.widthPixels);
                root.setOrientation(LinearLayout.VERTICAL);
                root.setBackgroundColor(Color.BLACK);
                root.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, 0.0F));
                root.setBackgroundResource(imageResource);

                // Create and show the dialog
                splashDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
                // check to see if the splash screen should be full screen
                if ((activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                        == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
                    splashDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
                Window window = splashDialog.getWindow();
                switch (animation) {
                    case SLIDE_LEFT:
                        assert window != null;
                        window.setWindowAnimations(R.style.dialog_anim_slide_left);
                        break;
                    case SLIDE_UP:
                        assert window != null;
                        window.setWindowAnimations(R.style.dialog_anim_slide_up);
                        break;
                    case FADE_OUT:
                        assert window != null;
                        window.setWindowAnimations(R.style.dialog_anim_fade_out);
                        break;
                }

                splashDialog.setContentView(root);
                splashDialog.setCancelable(false);
                splashDialog.show();

            }
        };
        activity.runOnUiThread(runnable);
    }

    public void removeSplashScreen() {
        if (splashDialog != null && splashDialog.isShowing()) {
            splashDialog.dismiss();
            splashDialog = null;
        }
    }

}
