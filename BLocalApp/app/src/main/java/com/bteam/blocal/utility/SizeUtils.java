package com.bteam.blocal.utility;

import android.content.Context;

import com.bteam.blocal.R;

public class SizeUtils {
    public static boolean isTablet(Context context){
        return context.getResources().getBoolean(R.bool.isTablet);
    }
}
