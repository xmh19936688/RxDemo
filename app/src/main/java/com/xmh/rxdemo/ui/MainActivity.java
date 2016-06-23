package com.xmh.rxdemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xmh.rxdemo.R;
import com.xmh.rxdemo.data.DataManager;
import com.xmh.rxdemo.demo.Demo;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> data = DataManager.getInstance().getData();

        Demo.simple(data);

        Demo.demoOfCreateObservable();

        Demo.demoOfCreateObserver();

        Demo.demoOfScheduler();

        Demo.demoOfMap();

        Demo.demoOfFlatMap();

        Demo.demoOfAction();

    }
}
