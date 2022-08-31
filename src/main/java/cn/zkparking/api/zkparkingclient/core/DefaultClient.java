package cn.zkparking.api.zkparkingclient.core;

/**
 * Description: sdk
 *
 */
public class DefaultClient extends AbstractZkClient {

    public DefaultClient(String serverUrl, String appId, String appSecret) {
        super(serverUrl, appId, appSecret);
    }


    public static Builder builder(String serverUrl, String appId, String appSecret) {
        return new Builder(serverUrl, appId, appSecret);
    }

    public static class Builder {
        private DefaultClient client;

        Builder(String serverUrl, String appId,String appSecret) {
            client = new DefaultClient(serverUrl, appId, appSecret);
        }

        public DefaultClient build() {
            return client;
        }
    }
}
