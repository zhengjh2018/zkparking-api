package cn.zkparking.api.zkparkingclient.utils.lang;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * description:MD5加密工具类  —— 签名处理核心文件
 *
 */
public class MD5Helper {

    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(MD5Helper.class);

    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param key           密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
        text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }

    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param sign          签名结果
     * @param key           密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String input_charset) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
        if (mysign.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 数据的字节数组
     *
     * @param content
     * @param charset
     * @return
     * @throws Exception
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (Exception e) {
            LOG.error("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
            throw new RuntimeException(e);
        }
    }

    /**
     * MD5 Encode
     *
     * @param original  原始数据
     * @return  结果
     */
    public static String MD5Encode(String original) {

        if (StringUtils.isEmpty(original)) {
            return "";
        }
        return DigestUtils.md5Hex(original.getBytes());
    }

    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
    public static String MD5Sign(SortedMap<String, String> packageParams, String key) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        LOG.info("md5:" + sb.toString());
        String sign = MD5Encode(sb.toString()).toUpperCase();
        LOG.info("packge签名:" + sign);
        return sign;

    }

}
