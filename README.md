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

<!DOCTYPE html>
<html manifest="demo_html.appcache">
// HTML 在头中通过 manifest 属性引用 manifest 文件
// manifest 文件：就是上面以 appcache 结尾的文件，是一个普通文件文件，列出了需要缓存的文件
// 浏览器在首次加载 HTML 文件时，会解析 manifest 属性，并读取 manifest 文件，获取 Section：CACHE MANIFEST 下要缓存的文件列表，再对文件缓存
<body>
...
</body>
</html>

// 原理说明如下：
// AppCache 在首次加载生成后，也有更新机制。被缓存的文件如果要更新，需要更新 manifest 文件
// 因为浏览器在下次加载时，除了会默认使用缓存外，还会在后台检查 manifest 文件有没有修改（byte by byte)
发现有修改，就会重新获取 manifest 文件，对 Section：CACHE MANIFEST 下文件列表检查更新
// manifest 文件与缓存文件的检查更新也遵守浏览器缓存机制
// 如用户手动清了 AppCache 缓存，下次加载时，浏览器会重新生成缓存，也可算是一种缓存的更新
// AppCache 的缓存文件，与浏览器的缓存文件分开存储的，因为 AppCache 在本地有 5MB（分 HOST）的空间限制

##### 
#####
#####
#####
#####

 