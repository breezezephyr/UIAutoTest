package com.auto.test.recognize;

import com.auto.test.standard.BusinessException;
import com.auto.test.utils.JsonUtils;
import com.auto.test.utils.RestUtils;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 实现描述：http://www.ruokuai.com 打码平台
 *
 */
@Service
public class ImgRecognizer {

    private static final String API = "http://api.ruokuai.com/create.json";
    private static final String username = "";
    private static final String password = "";
    // http://www.ruokuai.com/home/pricetype
    private static final String typeid = ""; // 3040 - 4位英数混合
    private static final String softid = "";
    private static final String softkey = "";
    private static final int TIMEOUT = 60000;

    public String recognize(File imageFile) {
        if (imageFile == null)
            throw new BusinessException(10006, "没有指定图片"); // 10006 = 若快验证码识别失败
        HttpEntity body = MultipartEntityBuilder.create().addTextBody("username", username)
                .addTextBody("password", password).addTextBody("typeid", typeid).addTextBody("softid", softid)
                .addTextBody("softkey", softkey).addBinaryBody("image", imageFile).build();
        String response = RestUtils.post(API, body, TIMEOUT);
        return JsonUtils.parse(response).path("Result").asText("");
    }

}
