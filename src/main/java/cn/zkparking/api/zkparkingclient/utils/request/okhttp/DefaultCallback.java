package cn.zkparking.api.zkparkingclient.utils.request.okhttp;

import okhttp3.Call;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description: 默认的请求回调实现
 *
 */
public class DefaultCallback<T> extends BaseCallback<T> {

    /**
     * Logger for DefaultCallback
     */
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCallback.class);

    public DefaultCallback(){
        super();
    }

    @Override
    public void onSuccess(T t) {
        super.onSuccess(t);
    }

    @Override
    public void onError(int code) {
        LOG.error("okhttp3 requestASync error, code : {}", code);
        super.onError(code);
    }

    @Override
    public void onFailure(Call call, Exception ex) {
        LOG.error("okhttp3 requestASync failure, ex : {}", ex);
        super.onFailure(call, ex);
    }
}
