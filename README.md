### Hybrid开发(WebView + Native)




#### WebView调用Native的三种方式：

##### 1、通过WebView的addJavascriptInterface进行对象映射。


##### 2、通过WebViewClient的shouldOverrideUrlLoading方法回调拦截url：一般根据scheme（协议格式）、authority（协议名）来判定。


##### 3、通过WebChromeClient的 onJSAlert() onJsPrompt() onJsConfirm() 回调拦截Js对话框alert() confirm() prompt()。




#### Native调用WebView的两种方案：

##### 1、loadUrl("javascript:callJS()")：方法简洁、效率低。当不需要返回值且对性能要求较低时可以考虑使用。

##### 2、evaluateJavascript(script,resultCallback)：4.4以上可以使用，效率高有返回值。




#### 新增JsBridgeActivity + jsbridge.html，用于实现Native + h5 之间的调用协议的实践。


==================================================================================



  
#### WebView的缓存机制：

##### Android WebView的主要性能问题：
##### 1、加载速度慢
##### 2、消耗流量


##### 1、H5页面加载速度慢原因：
##### （1）渲染速度慢
##### （2）页面资源加载缓慢。


##### 2、消耗流量原因：
##### （1）每次使用过H5页面，都需要重新加载该H5页面
##### （2）每加载一个H5页面，就会有较多的网络请求
##### （3）每个请求都是串行的


##### 针对以上Android WebView的性能问题，有如下三种解决方案：
##### 1、H5的缓存机制（WebView自带）
##### 2、资源预加载
##### 3、资源拦截


  
##### 1.1、Android自带的缓存机制有5种： 
##### （1）浏览器缓存机制
##### （2）Application Cache缓存机制
##### （3）Dom Storage 缓存机制
##### （4）Web SQL Database缓存机制
##### （5）Indexed Database 缓存机制
##### 
##### 1.1.1、浏览器缓存机制：
##### 根据Http协议头里的Cache-Control(或Expire) 和 Last-Modified（或ETag）等字段来控制文件的缓存机制：
##### （1）Cache-Control:用于控制文件在本地缓存的有效时长。
##### 如服务器回包：Cache-Control:max-age=600，则表示文件在本地应该缓存，且有效时长是600秒（从发出请求算起）。在接下来600秒内，如果有请求这个资源，浏览器不会发出 HTTP 请求，而是直接使用本地缓存的文件。
##### （2）Expire 与Cache-Control字段类似【前者是HTTP 1.0标准中的字段，后者是HTTP 1.1标准中的字段，同时出现后者优先级高】
##### （3）Last-Modified:标识文件在服务器上的最新更新时间。
##### 如果本地缓存文件时效到期，那么会请求服务端，请求的时候浏览器会通过If-Modified-Since字段带上这个时间，服务端收到这个时间戳会和
##### 会和服务器最后更改的时间进行对比，如果相同则表示，最新文件未更新。这个时候会返回304，客户端继续使用缓存文件；如果服务器文件有更新，
##### 那么会返回200，同时把最新文件返回。
##### （4）ETag 与 Last-Modified功能类似，即标识文件在服务器上的最新更新时间。
##### 区别是ETag是一个对文件进行标识的字符串。在向服务器查询文件是否有更新时，浏览器通过If-None-Match字段把特征字串发送给服务端来进行匹配，以此
##### 来判定文件是否有更新。没有更新返回304，有则返回200。ETag和Last-Modified可以同时使用，此时只要满足一个条件，则认为文件未更新。

##### 常见用法： 
##### Cache-Control与Last-Modified一起使用；Expire与ETag一起使用。
##### 即一个用于控制缓存有效时间，一个用于在缓存失效后，向服务端查询是否有更新。

##### 注：浏览器缓存机制是浏览器内核的机制，一般都是标准实现，不需要客户端操心。 
#####
##### 1.1.2、Application Cache缓存机制：以文件为单位进行缓存，且文件有一定更新机制（类似于浏览器缓存机制）【专门为 Web App离线使用而开发的缓存机制】
#####  AppCache 原理有两个关键点：manifest 属性和 manifest 文件。

##### 1.1.3、Dem Storage 缓存机制：通过存储字符串的 Key - Value 对来提供
##### DOM Storage 分为 sessionStorage & localStorage； 二者使用方法基本相同，区别在于作用范围不同：a. sessionStorage：具备临时性，即存储与页面相关的数据，它在页面关闭后无法使用 b. localStorage：具备持久性，即保存的数据在页面关闭后也可以使用。
##### 应用场景：存储临时、简单的数据。代替 **将 不需要让服务器知道的信息 存储到 cookies **的这种传统方法。Dom Storage 机制类似于 Android 的 SharedPreference机制。
#####
##### 1.1.4、Web SQL Database 缓存机制：基于 SQL 的数据库存储机制【不再维护，取而代之的是IndexedDB缓存机制】
##### 应用场景：存储适合数据库的结构化数据
##### 
##### 1.1.5、IndexedDB 缓存机制：属于 NoSQL 数据库，通过存储字符串的 Key - Value 对来提供，类似于 Dom Storage 存储机制 的key-value存储方式。
##### 应用场景：存储 复杂、数据量大的结构化数据
#####
##### 1.1.6、File System【WebView暂不支持】：为 H5页面的数据 提供一个虚拟的文件系统

