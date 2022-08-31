package cn.zkparking.api.zkparkingclient.utils.request.okhttp;

import cn.zkparking.api.zkparkingclient.utils.json.JsonHelper;
import okhttp3.*;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * description: 全局统一使用的OkHttpClient工具，okhttp版本：okhttp3
 *
 */
public class OkHttpUtils {
    private static final Logger LOG = LoggerFactory.getLogger(OkHttpUtils.class);

    public static final long  DEFAULT_READ_TIMEOUT_MILLIS       = 15 * 1000;
    public static final long  DEFAULT_WRITE_TIMEOUT_MILLIS      = 20 * 1000;
    public static final long  DEFAULT_CONNECT_TIMEOUT_MILLIS    = 20 * 1000;
    private static final long HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;
    private OkHttpClient mOkHttpClient;

    /**
     * 防止单例模式被JAVA反射攻击
     */
    private static boolean    flag                              = false;

    private OkHttpUtils(){

        synchronized (OkHttpUtils.class) {
            if (!flag) {
                flag = true;
                // HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                //// 请求及响应报文，包含header、body数据
                // loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                mOkHttpClient = new OkHttpClient.Builder()
                        .readTimeout(DEFAULT_READ_TIMEOUT_MILLIS,TimeUnit.MILLISECONDS)
                        .writeTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS,TimeUnit.MILLISECONDS)
                        .connectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS,TimeUnit.MILLISECONDS)
                          // FaceBook 网络调试器，可在Chrome调试网络请求，查看SharePreferences,数据库等
                          // .addNetworkInterceptor(new StethoInterceptor())
                          // http数据log，日志中打印出HTTP请求&响应数据
                          // .addInterceptor(loggingInterceptor)
                          .addInterceptor(new LoggerInterceptor()) // 自定义输出日志
                          // .retryOnConnectionFailure(false)
                          .addInterceptor(new RetryInterceptor(3))// 重试3次
                          // .sslSocketFactory(sslSocketFactory, trustManager)// Okhttp
                          // 访问自签名证书 HTTPS 地址
                          .build();
            } else {
                throw new RuntimeException("单例模式被侵犯！");
            }
        }

    }

    /**
     * 对外提供的获取支持自签名的okhttp客户端
     *
     * @param certificate 自签名证书的输入流
     * @return 支持自签名的客户端
     */
    public OkHttpClient getTrusClient(InputStream certificate) {
        X509TrustManager trustManager;
        SSLSocketFactory sslSocketFactory;
        try {
            trustManager = trustManagerForCertificates(certificate);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            // 使用构建出的trustManger初始化SSLContext对象
            sslContext.init(null, new TrustManager[] { trustManager }, null);
            // 获得sslSocketFactory对象
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        return new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, trustManager).build();
    }

    /**
     * 设置自签名证书
     *
     * @return SSLSocketFactory
     */
    public SSLSocketFactory setCertificates() {
        X509TrustManager trustManager;
        SSLSocketFactory sslSocketFactory;

        try {
            trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { trustManager }, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        return sslSocketFactory;
    }

    /**
     * 导入自签名证书
     * 
     * @return 导入自签名证书
     */
    private InputStream trustedCertificatesInputStream() {
        String comodoRsaCertificationAuthority = ""
                + "-----BEGIN CERTIFICATE-----\n"
                + "..." // 此处省略
                + "-----END CERTIFICATE-----\n";
        String entrustRootCertificateAuthority = ""
                + "-----BEGIN CERTIFICATE-----\n"
                + "..." // 此处省略
                + "-----END CERTIFICATE-----\n";
        return new Buffer().writeUtf8(comodoRsaCertificationAuthority)
                .writeUtf8(entrustRootCertificateAuthority).inputStream();
    }

    /**
     * 获去信任自签证书的trustManager
     *
     * @param in 自签证书输入流
     * @return 信任自签证书的trustManager
     * @throws GeneralSecurityException 异常
     */
    private X509TrustManager trustManagerForCertificates(InputStream in) throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        // 通过证书工厂得到自签证书对象集合
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }
        // 为证书设置一个keyStore
        char[] password = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        // 将证书放入keystore中
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }
        // Use it to build an X509 trust manager.
        // 使用包含自签证书信息的keyStore去构建一个X509TrustManager
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * 内部类，只有被调用到时才会装载，从而实现了延迟加载
     */
    private static class SingleHolder {

        private static OkHttpUtils instance = new OkHttpUtils();
    }

    public static OkHttpUtils instance() {
        return SingleHolder.instance;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 异步请求
     *
     * @param client {@link SimpleHttpClient}
     * @param callback {@link BaseCallback}
     */
    public void requestASync(SimpleHttpClient client, BaseCallback callback) {
        if (null == callback) {
            throw new NullPointerException("callback is null");
        }

        Request request = client.buildRequest();
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                sendFailureMessage(callback, call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    String result = body.string();
                    if (callback.mType == null || callback.mType == String.class) {
                        sendOnSuccessMessage(callback, result);
                    } else {
                        // JSON 格式，转换为对象
                        String typeName = ((Class) callback.mType).getName();
                        try {
                            Class<?> clazz = Class.forName(typeName);
                            sendOnSuccessMessage(callback, JsonHelper.fromJson(result, clazz));
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onFailure(call, e);
                        }
                    }
                    body.close();
                } else {
                    sendOnErrorMessage(callback, response.code());
                }
            }
        });
    }

    public void requestSync(SimpleHttpClient client, BaseCallback callback) {
        if (null == callback) {
            throw new NullPointerException("callback is null");
        }

        Request request = client.buildRequest();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                String result = body.string();
                if (callback.mType == null || callback.mType == String.class) {
                    sendOnSuccessMessage(callback, result);
                } else {
                    // 获取泛型的类型
                    String typeName = ((Class) callback.mType).getName();
                    try {
                        Class<?> clazz = Class.forName(typeName);
                        // JSON 格式，转换为对象
                        sendOnSuccessMessage(callback, JsonHelper.fromJson(result, clazz));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                body.close();
            } else {
                sendOnErrorMessage(callback, response.code());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadFile(SimpleHttpClient client, BaseCallback callback) {
        if (null == callback) {
            throw new NullPointerException("callback is null");
        }

        Request request = client.buildRequest();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                String result = body.string();
                if (callback.mType == null || callback.mType == String.class) {
                    sendOnSuccessMessage(callback, result);
                } else {
                    // 获取泛型的类型
                    String typeName = ((Class) callback.mType).getName();
                    try {
                        Class<?> clazz = Class.forName(typeName);
                        // JSON 格式，转换为对象
                        sendOnSuccessMessage(callback, JsonHelper.fromJson(result, clazz));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                body.close();
            } else {
                sendOnErrorMessage(callback, response.code());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object request2Object(SimpleHttpClient client) {
        Request request = client.buildRequest();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();

                MediaType mediaType = body.contentType();
                // image/jpeg
                String strMediaType = mediaType.toString();
                if (!strMediaType.contains("image")) {
                    String result = body.string();
                    LOG.info("mediaType not image/*," + strMediaType);
//                    LOG.info("response : ," + JsonHelper.toJSONString(JsonHelper.fromJson(result, Map.class)));
                    return result;
                }
                try {
                    InputStream inputStream = body.byteStream();
                    //File jpg = FileHelper.createTmpFile(inputStream, UUID.randomUUID().toString(), "jpg");
                    //Map<String, Object> retMap = new HashMap<>(2);
                    //retMap.put("is", inputStream);
                    //retMap.put("jpg", jpg);
                    return inputStream;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                body.close();
            } else {
                LOG.info("======error====="+response.code());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void requestSyncNotThrows(SimpleHttpClient client, BaseCallback callback) throws Exception {
        if (null == callback) {
            throw new NullPointerException("callback is null");
        }

        Request request = client.buildRequest();
        Response response = mOkHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            ResponseBody body = response.body();
            String result = body.string();
            if (callback.mType == null || callback.mType == String.class) {
                sendOnSuccessMessage(callback, result);
            } else {
                // 获取泛型的类型
                String typeName = ((Class) callback.mType).getName();
                try {
                    Class<?> clazz = Class.forName(typeName);
                    // JSON 格式，转换为对象
                    sendOnSuccessMessage(callback, JsonHelper.fromJson(result, clazz));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            body.close();
        } else {
            sendOnErrorMessage(callback, response.code());
        }
    }

    private void sendOnSuccessMessage(BaseCallback callback, Object result) {
        callback.onSuccess(result);
    }

    private void sendOnErrorMessage(BaseCallback callback, int code) {
        callback.onError(code);
    }

    private void sendFailureMessage(BaseCallback callback, Call call, IOException ex) {
        callback.onFailure(call, ex);
    }

}
