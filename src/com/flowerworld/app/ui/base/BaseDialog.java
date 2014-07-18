package com.flowerworld.app.ui.base;

import android.app.Dialog;
import android.content.Context;
import com.flowerworld.app.interf.IActivityRequestThreadManager;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.tool.http.HttpRequestFacade;

public class BaseDialog extends Dialog implements IActivityRequestThreadManager {

    protected final String TAG = this.getClass().getSimpleName();
    private ActivityRequestThreadManager requestThreadManager = new ActivityRequestThreadManager();

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        initialize();
    }

    public BaseDialog(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        requestThreadManager.stopAllCurrentActivityRequestThreads();
    }

    @Override
    public ActivityRequestThreadManager getRequestThreadManager() {
        return requestThreadManager;
    }

    public void requestHttp(IHttpProcess process) {
        HttpRequestFacade.requestHttp(this, process);
    }

    @Override
    public Context getContextHanler() {
        return getContext();
    }

//	public void requestHttp(int sign, IHttpProcess process)
//	{
//		HttpRequestFacade.requestHttp(this, process, sign);
//	}

}
