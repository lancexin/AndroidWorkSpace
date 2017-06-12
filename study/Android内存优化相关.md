# 内存优化

### 减少小对象的内存使用

* 使用更加轻量的数据结构,例如:我们可以考虑使用ArrayMap/SparseArray而不是HashMap等传统数据结构

* 避免在Android里面使用Enum(枚举),Enums的内存消耗通常是static constants的2倍

* 减小Bitmap对象的内存占用,使用更小的图片

### 内存对象的重复利用

* 复用系统自带的资源

* 注意在ListView/GridView等出现大量重复子组件的视图里面对ConvertView的复用

* Bitmap对象的复用

* 避免在onDraw方法里面执行对象的创建

* 某些地方用StringBuilder代替String的使用

### 避免对象的内存泄露
* 注意Activity的泄漏:Handler或者Thread异步回调等很容易出现

* 考虑使用Application Context而不是Activity Context

* 注意临时Bitmap对象的及时回收

* 注意监听器的注销

* 注意缓存容器中的对象泄漏,需要考虑UI不再显示时进行缓存的清理等.

* 注意WebView的泄漏

* 注意Cursor对象是否及时关闭

### 内存使用策略优化
* 谨慎使用large heap

* 综合考虑设备内存阈值与其他因素设计合适的缓存大小(一般需要结合手机内存,机型,屏幕分辨率,getMemoryClass等进行动态设计)
* onLowMemory()与onTrimMemory()的意义和用法,
    > onTrimMemory TRIM_MEMORY_UI_HIDDEN 当UI都不显示的时候会回调,表示所有UI都不显示,这个时候可能需要释放一些内存或者缓冲池.
    
    > onTrimMemory TRIM_MEMORY_RUNNING_MODERATE    表示应用程序正常运行，并且不会被杀掉。但是目前手机的内存已经有点低了，系统可能会开始根据LRU缓存规则来去杀死进程了。
    
    > onTrimMemory TRIM_MEMORY_RUNNING_LOW    表示应用程序正常运行，并且不会被杀掉。但是目前手机的内存已经非常低了，我们应该去释放掉一些不必要的资源以提升系统的性能，同时这也会直接影响到我们应用程序的性能。
    
    > onTrimMemory TRIM_MEMORY_RUNNING_CRITICAL    表示应用程序仍然正常运行，但是系统已经根据LRU缓存规则杀掉了大部分缓存的进程了。这个时候我们应当尽可能地去释放任何不必要的资源，不然的话系统可能会继续杀掉所有缓存中的进程，并且开始杀掉一些本来应当保持运行的进程，比如说后台运行的服务。
    
    > onTrimMemory TRIM_MEMORY_BACKGROUND    表示手机目前内存已经很低了，系统准备开始根据LRU缓存来清理进程。这个时候我们的程序在LRU缓存列表的最近位置，是不太可能被清理掉的，但这时去释放掉一些比较容易恢复的资源能够让手机的内存变得比较充足，从而让我们的程序更长时间地保留在缓存当中，这样当用户返回我们的程序时会感觉非常顺畅，而不是经历了一次重新启动的过程。
    
    > onTrimMemory TRIM_MEMORY_MODERATE    表示手机目前内存已经很低了，并且我们的程序处于LRU缓存列表的中间位置，如果手机内存还得不到进一步释放的话，那么我们的程序就有被系统杀掉的风险了。
    
    > onTrimMemory TRIM_MEMORY_COMPLETE    表示手机目前内存已经很低了，并且我们的程序处于LRU缓存列表的最边缘位置，系统会最优先考虑杀掉我们的应用程序，在这个时候应当尽可能地把一切可以释放的东西都进行释放。

    > onLowMemory 在系统内存不足，所有后台程序（优先级为background的进程，不是指后台运行的进程）都被杀死时，系统会调用onLowMemory
* 资源文件需要选择合适的文件夹进行存放
* Try catch某些大内存分配的操作
* 谨慎使用static对象
* 特别留意单例对象中不合理的持有
* 珍惜Services资源,当service不用时需要关闭,根据需求定义是否需要用service
* 优化布局层次，减少内存消耗
* 谨慎使用“抽象”编程
* 使用nano protobufs序列化数据
* 谨慎使用依赖注入框架
* 谨慎使用多进程
* 使用ProGuard来剔除不需要的代码
* 谨慎使用第三方libraries
* 考虑不同的实现方式来优化内存占用
* 对最终的APK使用zipalign