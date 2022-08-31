package cn.zkparking.api.zkparkingclient.utils.json;

import cn.zkparking.api.zkparkingclient.utils.lang.ArrayUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * description: JSON 转换工具类
 *
 */
public class JsonHelper {
    private static final Logger LOG = LoggerFactory.getLogger(JsonHelper.class);

    private JsonHelper(){
        throw new UnsupportedOperationException();
    }

    // ---------------------------------------------------------------------
    // convert Java instance to JSON String
    // ---------------------------------------------------------------------

    /**
     * 将Java对象转换JSON字符串.
     * <p>
     * 中文字符换转成unicode码;如果不希望转换unicode码,则使用{@link #toJson(Object, boolean, String...)}
     * </p>
     * 
     * <pre>
     * User user = new User("admin", "123456");
     * JsonUtil.toJson(user);
     * </pre>
     *
     * @param value Java对象
     * @param <T> 泛型类型
     * @return json字符串
     * @see #toJson(Object, boolean, String...)
     */
    public static <T> String toJson(T value) {
        return toJson(value, true);
    }

    /**
     * 将Java对象转换JSON字符串。请注意这里的属性过滤会过滤掉所有类中的属性名。
     * 也就是说假设类User中有name和group，而类Group也有name属性。那么如果过滤属性name，那么User和Group中的name都会过滤掉。
     * 如果想要针对某个类的属性进行过滤请使用{@link #toJson(Object, Map)}
     * <p>
     * 中文字符换转成unicode码;如果不希望转换unicode码,则使用{@link #toJson(Object, boolean, String...)}
     * </p>
     * 
     * <pre>
     * User user = new User("admin", "123456");
     * JsonUtil.toJson(user, "username");// --- {"password":"123456"}
     * </pre>
     *
     * @param value Java对象
     * @param <T> 泛型类型
     * @param propertyNames 需要过滤的属性列表
     * @return json字符串
     * @see #toJson(Object, boolean, String...)
     */
    public static <T> String toJson(T value, final String... propertyNames) {
        return toJson(value, true, propertyNames);
    }

    /**
     * 将Java对象转换JSON字符串。
     *
     * @param value 待处理的对象
     * @param toUnicode 如果值为{@code true}则自动转成unicode码，否则不处理
     * @param propertyNames 过滤的属性名，可为空
     * @param <T> 泛型类型
     * @return json字符串
     */
    public static <T> String toJson(T value, boolean toUnicode, final String... propertyNames) {
        if (value == null) return null;
        List<SerializerFeature> serializerFeatureList = new ArrayList<>();
        serializerFeatureList.add(SerializerFeature.DisableCircularReferenceDetect);
        if (toUnicode) serializerFeatureList.add(SerializerFeature.BrowserCompatible);
        SerializerFeature[] serializerFeatures = new SerializerFeature[serializerFeatureList.size()];
        serializerFeatureList.toArray(serializerFeatures);
        if (ArrayUtils.isEmpty(propertyNames)) return JSON.toJSONString(value, serializerFeatures);
        return JSON.toJSONString(value, new PropertyFilter() {

            @Override
            public boolean apply(Object object, String name, Object value) {
                return !ArrayUtils.containsElement(propertyNames, name);
            }
        }, serializerFeatures);
    }

    /**
     * 将Java对象转换为JSON字符串，会过滤掉指定类中的属性，不会污染到其它类中的同名属性。
     * 
     * <pre>
     * Map&lt;Class&lt;?&gt;, List&lt;String&gt;&gt; map = new HashMap&lt;Class&lt;?&gt;, List&lt;String&gt;&gt;(2);
     * map.put(Person.class, Arrays.asList("username"));
     * map.put(Group.class, Arrays.asList("id"));
     * LOG.info(toJson(list, map));
     * </pre>
     *
     * @param value Java对象
     * @param classOfProps 需要过滤的属性列表
     * @return json字符串
     */
    public static String toJson(Object value, final Map<Class<?>, List<String>> classOfProps) {
        return JSON.toJSONString(value, new PropertyFilter() {

            @Override
            public boolean apply(Object object, String name, Object value) {
                List<String> props = classOfProps.get(object.getClass());
                return !props.contains(name);
            }
        }, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.BrowserCompatible);
    }

    /**
     * 将Java对象转换为JSON字符串，该方法提供了开放的转换规则，比如出现了死循环而导致了结果不正确。<br>
     * {@link SerializerFeature}对象枚举
     * <ul>
     * <li>{@link SerializerFeature#DisableCircularReferenceDetect} 禁用循环引用</li>
     * <li>{@link SerializerFeature#WriteClassName} json序列化时将类信息保存以便反序列化时可以自动获取类信息</li>
     * <li>{@link SerializerFeature#BeanToArray} 将Java对象转换为数组，这可能已经不是json序列化的范围之内</li>
     * <li>{@link SerializerFeature#PrettyFormat} 用于显示格式化过的json字符串，一般用于输出控制台效果较好。</li>
     * <li>{@link SerializerFeature#UseSingleQuotes} 使用单引号，默认使用双引号</li>
     * <li>{@link SerializerFeature#WriteDateUseDateFormat} 日期格式化输出，日期格式为：yyyy-MM-dd
     * HH:mm:ss</li>
     * <li>{@link SerializerFeature#BrowserCompatible} 解决中文乱码，会转成Unicode码</li>
     * </ul>
     * 该对象枚举值非常多，这里仅仅提供一些常用的枚举值作为介绍。
     *
     * @param value Java对象
     * @param features 序列化规则
     * @return json字符串
     */
    public static String toJson(Object value, SerializerFeature... features) {
        return JSON.toJSONString(value, features);
    }

    // ---------------------------------------------------------------------
    // convert JSON String to Java instance
    // ---------------------------------------------------------------------

    /**
     * 将json字符串转为Java对象
     *
     * @param json json字符串
     * @param clazz 转换的类型
     * @param <T> 泛型类型
     * @return Java对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * 将json字符串转为数组
     *
     * @param json json字符串
     * @param clazz 转换的类型
     * @param <T> 泛型类型
     * @return Java对象
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    /**
     * 暴力解析:Alibaba fastjson
     * @param json
     * @return
     */
    public final static Map isJSONValid(String json) {

        try {
            JSONObject.parseObject(json);
        } catch (JSONException ex) {

            return null;
        } catch (ClassCastException cx) {
            return null;
        }
        return JsonHelper.fromJson(json,Map.class);
    }

    /**
     *  将Java对象转为json字符串
     *
     * @param object Java对象
     * @return json字符串
     */
    public static String toJSONString(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     * 向控制台打印格式化过的json字符串
     *
     * @param value Java对象
     */
    public static void println(Object value) {

        LOG.info(toJson(value, SerializerFeature.PrettyFormat, SerializerFeature.BrowserCompatible));
    }




}