##### 缓存机制汇总如下：
![avatar](https://upload-images.jianshu.io/upload_images/944365-5f9648d34bc73b18.png)
##### 



  
##### 1.2、缓存模式：即告诉Android WebView 什么时候去读缓存，以哪种方式去读缓存 
##### Android WebView 自带的缓存模式有4种：
##### （1）LOAD_CACHE_ONLY： 只使用缓存； 
##### （2）LOAD_NO_CACHE： 只从网络获取；
##### （3）LOAD_DEFAULT：（默认）根据cache-control决定是否从网络上取数据。
##### （4）LOAD_CACHE_ELSE_NETWORK： LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
##### 总结：根据以上两种模式，建议缓存策略为，判断是否有网络，有的话，使用LOAD_DEFAULT，无网络时，使用LOAD_CACHE_ELSE_NETWORK。
#####



  
##### 2.1、资源预加载：提早加载将需使用的H5页面，即 提前构建缓存
##### 2.1.1、预加载WebView对象
![avatar](https://upload-images.jianshu.io/upload_images/944365-c14d7fef491bb587.png)
##### 
##### 2.1.2、预加载H5资源：在应用启动、初始化第一个WebView对象时，直接开始网络请求加载H5页面，后续需打开这些H5页面时就直接从该本地对象中获取
##### 具体实现：在Android 的BaseApplication里初始化一个WebView对象（用于加载常用的H5页面资源）；当需使用这些页面时再从BaseApplication里取过来直接使用
##### 应用场景：对于Android WebView的首页建议使用这种方案，能有效提高首页加载的效率
##### 2.1.3、自身构建缓存：
##### （1）事先将更新频率较低、常用 & 固定的H5静态资源 文件（如JS、CSS文件、图片等） 放到本地
##### （2）拦截H5页面的资源网络请求 并进行检测
##### （3）如果检测到本地具有相同的静态资源 就 直接从本地读取进行替换 而 不发送该资源的网络请求到服务器获取。
#####

  
  
  
  
#### WebView加载内容的几种方式：
##### 1、加载assets目录下的本地网页，如：
##### mWebView.loadUrl("file:///android_asset/html/test1.html");
##### 2、加载远程网页，如：
##### mWebView.loadUrl("http://www.google.com");
##### 3、使用loadData或loadDataWithBaseURL方法加载内容（主要用于加载html片段，而不是一个完整的网页），如：
##### mWebView.loadDataWithBaseURL(null, "<span style=\"\">网页加载失败</span>", "text/html", "utf-8", null);
  
  
#### WebView的UserAgent：
##### 用户代理，是Http协议的一部分，属于头域的组成部分，简称为UA。
##### 是一种向访问网站提供你所使用的浏览器类型及版本、操作系统及版本、浏览器内核等信息标识。
##### 通过这个标识，用户所访问的网站可以显示不同的排版，从而为用户提供更好的体验或者信息统计。例如用手机访问谷歌和电脑访问是不一样的，这些是谷歌根据访问者的UA来判断的。  
##### 获取UA的方式：

''
        //得到WebSettings对象
        WebSettings settings = webview.getSettings();
        // 获取到UserAgentString
        String userAgent = settings.getUserAgentString();
''  



#### WebView重定向需要考虑的case：
##### 1、是最普通的http url【不含.doc .apk等下载url】
##### 2、下载的http url【如.doc .apk等】
##### 3、非http或https自定义url 【如 "weixin://  alipays://等】 
  
  
#### Android打开office文档的方案如下：
##### 1、调用第三方的office软件，缺点--不安全。
##### 2、自己解析office文件并自定义控件显示，缺点--工作量非常庞大
##### 3、将office文件转html格式，使用WebView展示，缺点--office转html的工作太过复杂耗时
##### 4、使用第三方的在线预览工具，缺点--不安全
##### 参考[Android打开doc、xlsx、ppt等office文档解决方案](https://blog.csdn.net/u011791526/article/details/73088768) 


#### Android WebView播放音乐属性：
#####  //是否支持播放音乐
#####  ws.setPluginState(WebSettings.PluginState.ON);
#####  ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
      
#####  //是否需要用户点击才播放
#####  ws.setMediaPlaybackRequiresUserGesture(true);
  
  
##### 参考：
  [Android：手把手教你构建 全面的WebView 缓存机制 & 资源加载方案](https://www.jianshu.com/p/5e7075f4875f)

  
  [最全面总结 Android WebView与 JS 的交互方式](https://www.jianshu.com/p/345f4d8a5cfa)
#####
#####
#####
#####
#####
#####
#####
#####
#####
#####
#####
#####
#####
#####
#####
#####
#####
#####
#####
#####
#####
#####
#####
#####
#####
#####
#####
#####
#####

