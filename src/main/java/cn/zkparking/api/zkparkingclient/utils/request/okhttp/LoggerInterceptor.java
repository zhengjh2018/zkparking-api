package cn.zkparking.api.zkparkingclient.utils.request.okhttp;

import cn.zkparking.api.zkparkingclient.utils.lang.StringUtils;
import okhttp3.*;
import okio.Buffer;
import okio.BufferedSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * description: 日志中打印出HTTP请求&响应数据
 *
 */
public class LoggerInterceptor implements Interceptor {
    private static final Logger LOG = LoggerFactory.getLogger(LoggerInterceptor.class);

    @Override
    public Response intercept(Chain chain) throws IOException {
        long startTime = System.currentTimeMillis();
        Request request = chain.request();
        String Heads = printRequestMessage(request);
        Response response = chain.proceed(request);
        long endTime = System.currentTimeMillis();
        String costTime = String.format("%.3f", (endTime - startTime) / 1000.0);
        if(Heads.indexOf("NoPrintResponse",0) == -1){
            printResponseMessage(response,costTime);
        }
        return response;
    }

    /**
     * 打印请求消息
     *
     * @param request 请求的对象
     */
    private String printRequestMessage(Request request) {
        if (request == null) {
            return StringUtils.EMPTY_STRING;
        }

        LOG.info("---------- Start ----------------");
        LOG.info("Url   : " + request.url().url().toString());
        LOG.info("Method: " + request.method());
        LOG.info("Heads : " + request.headers());
        String Heads = String.valueOf(request.headers());
        //请求头部带NoPrintParams不去打印Params内容
        if(Heads.indexOf("NoPrintParams",0)!=-1){
            return Heads;
        }
        RequestBody requestBody = request.body();
        if (requestBody == null) {
            return Heads;
        }

        if(request.body().contentType() != null) {
            String mediaType = request.body().contentType().type();
            LOG.info("mediaType   : " + mediaType);
            if (mediaType != null && mediaType.equals("multipart")) {
                // 响应如果是图片忽略
                return Heads;
            }
        }

        try {
            Buffer bufferedSink = new Buffer();
            requestBody.writeTo(bufferedSink);
            Charset charset = requestBody.contentType().charset();
            charset = charset == null ? Charset.forName("utf-8") : charset;
            LOG.info("Params: " + bufferedSink.readString(charset));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Heads;
    }

    /**
     * 打印返回消息
     *
     * @param response 返回的对象
     */
    private void printResponseMessage(Response response, String costTime) {
        if (response == null || !response.isSuccessful()) {
            return;
        }
        ResponseBody responseBody = response.body();
        String mediaType = responseBody.contentType().toString();
        if (mediaType.contains("image/")) {
            // 响应如果是图片忽略
            return;
        }
        long contentLength = responseBody.contentLength();
        BufferedSource source = responseBody.source();
        try {
            source.request(Long.MAX_VALUE); // Buffer the entire body.
        } catch (IOException e) {
            e.printStackTrace();
        }
        Buffer buffer = source.buffer();
        Charset charset = UTF_8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = Optional.ofNullable(contentType.charset()).orElse(charset);
        }
        if (contentLength != 0) {
            String result = buffer.clone().readString(charset);
            LOG.info("Response: " + result);
            LOG.info("---------- End :"+ costTime +" s----------");
        }
    }
}
