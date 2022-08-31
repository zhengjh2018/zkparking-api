package cn.zkparking.api.zkparkingclient.utils.request.okhttp;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * description: OkHttp自定义重试次数
 *
 */
public class RetryInterceptor implements Interceptor {
    private static final Logger LOG = LoggerFactory.getLogger(RetryInterceptor.class);

    private int  maxRetry;    // 假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）

    public RetryInterceptor(int maxRetry){
        this.maxRetry = maxRetry;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        AtomicInteger retryCount = new AtomicInteger(0);
        Response response = chain.proceed(request);

        while (!response.isSuccessful() && retryCount.incrementAndGet() <= maxRetry) {
            LOG.info("请求失败，重新发起请求");
            response = chain.proceed(request);
        }
        return response;
    }
}
