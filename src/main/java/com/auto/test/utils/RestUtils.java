package com.auto.test.utils;

import com.auto.test.standard.BusinessException;
import com.google.common.base.Charsets;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 实现描述：HttpClient工具
 */
public abstract class RestUtils {
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int SOCKET_TIMEOUT = 10000;

    public static String get(String url) {
        return execute(Request.Get(url));
    }

    public static String post(String url, Form form) {
        return execute(Request.Post(url).bodyForm(form.build(), Charsets.UTF_8));
    }

    public static String post(String url, Form form, int socketTimeout) {
        return execute(Request.Post(url).bodyForm(form.build(), Charsets.UTF_8), socketTimeout);
    }

    public static String post(String url, String json) {
        return execute(Request.Post(url).bodyString(json, ContentType.APPLICATION_JSON));
    }

    public static String post(String url, String name, File file) {
        HttpEntity body = MultipartEntityBuilder.create().addBinaryBody(name, file).build();
        return execute(Request.Post(url).body(body));
    }

    public static String post(String url, HttpEntity body, Header... headers) {
        return execute(Request.Post(url).setHeaders(headers).body(body));
    }

    public static String post(String url, HttpEntity body, int socketTimeout) {
        return execute(Request.Post(url).body(body), socketTimeout);
    }

    private static String execute(Request request) {
        return execute(request, SOCKET_TIMEOUT);
    }

    private static String execute(Request request, int socketTimeout) {
        try {
            Content content = request.connectTimeout(CONNECT_TIMEOUT).socketTimeout(socketTimeout).execute()
                    .returnContent();
            Charset charset = content.getType().getCharset();
            return new String(content.asBytes(), charset != null ? charset : Charsets.UTF_8);
        } catch (IOException e) {
            throw new BusinessException(10004, e); // 10004 = HTTP请求异常
        }
    }

}
