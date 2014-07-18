package com.flowerworld.app.tool.http;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import com.flowerworld.app.R;
import com.flowerworld.app.interf.IActivityRequestThreadManager;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.tool.util.*;
import com.google.gson.JsonObject;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * http请求的门面类，全部统一调用此类
 *
 * @author Guo.Jinjun
 */
public class HttpRequestFacade {
    protected final static String TAG = HttpRequestFacade.class.getSimpleName();

    public static final int DEFAULT_SIGN = 0;
    //
    public static final String RESULT_PARAMS_ERROR = "error";
    public static final String RESULT_PARAMS_MSG = "msg";
    public static final String RESULT_PARAMS_RESULT = "result";

    public static final String REQUEST_GET = "GET";
    public static final String REQUEST_POST = "POST";
    public static final String REQUEST_POST_FILE = "POST_FILE";

    /**
     * 发起http请求
     *
     * @param handler 发起源，当发起源销毁时，http线程也将销毁
     * @param process 参数和结果接口
     */
    public static void requestHttp(IActivityRequestThreadManager handler, IHttpProcess process) {
        requestHttp(handler, process, DEFAULT_SIGN, REQUEST_POST, true);
    }

    /**
     * 发起http请求
     *
     * @param handler    发起源，当发起源销毁时，http线程也将销毁
     * @param process    参数和结果接口
     * @param showDialog 是否显示请稍后对话框
     */
    public static void requestHttp(IActivityRequestThreadManager handler, IHttpProcess process, boolean showDialog) {
        requestHttp(handler, process, DEFAULT_SIGN, REQUEST_POST, showDialog);
    }

    /**
     * 发起http请求
     *
     * @param handler     发起源，当发起源销毁时，http线程也将销毁
     * @param process     参数和结果接口
     * @param requestType get或post
     */
    public static void requestHttp(IActivityRequestThreadManager handler, IHttpProcess process, String requestType) {
        requestHttp(handler, process, DEFAULT_SIGN, requestType, true);
    }

