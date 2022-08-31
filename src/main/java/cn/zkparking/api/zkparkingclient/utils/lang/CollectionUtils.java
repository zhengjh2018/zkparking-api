package cn.zkparking.api.zkparkingclient.utils.lang;

import java.util.*;

/**
 * description: 集合框架的工具类
 *
 */
public class CollectionUtils {

    private CollectionUtils() {

    }

    /**
     * 判断集合是否为空
     * <pre class="code">CollectionUtils.isEmpty(list);</pre>
     *
     * @param collection 集合
     * @return 如果集合为{@code null}或为空是则返回{@code true}，否则返回{@code false}
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 判断map是否为空
     * <pre class="code">CollectionUtils.isEmpty(hashmap);</pre>
     *
     * @param map map集合
     * @return 如果map为{@code null}或为空是则返回{@code true}，否则返回{@code false}
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 判断集合是否为不为空
     * <pre class="code">CollectionUtils.isNotEmpty(list);</pre>
     *
     * @param collection 集合
     * @return 如果集合不为{@code null}且不为空是则返回{@code true}，否则返回{@code false}
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断map是否为不为空
     * <pre class="code">CollectionUtils.isNotEmpty(hashmap);</pre>
     *
     * @param map map集合
     * @return 如果map不为{@code null}且不为空是则返回{@code true}，否则返回{@code false}
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 将数组的内容转换为集合对象，注意该集合对象的可以修改的，
     * 注意区别JDK API{@linkplain Arrays#asList(Object[])}不同。
     * 如果数组为{@code null}或空，则会返回一个空的集合对象
     * <p>
     * 该方法相当于提供了一种创建集合对象并且集合里面已经包含了一定的数据。
     * </p>
     * <pre>
     *     List&lt;String&gt; stooges = CollectionUtils.asList("Larry", "Moe", "Curly");
     * </pre>
     *
     * @param values 数组
     * @param <T>    泛型类型
     * @return 集合对象
     */
    public static <T> List<T> asList(T... values) {
        if (ArrayUtils.isEmpty(values)) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>(values.length);
        Collections.addAll(list, values);
        return list;
    }

    /**
     * <p>
     * 将数组一一映射转换为Map对象，思路来自于Python中的字典，如果两个数组的长度不一致，则取较短的那个数组。
     * </p>
     * 所以一一映射，根据举例能够说明的更清楚。
     * <pre>
     *         keys =    [a,b,c]
     * 		   values = [1,2,3,4]
     * 		   则达到的Map值应该为{a=1,b=2,c=3}
     *     </pre>
     *
     * @param keys   键列表
     * @param values 值列表
     * @param <K>    泛型类型
     * @param <V>    泛型类型
     * @return 返回键值对应的Map对象
     */
    public static <K, V> Map<K, V> asMap(K[] keys, V[] values) {
        if (ArrayUtils.isEmpty(keys) || ArrayUtils.isEmpty(values)) {
            return Collections.emptyMap();
        }

        //两个数组只能取长度较短的那个至
        final int size = Math.min(keys.length, values.length);
        Map<K, V> map = new HashMap<>(size);

        for (int index = 0; index < size; index++) {
            map.put(keys[index], values[index]);
        }
        return map;
    }

    /**
     * <p>
     * 键值映射，将{@code keyStr}和{@code valueStr}按照{@code delimiter}分隔符分割，然后一一映射为Map对象。
     * </p>
     * 该方法在Web应用程序下比较适合使用，如果两个分割后的长度不一致，则取较短的部分。
     * 以下是示例说明：
     * <pre>
     *   keys =    a,b,c
     * 	 values = 1,2,3,4
     * 	 分割符合为逗号","
     * 	 则达到的Map值应该为{a=1,b=2,c=3}
     *  </pre>
     *
     * @param keyStr    键列表
     * @param valueStr  值列表
     * @param delimiter 分隔符
     * @return 返回键值对应的Map对象
     */
    public static Map<String, String> asMap(String keyStr, String valueStr, String delimiter) {
        if (StringUtils.isEmpty(keyStr) || StringUtils.isEmpty(valueStr)) {
            return Collections.emptyMap();
        }
        if (StringUtils.isEmpty(delimiter)) {
            throw new IllegalArgumentException("The delimiter is invalid!");
        }

        String[] keys = keyStr.split(delimiter);
        String[] values = valueStr.split(delimiter);

        return asMap(keys, values);
    }

    /**
     * <p>将url中的请求参数转为Map对象</p>
     *
     * @param urlParam url请求参数
     * @return 返回Map对象
     */
    public static Map<String, String> urlParam2Map(String urlParam) {
        if (StringUtils.isEmpty(urlParam)) {
            return Collections.emptyMap();
        }
        String[] arr = StringUtils.split(urlParam, "&");
        if (ArrayUtils.isEmpty(arr)) {
            return Collections.emptyMap();
        }

        Map<String, String> params = new HashMap<>(arr.length);
        for (String param : arr) {
            if (StringUtils.hasText(param)) {
                String[] kv = StringUtils.split(param, "=");
                if (StringUtils.hasLength(kv[0])) {
                    params.put(kv[0], kv[1]);
                }
            }
        }
        return params;
    }

