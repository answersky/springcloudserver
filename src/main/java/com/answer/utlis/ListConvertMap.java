package com.answer.utlis;

import com.sun.istack.internal.NotNull;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * created by liufeng
 * 2021/10/12
 * 实现list转Map
 * 将list中对象的某些属性转成key，然后根据key归类存放在map
 */
public class ListConvertMap {

    public static Map<String,List<Object>> listToMap(@NotNull List<?> list, @NotNull String fieldName) throws IllegalAccessException, NoSuchFieldException {
        Map<String,List<Object>> result=new LinkedHashMap<>();
        for(Object t:list){
            Object fieldValue=null;
            if(t instanceof Map){
                fieldValue=((Map) t).get(fieldName);
            }else {
                Class clazz=t.getClass();
                Field field=clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                fieldValue=field.get(t);
            }

            if(fieldValue!=null){
                List ts=result.get(String.valueOf(fieldValue.toString()));
                if(CollectionUtils.isEmpty(ts)){
                    ts=new ArrayList<>();
                }
                ts.add(t);
                result.put(String.valueOf(fieldValue.toString()),ts);
            }

        }
        return result;
    }
}
