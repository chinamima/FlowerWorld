package com.flowerworld.app.tool.http;

import android.content.Context;
import com.flowerworld.app.interf.IActivityRequestThreadManager;
import com.flowerworld.app.interf.IHttpProcess;

import java.lang.ref.SoftReference;

class RequestHolder {
    SoftReference<IActivityRequestThreadManager> handlerRef;
    SoftReference<Context> contextRef;
    //	SoftReference<IHttpProcess> processRef;
    IHttpProcess process;
    String resultStr;
    int sign;
}
