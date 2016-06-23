package com.xmh.rxdemo.demo;

import com.xmh.rxdemo.bean.Car;
import com.xmh.rxdemo.bean.Person;
import com.xmh.rxdemo.data.DataManager;
import com.xmh.rxdemo.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by mengh on 2016/6/22 022.
 */
public class Demo {

    /**遍历并输出*/
    public static void simple(List<String> data){
        Observable.from(data)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.e("string","completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("string","error"+e);
                    }

                    @Override
                    public void onNext(String s) {
                        LogUtil.e("read",s);
                    }
                });
    }

    public static void simpleJust(List<String> data){
        Observable.just(data)
                .subscribe(new Subscriber<List<String>>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void setProducer(Producer p) {
                        super.setProducer(p);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> strings) {

                    }
                });
    }

    /**创建被观察者*/
    public static void demoOfCreateObservable() {

        //手动发事件
        Observable.create(new Observable.OnSubscribe<Person>(){

            @Override
            public void call(Subscriber<? super Person> subscriber) {
                subscriber.onNext(new Person("a",1));
                subscriber.onNext(new Person("a",1));
                subscriber.onNext(new Person("a",1));
                subscriber.onCompleted();
            }
        });

        //数量有限的几个数据
        Observable.just("a","aa","aaa");

        //从List或Array发事件
        ArrayList<String> strings = new ArrayList<>();
        Observable.from(strings);

    }

    /**创建观察者*/
    public static void demoOfCreateObserver() {
        //Observer
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                //事件处理完成
            }
            @Override
            public void onError(Throwable e) {
                //事件处理出错
            }
            @Override
            public void onNext(String s) {
                //有新的事件
            }
        };

        //Subscriber
        Subscriber<String> subscriber = new Subscriber<String>() {

            @Override
            public void onStart() {
                super.onStart();
                //事件开始前
            }

            @Override
            public void setProducer(Producer p) {
                super.setProducer(p);
            }

            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(String s) {}
        };
        //可以取消订阅
        subscriber.unsubscribe();
    }

    /**线程控制*/
    public static void demoOfScheduler() {
        //创建观察者
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                LogUtil.e("next-thread",Thread.currentThread().getId()+"<->"+Thread.currentThread().getName());
            }
        };

        //创建被观察对象
        Observable.OnSubscribe<String> observable = new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                LogUtil.e("send-thread",Thread.currentThread().getId()+"<->"+Thread.currentThread().getName());
                subscriber.onNext("gfsdh");
                subscriber.onNext("dfshs");
                subscriber.onNext("fdsa");
                subscriber.onCompleted();
            }
        };

        //开始搞起
        Observable.create(observable)
                .subscribeOn(Schedulers.io())//指定事件发生线程
                .observeOn(Schedulers.newThread())//指定下一个事件处理线程
                .map(new Func1<String, Car>() {
                    private int count=0;
                    @Override
                    public Car call(String s) {
                        LogUtil.e("to-car-thread",Thread.currentThread().getId()+"<->"+Thread.currentThread().getName());
                        return new Car(count++,s);
                    }
                })//将事件内容转型
                .observeOn(Schedulers.newThread())//指定一下一个事件处理线程
                .map(new Func1<Car, Person>() {
                    private int count=0;
                    @Override
                    public Person call(Car car) {
                        LogUtil.e("to-person-thread",Thread.currentThread().getId()+"<->"+Thread.currentThread().getName());
                        return new Person(car.getStyle(),count++);
                    }
                })//将事件内容转型
                .observeOn(Schedulers.computation())//指定一下一个事件处理线程
                .map(new Func1<Person, String>() {
                    @Override
                    public String call(Person person) {
                        LogUtil.e("to-string-thread",Thread.currentThread().getId()+"<->"+Thread.currentThread().getName());
                        return person.getName();
                    }
                })//将事件内容转型
                .observeOn(AndroidSchedulers.mainThread())//指定一下一个事件处理线程
                .subscribe(subscriber);//将事件发给订阅者
    }

    public static void demoOfMap(){
        Observable.from(DataManager.getInstance().getData())
                .map(new Func1<String, Car>() {
                    private int count=0;
                    @Override
                    public Car call(String s) {
                        return new Car(count++,s);
                    }
                })//将事件内容转型
                .subscribe(new Action1<Car>() {
                    @Override
                    public void call(Car car) {
                        LogUtil.e("next",car.getStyle());

                    }
                });//将事件发给订阅者
    }

    public static void demoOfFlatMap() {
        //一个人有多辆车，输出每个人的每个车
        Observable.from(DataManager.getInstance().getPserson())
                .flatMap(new Func1<Person, Observable<Car>>() {
                    @Override
                    public Observable<Car> call(Person person) {
                        return Observable.from(person.getCars());
                    }
                })
                .subscribe(new Action1<Car>() {
                    @Override
                    public void call(Car car) {
                        LogUtil.e("car",car.getStyle());
                    }
                });
    }

    public static void demoOfAction() {
        Observable.from(DataManager.getInstance().getData())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        LogUtil.e("next",s);

                    }
                });//将事件发给订阅者
    }

}
