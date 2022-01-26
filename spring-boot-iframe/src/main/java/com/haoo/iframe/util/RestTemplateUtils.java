package com.haoo.iframe.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

/**
 * SpringRestTemplate 操作工具类
 * <p>
 *     用于调用第三方 API 接口
 * </p>
 *
 * @author pluto
 * @see RestTemplateUtils
 **/
@Slf4j
public class RestTemplateUtils {

    private static final String PREFIX = "https://";

    /**
     * 无参构造
     */
    private RestTemplateUtils() {
    }

    /**
     * HTTP 单例实例
     * @return HTTP 请求模版
     */
    public static RestTemplate getInstance() {
        return SingletonRestTemplate.INSTANCE;
    }

    /**
     * HTTPS 单例
     */
    public static RestTemplate getHttpsInstance() {
        return HttpsSingletonRestTemplate.INSTANCE;
    }

    /**
     * HTTP 格式接口调用
     * @param url   url
     * @param method    请求方式
     * @param httpHeaders   头信息，可以为空
     * @param body  Body 体
     * @return  响应实体
     */
    private static ResponseEntity<String> baseExecute(String url, HttpMethod method, HttpHeaders httpHeaders, String body) {
//        判断是否存在 HttpHeaders 没有则创建
        if (httpHeaders == null || httpHeaders.isEmpty()) {
            httpHeaders = new HttpHeaders();
        }
//        默认设置头信息
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Accept", "application/json");
        httpHeaders.add("Accept-Charset", "UTF-8");
        httpHeaders.add("Cache-Control", "no-cache");
        if (body == null || body.trim().length() == 0) {
            body = "{}";
        }
//        将头信息、请求体生成 HTTP 实体
        HttpEntity<String> entity = new HttpEntity<String>(body, httpHeaders);
//        创建生成的单例 HTTP 的 RestTemplate 对象
//        通过单例对象 RestTemplate 做实际请求
        return getInstance().exchange(url, method, entity, String.class);
    }

    /**
     * HTTPS 格式接口调用
     * @param url   url
     * @param method    请求方式
     * @param httpHeaders   头信息，可以为空
     * @param body  Body 体
     * @return  响应实体
     */
    private static ResponseEntity<String> httpsBaseExecute(String url, HttpMethod method, HttpHeaders httpHeaders, String body) {
//        判断是否存在 HttpHeaders 没有则创建
        if (httpHeaders == null || httpHeaders.isEmpty()) {
            httpHeaders = new HttpHeaders();
        }
//        默认设置头信息
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Accept", "application/json");
        httpHeaders.add("Accept-Charset", "UTF-8");
        httpHeaders.add("Cache-Control", "no-cache");
        if (body == null || body.trim().length() == 0) {
            body = "{}";
        }
//        将头信息、请求体生成 HTTP 实体
        HttpEntity<String> entity = new HttpEntity<String>(body, httpHeaders);
//        创建生成的单例 HTTPS 的 RestTemplate 对象
//        通过单例对象 RestTemplate 做实际请求
        return getHttpsInstance().exchange(url, method, entity, String.class);
    }

    /**
     * 通用请求方法
     * @param url   url
     * @param method    请求方式
     * @param body  请求体
     * @param httpHeaders   请求头
     * @return  响应实体
     */
    private static ResponseEntity<String> baseExecute(String url, HttpMethod method, String body, HttpHeaders httpHeaders) {
        if (StringUtils.startsWithIgnoreCase(url, PREFIX)) {
//            执行 HTTPS
            return httpsBaseExecute(url, method, httpHeaders, body);
        }
//        执行 HTTP
        return baseExecute(url, method, httpHeaders, body);
    }

