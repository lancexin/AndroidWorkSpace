# keyedtaskprocessor

一个简单易用的任务调度模块,根据key值在不同的线程中按照顺序执行Runnable.

### 一个形象的比喻

我们将keyedtaskprocessor比喻成公路上的收费站,收费站分为大车道,中车道,小车道,不同的车辆经过收费站时根据车的类型进入不同的车道排队收费.

这里的车道则为key,车辆则为Runnable



### 对比HandlerThread

1.不支持dealy和postAtTime
2.支持多通道,HandlerThread是单通道
3.都能在一定程度上解决线程安全问题.


