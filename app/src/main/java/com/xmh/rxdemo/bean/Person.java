package com.xmh.rxdemo.bean;

import com.xmh.rxdemo.data.DataManager;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mengh on 2016/6/22 022.
 */
public class Person implements Serializable{
    private String name;
    private int age;
    private List cars;;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        this.cars= DataManager.getInstance().getCar();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List getCars() {
        return cars;
    }

    public void setCars(List cars) {
        this.cars = cars;
    }
}
