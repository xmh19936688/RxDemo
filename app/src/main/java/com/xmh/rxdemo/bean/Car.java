package com.xmh.rxdemo.bean;

import java.io.Serializable;

/**
 * Created by mengh on 2016/6/22 022.
 */
public class Car implements Serializable{
    private int wheelCount=4;
    private String style;

    public Car(int wheelCount, String style) {
        this.wheelCount = wheelCount;
        this.style = style;
    }

    public int getWheelCount() {
        return wheelCount;
    }

    public void setWheelCount(int wheelCount) {
        this.wheelCount = wheelCount;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
