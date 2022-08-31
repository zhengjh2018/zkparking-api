package cn.zkparking.api.zkparkingclient.utils.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

public class MD5Util {


    private static final Logger LOG = LoggerFactory.getLogger(MD5Util.class);

    /**
     * md5签名
     *
     * 按参数名称升序，将参数值进行连接 签名
     * @param appSecret
     * @param params
     * @return
     */
    public static String sign(String appSecret,  Map<String, Object> params) {
        List<String> list = params.entrySet().stream().filter(entry -> entry.getValue() != "")
                .map(entry -> entry.getKey() + "=" + entry.getValue()+ "&").collect(Collectors.toCollection(ArrayList::new));
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + appSecret;
        LOG.info("Sign Before MD5: {}", result);
        result = MD5Helper.MD5Encode(result).toUpperCase();
        LOG.info("Sign Result: {}", result);
        return result;
    }


    /**
     * 请求参数签名验证
     *
     * @param appSecret
     * @param map
     * @return true 验证通过 false 验证失败
     * @throws Exception
     */
    public static boolean verifySign(String appSecret, Map map) throws Exception {

        Map<String, Object> param=new HashMap<>();

        Set<String> keys = map.keySet();
        for(String key:keys) {
            if (!key.equals("signature")) {
                param.put(key, URLDecoder.decode(String.valueOf(map.get(key)), "UTF-8"));
            }
        }

        String signStr =  String.valueOf(map.get("signature"));

        if (!sign(appSecret, param).equals(signStr)) {
            return false;
        }
        return true;
    }


    public static void main(String[] args) {
//        Map<String, Object> res = new HashMap<>();
//        res.put("appKey", "2");
//        res.put("format", "json");
//        res.put("version", "1.0");
//        res.put("nonceStr", "212");
//        res.put("parkId", 10001);
//        res.put("orderNo", "123445666");
//        res.put("plateNo", "闽D12345");
//        res.put("startTime", "2020-06-04 11:11:11");
//        res.put("endTime", "2020-06-04 11:11:11");
//        res.put("freeType", 0);
//        res.put("freeMoney", 1);
//        res.put("freeTime", 1);
//        res.put("timestamp", "1591322290");
//        String ret = sign("2", res);
      //  LOG.info(ret);
    }
}
