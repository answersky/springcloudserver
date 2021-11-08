package com.answer.utlis;

import com.answer.model.Student;
import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * created by liufeng
 * 2021/10/12
 */
public class UtilTest {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        List<Map<String,String>> maps=new ArrayList<>();
        Map<String,String> map=new LinkedHashMap<>();
        map.put("id","1");
        map.put("age","12");
        map.put("name","xxx1");
        Map<String,String> map1=new LinkedHashMap<>();
        map1.put("id","2");
        map1.put("age","12");
        map1.put("name","xxx1");
        maps.add(map);
        maps.add(map1);
        Map<String, List<Object>> result=ListConvertMap.listToMap(maps,"id");
        System.out.println(result);


        Student s1=new Student(1,"dddd",23);
        Student s2=new Student(2,"xxx",23);
        Student s3=new Student(3,"dddddd",23);
        Student s4=new Student(2,"xxx",23);
        Student s5=new Student(3,"gggg",23);
        List<Student> students= Lists.newArrayList(s1,s2,s3,s4,s5);
        Map<String, List<Object>> resultMap=ListConvertMap.listToMap(students,"id");
        System.out.println(resultMap);
        for(Map.Entry<String,List<Object>> entry:resultMap.entrySet()){
            List<Object> list=entry.getValue();
            for(Object object:list){
                Student student= (Student) object;
                System.out.println(student.getName());
            }
        }
    }
}
