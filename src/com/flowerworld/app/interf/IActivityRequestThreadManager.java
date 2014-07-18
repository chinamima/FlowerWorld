package com.flowerworld.app.interf;

import android.content.Context;
import com.flowerworld.app.ui.base.ActivityRequestThreadManager;

public interface IActivityRequestThreadManager {
    ActivityRequestThreadManager getRequestThreadManager();

    Context getContextHanler();
}