    /**
     * 集合对象转为数组
     * <pre class="code">
     * CollectionUtils.toStringArray(Arrays.asList("index","boy","amazing")); //--- [boy,index,amazing]
     * </pre>
     *
     * @param collection 集合对象
     * @return 返回数组对象
     */
    public static String[] toStringArray(Collection<String> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return new String[0];
        }
        return collection.toArray(new String[collection.size()]);
    }

    /**
     * 将集合数据转为字符串，每个元素之间采用,拼接。
     *
     * @param collection 集合数据
     * @return 字符串
     */
    public static String toString(final Collection<String> collection) {
        return toString(collection, ",");
    }

    /**
     * 将集合数据转为字符串，每个元素之间采用{@code delimiter}拼接。
     *
     * @param collection 集合数据
     * @param delimiter  分隔符
     * @return 字符串
     */
    public static String toString(final Collection<String> collection, String delimiter) {
        if (isEmpty(collection)) return StringUtils.EMPTY_STRING;
        if (delimiter == null) delimiter = StringUtils.EMPTY_STRING;
        StringBuilder builder = new StringBuilder();
        int size = collection.size(), i = 0;
        for (String str : collection) {
            if (i == size - 1) builder.append(str);
            else builder.append(str).append(delimiter);
            i++;
        }
        return builder.toString();
    }

    /**
     * 将Map对象转为形如URL参数格式的字符串
     * <pre>
     *     Map&lt;String,String&gt; params = new HashMap&lt;&gt;();
     *     params.put("username","admin");
     *     params.put("password","123456");
     *     String str = CollectionUtils.toString(params);
     *     //则得到的字符串结果为 "username=admin&nbsp;password=123456";
     * </pre>
     *
     * @param data 数据
     * @return 类四URL参数格式的字符串
     * @see #toString(Map, String, String)
     */
    public static String toString(final Map<String, String> data) {
        return toString(data, "=", "&");
    }

    /**
     * <p>
     * 将Map对象转成字符串，其中Key与Value的连接使用{@code keyDelimiter}拼接,键值对之间采用采用{@code entryDelimiter}拼接
     * </p>
     * <pre>
     *     Map&lt;String,String&gt; params = new HashMap&lt;&gt;();
     *     params.put("username","admin");
     *     params.put("password","123456");
     *     String str = CollectionUtils.toString(params,"=","|");
     *     //则得到的字符串结果为 username=admin|password=123456
     * </pre>
     *
     * @param data           数据
     * @param keyDelimiter   Key和Value的拼接字符串，默认值为空
     * @param entryDelimiter 键值对的拼接字符串，默认值为空
     * @return 返回字符串
     */
    public static String toString(final Map<String, String> data, String keyDelimiter, String entryDelimiter) {
        return toString(data, keyDelimiter, entryDelimiter, false, true);
    }

    /**
     * <p>
     * 将Map对象转成字符串，其中Key与Value的连接使用{@code keyDelimiter}拼接,键值对之间采用采用{@code entryDelimiter}拼接.
     * </p>
     * <pre>
     *     Map&lt;String,String&gt; params = new HashMap&lt;&gt;();
     *     params.put("username","admin");
     *     params.put("password","123456");
     *     String str = CollectionUtils.toString(params,"=","|",true);
     *     //则得到的字符串结果为 password=123456|username=admin
     * </pre>
     *
     * @param data           Map数据
     * @param keyDelimiter   Key和Value的拼接字符串，默认值为空
     * @param entryDelimiter 键值对的拼接字符串，默认值为空
     * @param keySortable    如果值为{@code true}则按自然排序排序,否则不处理
     * @param ignoreEmpty    如果值为{@code true}则忽略空值
     * @return 返回字符串
     */
    private static String toString(final Map<String, String> data, String keyDelimiter, String entryDelimiter,
                                   boolean keySortable, boolean ignoreEmpty) {
        if (isEmpty(data)) return StringUtils.EMPTY_STRING;
        if (keyDelimiter == null) keyDelimiter = StringUtils.EMPTY_STRING;
        if (entryDelimiter == null) entryDelimiter = StringUtils.EMPTY_STRING;
        int size = data.size(), i = 0;
        Set<String> keys = data.keySet();
        if (keySortable) keys = new TreeSet<>(keys);
        StringBuilder builder = new StringBuilder();
        for (String key : keys) {
            if (ignoreEmpty && StringUtils.hasLength(data.get(key))) {
                if (i == size - 1) builder.append(key).append(keyDelimiter).append(data.get(key));
                else builder.append(key).append(keyDelimiter).append(data.get(key)).append(entryDelimiter);
            } else if (!ignoreEmpty) {
                if (i == size - 1) builder.append(key).append(keyDelimiter).append(data.get(key));
                else builder.append(key).append(keyDelimiter).append(data.get(key)).append(entryDelimiter);
            }
            i++;
        }
        return builder.toString();
    }

    /**
     * 将Map对象的数据转为通用签名字符串(目前通用签名规则为：将key和value用'='连接，每个键值对之间用'&nbsp;'连接，并且键列表按照自然排序)
     *
     * @param params Map对象数据
     * @return 返回字符串
     */
    public static String asCommonSignString(final Map<String, String> params) {
        return toString(params, "=", "&", true, true);
    }

    /**
     * protobuf 序列化集合，会生成 java.util.Collections$UnmodifiableRandomAccessList 一个对象，因没有实现 AbstractList 类，固没有set/add/remove方法<br/>
     *  Collections$UnmodifiableRandomAccessList 是 Collections 的静态内部成员类，继承 UnmodifiableList<E> 实现 RandomAccess 接口<br/>
     *  诸如 Arrays.asList 方法生成的 ArrayList 也只是 Arrays 的内部类，也是不支持set/add/remove方法
     *  需要把这个对象转成 ArrayList 或 LinkedList，否则会报 java.lang.UnsupportedOperationException
     * @param collection    集合
     * @param <E>   对象
     * @return  E
     */
    public static <E> List<E> collectionToList(Collection<E> collection) {
        List<E> list;
        if (collection instanceof ArrayList || collection instanceof LinkedList) {
            list = (List<E>) collection;
        } else {
            list = new ArrayList<>(collection);
        }
        return list;
    }
}
