package com.weds.bean.http;

import com.google.gson.reflect.TypeToken;
import com.weds.core.base.BaseService;
import com.weds.core.utils.coder.BeanCoder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

public class HttpRequestService extends BaseService {

    @Resource
    private HttpReqParams httpReqParams;

    @Resource
    private RestTemplate restTemplate;

    /**
     * POST加密请求（参数）
     *
     * @param methodName
     * @param requestData
     * @param typeToken
     * @param uriVariables
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T sendPostRequestByEncrypt(String methodName, Object requestData, TypeToken<T> typeToken, HttpHeaders headers,
                                          Object... uriVariables) throws Exception {
        if (headers == null) {
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        //将参数和header组成一个请求
        HttpEntity<String> request = new HttpEntity<>(BeanCoder.parseObjToJson(requestData, httpReqParams.getAppSecret(),
                httpReqParams.getImageRoot(), httpReqParams.getImageType()), headers);
        String resp = restTemplate.postForObject(httpReqParams.getRequestUrl() + methodName, request,
                String.class, uriVariables);
        return getGson().fromJson(resp, typeToken.getType());
    }

    public <T> T sendPostRequestByEncrypt(String methodName, Object requestData, TypeToken<T> typeToken) throws Exception {
        return sendPostRequestByEncrypt(methodName, requestData, typeToken, null);
    }

    /**
     * POST请求（参数）
     *
     * @param methodName
     * @param requestData
     * @param typeToken
     * @param uriVariables
     * @param <T>
     * @return
     */
    public <T> T sendPostRequest(String methodName, Object requestData, TypeToken<T> typeToken, HttpHeaders headers,
                                 Object... uriVariables) throws Exception {
        if (headers == null) {
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        //将参数和header组成一个请求
        HttpEntity<String> request = new HttpEntity<>(toJson(requestData), headers);
        String resp = restTemplate.postForObject(httpReqParams.getRequestUrl() + methodName, request, String.class,
                uriVariables);
        return getGson().fromJson(resp, typeToken.getType());
    }

    public <T> T sendPostRequest(String methodName, Object requestData, TypeToken<T> typeToken) throws Exception {
        return sendPostRequest(methodName, requestData, typeToken, null);
    }
}
