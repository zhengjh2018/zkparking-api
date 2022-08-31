package cn.zkparking.api.zkparkingclient.utils.request.okhttp;

import com.google.gson.internal.$Gson$Types;
import okhttp3.Call;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * description: 基础的http请求回调抽象类
 *
 */
public abstract class BaseCallback<T> {

    public Type mType;

    /**
     * Returns the type from super class's type parameter in {@link $Gson$Types#canonicalize}.
     *
     * @param subclass Class
     * @return super class's type parameter
     */
    private static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }

        ParameterizedType parameterizedType = (ParameterizedType) superclass;
        Type type = parameterizedType.getActualTypeArguments()[0];
        return $Gson$Types.canonicalize(type);
    }

    public BaseCallback() {
        mType = getSuperclassTypeParameter(this.getClass());
    }


    public void onSuccess(T t){

    }


    public void onError(int code) {

    }

    public void onFailure(Call call, Exception ex) {

    }
}
