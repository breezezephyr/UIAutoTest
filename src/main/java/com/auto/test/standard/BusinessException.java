package com.auto.test.standard;

import com.google.common.collect.Maps;
import com.google.common.io.Resources;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * 实现描述：标准业务异常
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -7872272011611466850L;
    private static final Map<Integer, String> mappings = Maps.newHashMap();
    private int errCode = 1;
    private String additionMessage = null;
    private boolean replace = false;

    static {
        try (InputStream is = Resources.getResource("properties/business-exception-message.properties").openStream()) {
            Properties props = new Properties();
            props.load(is);
            for (String key : props.stringPropertyNames()) {
                if (StringUtils.isNumeric(key))
                    mappings.put(Integer.valueOf(key), props.getProperty(key));
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public BusinessException(int errCode, String additionMessage) {
        super();
        this.errCode = errCode;
        this.additionMessage = additionMessage;
    }

    public BusinessException(int errCode, String additionMessage, boolean replace) {
        super();
        this.errCode = errCode;
        this.additionMessage = additionMessage;
        this.replace = replace;
    }

    public BusinessException(int errCode, Throwable cause) {
        super(cause);
        this.errCode = errCode;
    }

    public BusinessException(int errCode, String additionMessage, Throwable cause) {
        super(cause);
        this.errCode = errCode;
        this.additionMessage = additionMessage;
    }

    public BusinessException(int errCode, String additionMessage, boolean replace, Throwable cause) {
        super(cause);
        this.errCode = errCode;
        this.additionMessage = additionMessage;
        this.replace = replace;
    }

    public int getErrCode() {
        return errCode;
    }

    public String getAdditionMessage() {
        return additionMessage;
    }

    @Override
    public String getMessage() {
        if (replace && additionMessage != null) {
            return additionMessage;
        }
        if (mappings.containsKey(errCode)) {
            if (additionMessage != null)
                return mappings.get(errCode) + " - " + additionMessage;
            return mappings.get(errCode);
        }
        return additionMessage != null ? additionMessage : "未知错误";
    }

    public static BusinessException handle(Throwable e, int errCode, String message) {
        if (e instanceof BusinessException)
            return (BusinessException) e;
        if (message != null)
            return new BusinessException(errCode, message, e);
        return new BusinessException(errCode, e);
    }

}
