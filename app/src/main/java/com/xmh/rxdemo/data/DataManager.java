package com.xmh.rxdemo.data;

import com.xmh.rxdemo.bean.Car;
import com.xmh.rxdemo.bean.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengh on 2016/6/22 022.
 */
public class DataManager {

    private static DataManager manager;

    private DataManager(){}

    public static DataManager getInstance(){
        if(manager==null){
            manager=new DataManager();
        }
        return manager;
    }

    public List<String> getData(){
        List<String> data=new ArrayList<>();
        data.add("aaa");
        data.add("123");
        data.add("affadsf");
        data.add("daggaff");
        data.add("jhuiho");
        data.add("hdusaio");
        data.add("fhduewaio");
        return data;
    }

    public List<Car> getCar(){
        List<Car> data=new ArrayList<>();
        data.add(new Car(2,"zixingche"));
        data.add(new Car(2,"diandongche"));
        data.add(new Car(3,"sanlunche"));
        data.add(new Car(3,"diansanlun"));
        data.add(new Car(4,"tesila"));
        data.add(new Car(4,"jiaoche"));
        data.add(new Car(4,"suv"));
        return data;
    }

    public List<Person> getPserson(){
        List<Person> data=new ArrayList<>();
        data.add(new Person("zhangsan",20));
        data.add(new Person("lisi",25));
        data.add(new Person("wangwu",30));
        data.add(new Person("zhaoliu",32));
        data.add(new Person("songqi",45));
        data.add(new Person("zhuba",92));
        data.add(new Person("liujiu",21));
        return data;
    }
}
