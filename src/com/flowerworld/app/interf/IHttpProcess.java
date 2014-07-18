package com.flowerworld.app.interf;

import java.util.Map;

public interface IHttpProcess {
    /**
     * 获取访问地址
     *
     * @return 返回访问地址
     */
    String processUrl(int sign);

//	/**
//	 * 获取请求标识
//	 * 
//	 * @return 返回请求标识
//	 */
//	int processSign();

    /**
     * 填入访问参数
     *
     * @param params 参数的集合
     * @param sign   请求标识
     */
    void processParams(Map<String, Object> params, int sign);

    /**
     * 访问成功的响应
     *
     * @param resultStr 响应字符串，http请求的结果
     * @param sign      请求标识
     * @return true--由本方法处理；false--由父类处理；
     */
    boolean processResponseSucceed(String resultStr, int sign) throws Exception;

    /**
     * 访问失败的响应
     *
     * @param resultStr 响应字符串，http请求的结果
     * @param sign      请求标识
     * @return true--由本方法处理；false--由父类处理；
     */
    boolean processResponseFailed(String resultStr, int sign) throws Exception;
}