    /**
     * 返回 RESPONSE BODY 字符串响应的请求方式
     * @param url   url
     * @param method    请求方式
     * @param body  请求体
     * @param httpHeaders   请求头
     * @return  响应的 Body 字符串
     */
    private static String executeForBody(String url, HttpMethod method, String body, HttpHeaders httpHeaders) {
//        调用通用请求
        ResponseEntity<String> responseEntity = baseExecute(url, method, body, httpHeaders);
        log.info("REST_TEMPLATE: url:{}, method:{}, body:{} ,headers:{}", url, method, body,httpHeaders);
        if (responseEntity != null) {
//            如果返回的状态为 200 或 202 则标识成功返回 Body 域字符串（HTTP=200；HTTPS=202）
            if (HttpStatus.OK.equals(responseEntity.getStatusCode())|| HttpStatus.ACCEPTED.equals(responseEntity.getStatusCode())) {
                return responseEntity.getBody();
            } else {
//                不为 200 或 202 表示请求失败了
                log.error("REST_TEMPLATE: http response error, status is" + responseEntity.getStatusCode());
                throw new HttpServerErrorException(responseEntity.getStatusCode(), "请求异常，url: " + url);
            }
        }
//        响应错误
        log.error("REST_TEMPLATE: http response error, responseEntity:null");
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "请求异常，url: " + url);
    }

    /**
     * 返回 ResponseEntity 响应实体的请求方式
     * @param url   url
     * @param method    请求方法
     * @param body  参数
     * @param httpHeaders   头信息，可以为空
     * @return  响应实体
     */
    private static ResponseEntity<String> executeForAll(String url, HttpMethod method, String body, HttpHeaders httpHeaders) {
//        调用通用请求
        ResponseEntity<String> responseEntity = baseExecute(url, method, body, httpHeaders);
        log.info("REST_TEMPLATE: url:{}, method:{}, body:{} ", url, method, body);
        if (responseEntity != null) {
//            如果返回的状态为 200 或 202 则标识成功返回 Body 域字符串（HTTP=200；HTTPS=202）
            if (HttpStatus.OK.equals(responseEntity.getStatusCode())|| HttpStatus.ACCEPTED.equals(responseEntity.getStatusCode())) {
                return responseEntity;
            } else {
//                不为 200 或 202 表示请求失败了
                log.error("REST_TEMPLATE: http response error, status is" + responseEntity.getStatusCode());
                throw new HttpServerErrorException(responseEntity.getStatusCode(), "请求异常，url: " + url);
            }
        }
//        响应错误
        log.error("REST_TEMPLATE: http response error, responseEntity:null");
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "请求异常，url: " + url);
    }

    //************************************** 返回 RESPONSE BODY 响应 sta **************************************/

    /**
     * POST 方式请求接口
     * @param url   url
     * @param body  请求体
     * @param httpHeaders   请求头
     * @return  Body域的字符串
     * @throws Exception    请求异常
     */
    public static String postForBody(String url, String body, HttpHeaders httpHeaders) throws Exception {
        return executeForBody(url, HttpMethod.POST, body, httpHeaders);
    }

    /**
     * GET 方式请求接口
     * @param url   url
     * @param httpHeaders   请求头
     * @return  Body域的字符串
     * @throws Exception    请求异常
     */
    public static String getForBody(String url, HttpHeaders httpHeaders) throws Exception {
        return getForBody(url, "", httpHeaders);
    }

    /**
     * GET 方式请求接口
     * @param url   url
     * @param body  请求体
     * @param httpHeaders   请求头
     * @return  Body域的字符串
     * @throws Exception    请求异常
     */
    public static String getForBody(String url, String body, HttpHeaders httpHeaders) throws Exception {
        return executeForBody(url, HttpMethod.GET, body, httpHeaders);
    }

    /**
     * PUT 方式请求接口
     * @param url   url
     * @param body  请求体
     * @param httpHeaders   请求头
     * @return  Body域的字符串
     * @throws Exception    请求异常
     */
    public static String putForBody(String url, String body, HttpHeaders httpHeaders) throws Exception {
        return executeForBody(url, HttpMethod.PUT, body, httpHeaders);
    }

    /**
     * DELETE 方式请求接口
     * @param url   url
     * @param body  请求体
     * @param httpHeaders   请求头
     * @return  Body域的字符串
     * @throws Exception    请求异常
     */
    public static String deleteForBody(String url, String body, HttpHeaders httpHeaders) throws Exception {
        return executeForBody(url, HttpMethod.DELETE, body, httpHeaders);
    }

    //************************************** 返回 RESPONSE BODY 响应 sta **************************************/

    //************************************** 返回 ResponseEntity 响应体 sta **************************************/

    /**
     * POST 方式请求接口
     * @param url   url
     * @param body  参数
     * @param httpHeaders   头部数据
     * @return  响应实体
     * @throws Exception    请求异常
     */
    public static ResponseEntity<String> post(String url, String body, HttpHeaders httpHeaders) throws Exception {
        return executeForAll(url, HttpMethod.POST, body, httpHeaders);
    }

    /**
     * GET 方式请求接口
     * @param url   url
     * @param httpHeaders   头部
     * @return  响应实体
     * @throws Exception    请求异常
     */
    public static ResponseEntity<String> get(String url, HttpHeaders httpHeaders) throws Exception {
        return get(url, "", httpHeaders);
    }

    /**
     * GET 方式请求接口
     * @param url   url
     * @param body  参数
     * @param httpHeaders   头部
     * @return  响应实体
     * @throws Exception    请求异常
     */
    public static ResponseEntity<String> get(String url, String body, HttpHeaders httpHeaders) throws Exception {
        return executeForAll(url, HttpMethod.GET, body, httpHeaders);
    }

    /**
     * PUT 方式请求接口
     * @param url   url
     * @param body  参数
     * @param httpHeaders   头部
     * @return  响应实体
     * @throws Exception    请求异常
     */
    public static ResponseEntity<String> put(String url, String body, HttpHeaders httpHeaders) throws Exception {
        return executeForAll(url, HttpMethod.PUT, body, httpHeaders);
    }

    /**
     * DELETE 方式请求接口
     * @param url   url
     * @param body  参数
     * @param httpHeaders   头部
     * @return  响应实体
     * @throws Exception    请求异常
     */
    public static ResponseEntity<String> delete(String url, String body, HttpHeaders httpHeaders) throws Exception {
        return executeForAll(url, HttpMethod.DELETE, body, httpHeaders);
    }

    //************************************** 返回 ResponseEntity 响应体 end **************************************/

    /**
     * HTTP 单例
     * 使 RestTemplate 单例化
     */
    private static class SingletonRestTemplate {
        static final RestTemplate INSTANCE = new RestTemplate();
    }

    /**
     * HTTPS 单例
     * 使 RestTemplate 单例化
     */
    private static class HttpsSingletonRestTemplate {
        static final RestTemplate INSTANCE = new RestTemplate(getRequestFactory());
    }

    /**
     * 构建新的 HTTP实例，用于调用 HTPPS 请求
     * @return  新的实例 HTTP
     */
    private static HttpComponentsClientHttpRequestFactory getRequestFactory() {
//        创建一个新的实例 HTTP
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(sslClient());
        return requestFactory;
    }

    /**
     * 生成 HTTP 客户端对象
     * @return  客户端对象
     */
    private static HttpClient sslClient() {
        SSLContext sslContext = null;
        try {
//            创建 SSL 上下文对象
            sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).setProtocol("TLS").build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert sslContext != null;
//        创建 SSL 连接 工厂
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
//        通过工厂创建 HTTP 客户端对象
        return HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
    }

}
