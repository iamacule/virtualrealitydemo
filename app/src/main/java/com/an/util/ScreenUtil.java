package com.an.util;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.*;

/**
 * Created by chi on 26/08/15.
 */
public class ScreenUtil {
    private static DisplayMetrics dm = new DisplayMetrics();
    /*
     * Get screen Width
     */
    public static float getScreenWidth(WindowManager windowManager){
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /*
     * Get Screen Height
     */
    public static float getScreenHeight(WindowManager windowManager){
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /*
     * get status bar height
     */
    public static int getStatusBarHeight(Activity activity) {
        Rect rect = new Rect();
        Window window = activity.getWindow();
        if (window != null) {
            window.getDecorView().getWindowVisibleDisplayFrame(rect);
            View v = window.findViewById(Window.ID_ANDROID_CONTENT);

            Display display = ((WindowManager) activity.getSystemService(activity.WINDOW_SERVICE)).getDefaultDisplay();

            //return result title bar height
            return display.getHeight() - v.getBottom();
        }
        return 0;
    }
}
