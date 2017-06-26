package com.coderschool.nytimes.Utils;

import android.content.res.Resources;

/**
 * Created by Admin on 6/23/2017.
 */

public class UiUtils {

    public static int getScreenWidth(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
}
