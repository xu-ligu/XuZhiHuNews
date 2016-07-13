# XuZhiHuNews
学习用途（模仿知乎日报）



##完成功能
* 获取开始动画图片，播放动画进入主界面。
* 展示热文信息及图片，加载横幅图片滚动播放，热闻列表，热闻分类侧边栏列表。
* 点击文章信息展示
* 下拉刷新、列表底部自动更新
* 自动缓存
* 标记已读文章标题
* 日间模式夜间模式切换


  
##使用到的jar包
* com.google.code.gson:gson:2.7
* com.loopj.android:android-async-http:1.4.9
* com.nostra13.universalimageloader:universal-image-loader:1.9.5
* com.android.support:design:22.2.1
* com.android.support:appcompat-v7:22.2.1
##技术描述：
* DrawerLayout嵌套SwipeRefreshLayout组件完成下拉刷新，嵌套FrameLayout后续装载content内容完成整体框架。
* 图片采用第三方框架ImageLoader异步加载加快加载速度，解决OOM问题；
* Banner使用自定义控件实现，内部用到了ViePager嵌套ImageView和TextView，并实现自动滚动滚动，无限滑动。热闻列表使用ListView嵌套横向RlataiveLayout实现。通过对listview的onScroll重写添加控制条件，完成下拉刷新冲突，和底部加载更多数据的操作。
* 使用android-async-http框架进行网络请求，它底层使用线程池处理并发，效率很高，特别适合请求频繁的网络操作；使用GSONFormat和GSON完成繁琐的解析代码的工作，提高开发效率。
* 使用SharedPreferences保存已查看条目ID，保存上次打开主题模式。使用SQLiteDatabase保存已加载的返回数据，离线状况下加载Database保存数据。
* 使用自定义RevealBackground视图，实现点击处全屏扩散效果，原理是通过overridePendingTransition（0,0）将爱窗体比特间的跳转取消，再点击时在原有activity界面上播放RevealBackgroundView的全屏扩散动画，播放完后新的界面就显示出来了。
* 使用CoordinatorLayout嵌套CollapsingToolbarLayout实现顶部展示title图片，向下阅读折叠的效果。
  

##数据接口来源
  [知乎日报API](https://github.com/izzyleung/ZhihuDailyPurify/wiki/%E7%9F%A5%E4%B9%8E%E6%97%A5%E6%8A%A5-API-%E5%88%86%E6%9E%90)
  非常感谢的！
  
##APK下载地址
* 百度云盘下载
  [XuZhiHuNews](http://pan.baidu.com/s/1hr7ZUiK)
  
##效果图
###今日热闻信息推送、横幅图片热闻滚动视图、已查看标记
![image](https://github.com/xu-ligu/XuZhiHuNews/raw/master/Banner.png)

  
###侧滑边栏效果
![image](https://github.com/xu-ligu/XuZhiHuNews/raw/master/menu.png)
  
###文章内容展示
![image](https://github.com/xu-ligu/XuZhiHuNews/raw/master/late.png)
  
###夜间模式效果图
![image](https://github.com/xu-ligu/XuZhiHuNews/raw/master/dark.png)
##开发工具
 * androidstudio1.5
 * gradle version 2.8
 * jdk version 1.8

