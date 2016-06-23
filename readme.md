###观察者
1. Observer
 - onNext()
 - onCompleted();
 - onError();
1. Subscriber
 - 继承Observer
 - onStart();在开始发送事件前
 - subscriber.unsubscribe();取消订阅

###被观察对象
1. Observable.create(Observable.OnSubscribe());
 ```
 Observable.create(new Observable.OnSubscribe<String>(){
 
     @Override
     public void call(Subscriber<? super String> subscriber) {
         subscriber.onNext("a");
         subscriber.onNext("aa");
         subscriber.onNext("aaa");
         subscriber.onCompleted();
     }
 });
 ```
1. Observable.just();
 ```
 Observable.just("a","aa","aaa");
 ```
1. Observable.from();
 ```
 ArrayList<String> strings = new ArrayList<>();
 Observable.from(strings);
 ```

###方法封装
1. FuncX
封装带有x个参数的带返回值方法。

1. ActionX
封装带有x个参数的无返回值方法。
	```
	public static void demoOfAction() {
		Observable.from(DataManager.getInstance().getData())
				.subscribe(new Action1<String>() {
					@Override
					public void call(String s) {
						LogUtil.e("next",s);
	
					}
				});//将事件发给订阅者
	}
	```

###类型转换
1. map()一对一转换
	```
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
	```
1. flatMap()一对多转换
	```
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
	```

###线程控制
1. subscribeOn()指定被观察对象线程
1. observeOn()指定观察者线程
1. Schedulers.immediate();当前线程
1. Schedulers.newThread();新开一个线程
1. Schedulers.io();与newThread()差不多，但io()中内置无数量上限的线程池。注意不要把计算操作放在这里，避免创建多余的线程。
1. Schedulers.computation();与io()的区别在于其线程池大小固定，大小为CPU核心数。注意不要把I/O操作放在这里，避免浪费CPU。
1. AndroidSchedulers.mainThread();UI线程。
```
public static void demoOfScheduler() {
    //创建观察者
    Subscriber<String> subscriber = new Subscriber<String>() {
        @Override
        public void onCompleted() {}

        @Override
        public void onError(Throwable e) {}

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
```
```
E/send-thread: 6412<->RxCachedThreadScheduler-1
E/to-car-thread: 6410<->RxNewThreadScheduler-2
E/to-car-thread: 6410<->RxNewThreadScheduler-2
E/to-person-thread: 6409<->RxNewThreadScheduler-1
E/to-car-thread: 6410<->RxNewThreadScheduler-2
E/to-person-thread: 6409<->RxNewThreadScheduler-1
E/to-person-thread: 6409<->RxNewThreadScheduler-1
E/to-string-thread: 6408<->RxComputationThreadPool-3
E/to-string-thread: 6408<->RxComputationThreadPool-3
E/to-string-thread: 6408<->RxComputationThreadPool-3
E/next-thread: 1<->main
E/next-thread: 1<->main
E/next-thread: 1<->main
```