    /**
     * 发起http请求
     *
     * @param handler     发起源，当发起源销毁时，http线程也将销毁
     * @param process     参数和结果接口
     * @param sign        请求识别标识；当多个http使用同一个IHttpProcess接口时，用sign标识区分；
     * @param requestType get或post
     * @param showDialog  是否显示请稍后对话框
     */
    public static void requestHttp(IActivityRequestThreadManager handler, IHttpProcess process, int sign,
            final String requestType, final boolean showDialog) {
        Context context = null;
        if (handler instanceof Activity) {
            context = (Context) handler;
        } else if (handler instanceof Fragment) {
            context = ((Fragment) handler).getActivity();
        } else if (handler instanceof Dialog) {
            context = ((Dialog) handler).getContext();
        } else {
            LOG.e(TAG, "request handler type is illegal!");
            return;
        }

        if (null == process) {
            return;
        }

        final RequestHolder holder = new RequestHolder();
        holder.handlerRef = new SoftReference<IActivityRequestThreadManager>(handler);
        holder.contextRef = new SoftReference<Context>(context);
//		holder.processRef = new SoftReference<IHttpProcess>(process);
        holder.process = process;
        holder.sign = sign;

        if (!AppUtils.netWorkIsEnable(context)) {
//			ToastUtil.show(context, context.getResources().getString(R.string.network_unenable));
            try {
                if (!process.processResponseFailed(HttpResultStatus.ERROR_NO_NETWORK, holder.sign)) {
                    processFailedResultDefault(HttpResultStatus.ERROR_NO_NETWORK);
                }
            } catch (Exception e) {
                e.printStackTrace();
                processFailedResultDefault(HttpResultStatus.ERROR_NO_NETWORK);
            }
            return;
        } else {
            if (showDialog) {
                ProgressDialogUtil.show(context, null);
            }

            AsyncTask<RequestHolder, Integer, RequestHolder> at = new AsyncTask<RequestHolder, Integer, RequestHolder>() {

                protected RequestHolder doInBackground(RequestHolder... params) {
                    RequestHolder holder = params[0];
                    IActivityRequestThreadManager handler = holder.handlerRef.get();
                    Context context = holder.contextRef.get();
//							IHttpProcess process = holder.processRef.get();
                    IHttpProcess process = holder.process;

                    if (null == handler || null == context || null == process) {
                        if (showDialog) {
                            ProgressDialogUtil.dismiss();
                        }
                        cancel(true);
                        return null;
                    }

                    Map<String, Object> map = new HashMap<String, Object>();
                    process.processParams(map, holder.sign);

                    if (REQUEST_POST == requestType) {
                        holder.resultStr = HttpRequestHelper.requestHttpPost(process.processUrl(holder.sign), map);
                    } else if (REQUEST_POST_FILE == requestType) {
                        holder.resultStr = HttpRequestHelper.requestHttpPostWithFile(process.processUrl(holder.sign), map);
                    } else {
                        holder.resultStr = HttpRequestHelper.requestHttpGet(process.processUrl(holder.sign), map);
                    }
                    LOG.d(TAG, "=====request=resultStr: " + holder.resultStr);

                    if (null == handler || null == context || null == process) {
                        if (showDialog) {
                            ProgressDialogUtil.dismiss();
                        }
                        cancel(true);
                        return null;
                    }

                    return holder;
                }

                protected void onPostExecute(RequestHolder holder) {
                    if (null == holder) {
                        return;
                    }

                    IActivityRequestThreadManager handler = holder.handlerRef.get();
                    Context context = holder.contextRef.get();
//							IHttpProcess process = holder.processRef.get();
                    IHttpProcess process = holder.process;

                    if (null == handler || null == context || null == process) {
                        if (showDialog) {
                            ProgressDialogUtil.dismiss();
                        }
                        cancel(true);
                        return;
                    }

                    if (showDialog) {
                        ProgressDialogUtil.dismiss();
                    }

                    if (judge(context, holder.resultStr)) {
                        try {
                            if (!process.processResponseSucceed(holder.resultStr, holder.sign)) {
                                processSucceedResultDefault(holder.resultStr);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            processSucceedResultDefault(holder.resultStr);
                        }
                    } else {
                        try {
                            if (!process.processResponseFailed(holder.resultStr, holder.sign)) {
                                processFailedResultDefault(holder.resultStr);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            processFailedResultDefault(holder.resultStr);
                        }
                    }
                    handler.getRequestThreadManager().removeRequestThread(this);
                }

                protected void onCancelled() {
                    if (showDialog) {
                        ProgressDialogUtil.dismiss();
                    }
                }

                ;
            }.execute(holder);

            handler.getRequestThreadManager().registerRequestThread(at);
        }
    }

    /**
     * 判断请求结果的是否成功
     *
     * @param resultStr 请求结果字符串
     * @return true---成功；false---失败
     */
    private static boolean judge(Context context, String resultStr) {
        try {
            if (HttpResultStatus.ERROR_DEFAULT.equals(resultStr)) {
                return false;
            } else if (HttpResultStatus.ERROR_CONNECT_SERVER_FAILED.equals(resultStr)) {
                return false;
            } else if (HttpResultStatus.ERROR_ILLEGAL_URL.equals(resultStr)) {
                return false;
            } else if (HttpResultStatus.ERROR_NO_NETWORK.equals(resultStr)) {
                return false;
            } else if (HttpResultStatus.ERROR_REQUEST_PARAMETER.equals(resultStr)) {
                return false;
            } else if (HttpResultStatus.ERROR_RESPONSE_EMPTY.equals(resultStr)) {
                return false;
            } else if (HttpResultStatus.ERROR_TIME_OUT.equals(resultStr)) {
                return false;
            }

            JsonObject resultJsonObj = GsonJsonUtil.parse(resultStr).getAsJsonObject();
            int error = GsonJsonUtil.optInt(resultJsonObj.get(RESULT_PARAMS_ERROR), -1);
            String msg = GsonJsonUtil.optString(resultJsonObj.get(RESULT_PARAMS_MSG), "");
            if (0 < error) {
                LOG.i(TAG, msg);
                return true;
            } else {
                LOG.e(TAG, msg);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG.e(TAG, e.getLocalizedMessage());
        }
        return false;
    }

    public static void processSucceedResultDefault(String resultStr) {
        try {
            JsonObject resultJsonObj = GsonJsonUtil.parse(resultStr).getAsJsonObject();
//			int error = GsonJsonUtil.optInt(resultJsonObj.get(RESULT_PARAMS_ERROR), -1);
            String msg = GsonJsonUtil.optString(resultJsonObj.get(RESULT_PARAMS_MSG), "");
            if (TextUtils.isEmpty(msg)) {
                return;
            }
            ToastUtil.show(ApplicationContextUtil.getApplicationContext(), msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void processFailedResultDefault(String resultStr) {
        try {
            if (HttpResultStatus.ERROR_DEFAULT.equals(resultStr)) {
                ToastUtil.show(ApplicationContextUtil.getApplicationContext(), R.string.error_default);
            } else if (HttpResultStatus.ERROR_CONNECT_SERVER_FAILED.equals(resultStr)) {
                ToastUtil.show(ApplicationContextUtil.getApplicationContext(), R.string.error_connect_server_failed);
            } else if (HttpResultStatus.ERROR_ILLEGAL_URL.equals(resultStr)) {
                ToastUtil.show(ApplicationContextUtil.getApplicationContext(), R.string.error_illegal_url);
            } else if (HttpResultStatus.ERROR_NO_NETWORK.equals(resultStr)) {
                ToastUtil.show(ApplicationContextUtil.getApplicationContext(), R.string.error_no_network);
            } else if (HttpResultStatus.ERROR_REQUEST_PARAMETER.equals(resultStr)) {
                ToastUtil.show(ApplicationContextUtil.getApplicationContext(), R.string.error_request_parameter);
            } else if (HttpResultStatus.ERROR_RESPONSE_EMPTY.equals(resultStr)) {
                ToastUtil.show(ApplicationContextUtil.getApplicationContext(), R.string.error_response_empty);
            } else if (HttpResultStatus.ERROR_TIME_OUT.equals(resultStr)) {
                ToastUtil.show(ApplicationContextUtil.getApplicationContext(), R.string.error_time_out);
            } else {
                JsonObject resultJsonObj = GsonJsonUtil.parse(resultStr).getAsJsonObject();
                String msg = GsonJsonUtil.optString(resultJsonObj.get(RESULT_PARAMS_MSG), "");
                if (TextUtils.isEmpty(msg)) {
                    return;
                }
                ToastUtil.show(ApplicationContextUtil.getApplicationContext(), msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JsonObject paserResultObj(String resultStr) {
        JsonObject obj = GsonJsonUtil.parse(resultStr).getAsJsonObject();
        return obj.get(HttpRequestFacade.RESULT_PARAMS_RESULT).getAsJsonObject();
    }
}
