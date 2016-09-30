package com.auto.test.standard;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.ImmutableMap;

import java.util.List;

/**
 * 实现描述：标准json数据格式
 */
public class StdJson {

    public static final int DEFAULT_SUCCESS_CODE = 0;
    public static final int DEFAULT_FAILURE_CODE = 1;
    private final int status;
    @JsonInclude(Include.NON_NULL)
    private final Object data;
    @JsonInclude(Include.NON_NULL)
    private final String message;

    private StdJson(int status, Object data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static StdJson list(List<?> list) {
        return list(list, list.size());
    }

    public static StdJson list(List<?> list, int total) {
        return ok(ImmutableMap.of("total", total, "list", list));
    }

    public static StdJson ok() {
        return ok(null);
    }

    public static StdJson ok(String key, Object data) {
        return ok(ImmutableMap.of(key, data));
    }

    public static StdJson ok(Object data) {
        return new StdJson(DEFAULT_SUCCESS_CODE, data, null);
    }

    public static StdJson err(String message) {
        return err(DEFAULT_FAILURE_CODE, message);
    }

    public static StdJson err(int code, String message) {
        return new StdJson(code, null, message);
    }

}
