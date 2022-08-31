package cn.zkparking.api.zkparkingclient.utils.request.okhttp;

import cn.zkparking.api.zkparkingclient.utils.json.JsonHelper;
import cn.zkparking.api.zkparkingclient.utils.lang.CollectionUtils;
import cn.zkparking.api.zkparkingclient.utils.lang.StringUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import okhttp3.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * description: 链式编程，建造者模式
 *
 */
public class SimpleHttpClient {

    /**
     * 请求方式
     */
    private enum Method {
           GET, POST
    }

    private Builder mBuilder;

    private SimpleHttpClient(Builder mBuilder){
        this.mBuilder = mBuilder;
    }

    /**
     * 构造方法私有化
     */
    private SimpleHttpClient(){

    }

    private RequestBody buildRequestBody() {

        if (mBuilder.jsonParamFlag) {
            // 转换为JSON对象
            String json = mBuilder.params == null ? "" : JsonHelper.toJSONString(mBuilder.params);
            return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        }

        if (mBuilder.xmlParamFlag) {
            //解决XStream对出现双下划线的bug
            XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));

            //将要提交给API的数据对象转换成XML格式数据Post给API
            String postDataXML = xStreamForRequestPostData.toXML(mBuilder.xmlObj);

            return RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), postDataXML);
        }

        if(mBuilder.urlEncodeFlag) {
            if(mBuilder.params != null) {
                FormBody.Builder builder = new FormBody.Builder();
                mBuilder.params.entrySet().stream().forEach(e -> {
                    builder.add(e.getKey(), (String) e.getValue());
                });
                return builder.build();
            }
        }

        if(mBuilder.jsonPostData){
            String json = mBuilder.postData == null ? "" : mBuilder.postData;
            return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mBuilder.postData);
        }

        // 原始表单 body 提交方式
        if (StringUtils.isNotEmpty(mBuilder.postData)) {
            MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
            return RequestBody.create(mediaType, mBuilder.postData);
        }

        // 上传文件,二进制数据传输
        if (null != mBuilder.uploadFile && StringUtils.isNotEmpty(mBuilder.formDataName)) {
            return new MultipartBody.Builder()
                    .setType(MediaType.parse("multipart/form-data"))
                    .addFormDataPart(mBuilder.formDataName,
                            mBuilder.uploadFile.getName(),
                            RequestBody.create(MediaType.parse("application/octet-stream"), mBuilder.uploadFile))
                    .build();
        }

        FormBody.Builder builder = new FormBody.Builder();
        // 若参数不为空
        if (null != mBuilder.params && !mBuilder.params.isEmpty()) {
            mBuilder.params.forEach((key, value) -> builder.add(key, value == null ? "" : value + ""));
        }
        return builder.build();
    }

    public Headers buildHeaders() {
        Headers.Builder builder = new Headers.Builder();
        if (CollectionUtils.isNotEmpty(this.mBuilder.headers)) {
            this.mBuilder.headers.forEach(builder::add);
        }
        return builder.build();
    }

    public Request.Builder builder() {
        Request.Builder builder = new Request.Builder();
        if (this.mBuilder.method == Method.GET) {
            builder.url(buildGetRequestParam());
            builder.get();
        } else if (this.mBuilder.method == Method.POST) {
            builder.post(buildRequestBody());
            builder.url(mBuilder.url);
        }
        return builder;
    }

    public Request buildRequest() {
        return builder().headers(buildHeaders()).build();
    }

    /**
     * 构建Get请求的参数，并拼装完整的URL
     *
     * @return 请求的URL包括请求参数
     */
    private String buildGetRequestParam() {
        if (CollectionUtils.isEmpty(mBuilder.params)) {
            return this.mBuilder.url;
        }
        // 拼接url的参数
        String paramUrl = mBuilder.params.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((param1, param2) -> param1 + "&" + param2).orElse("");
        if (this.mBuilder.url.contains("?")) {
            return this.mBuilder.url + "&" + paramUrl;
        }
        return this.mBuilder.url + "?" + paramUrl;
    }

    public void doRequestASync(BaseCallback callback) {
        OkHttpUtils.instance().requestASync(this, callback);
    }

    public void doRequestSync(BaseCallback callback) {
        OkHttpUtils.instance().requestSync(this, callback);
    }

    public void doRequestSyncNoThrows(BaseCallback callback) throws Exception {
        OkHttpUtils.instance().requestSyncNotThrows(this, callback);
    }



    public Object doRequest2Object() {
        return OkHttpUtils.instance().request2Object(this);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        /**
         * 待请求的url
         */
        private String              url;
        /**
         * 默认的请求方式
         */
        private Method              method        = Method.GET;

        /**
         * 是否xml格式的请求
         */
        private boolean             xmlParamFlag = false;

        /**
         * 是否JSON格式的请求
         */
        private boolean             jsonParamFlag = false;
        /**
         * 是否post JSON字符串格式的请求
         */
        private boolean             jsonPostData = false;
        /**
         * 是否JSON格式的请求
         */
        private boolean             urlEncodeFlag = false;

        /**
         * 请求参数列表
         */
        private Map<String, Object> params;

        /**
         * 请求xml对象
         */
        private Object              xmlObj;

        /**
         * post请求的请求体
         */
        private String              postData;

        /**
         * 上传文件
         */
        private File                uploadFile;

        /**
         * 上传文件的表单名称
         */
        private String              formDataName;

        /**
         * 自定义请求头列表
         */
        private Map<String, String> headers;

        /**
         * 请求的url
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder addXmlObject(Object xmlObj) {
            this.xmlObj = xmlObj;
            this.xmlParamFlag = true;
            return this;
        }

        public Builder addPostData(String postData) {
            this.postData = postData;
            return this;
        }

        public Builder uploadFile(File file, String formDataName) {
            this.uploadFile = file;
            this.formDataName = formDataName;
            return this;
        }

        /**
         * GET 请求方式
         */
        public Builder get() {
            return this;
        }

        /**
         * POST 请求方式
         */
        public Builder post() {
            this.method = Method.POST;
            return this;
        }

        /**
         * JSON 格式参数
         */
        public Builder json() {
            jsonParamFlag = true;
            return this;
        }

        public Builder xml() {
            xmlParamFlag = true;
            return this;
        }

        public Builder urlencode() {
            urlEncodeFlag = true;
            return this;
        }

        public Builder jsonPostData() {
            jsonPostData = true;
            return this;
        }

        /**
         * 添加请求参数
         */
        public Builder addParam(String key, String value) {
            if (params == null) {
                params = new HashMap<>();
            }
            params.put(key, value);
            return this;
        }

        /**
         * 添加请求参数集合
         */
        public Builder addParams(Map<String, Object> reqParams) {
            if (params == null) {
                params = new HashMap<>();
            }
            if (CollectionUtils.isNotEmpty(reqParams)) {
                this.params.putAll(reqParams);
            }
            return this;
        }

        /**
         * 添加自定义请求头
         */
        public Builder addHeader(String key, String value) {
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(key, value);
            return this;
        }

        /**
         * 添加请求头集合
         */
        public Builder addHeaders(Map<String, String> headers) {
            if (this.headers == null) {
                this.headers = new HashMap<>();
            }
            if (CollectionUtils.isNotEmpty(headers)) {
                this.headers.putAll(headers);
            }
            return this;
        }

        public SimpleHttpClient build() {
            return new SimpleHttpClient(this);
        }
    }

}
