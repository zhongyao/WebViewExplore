### Hybrid开发(WebView + Native)




#### WebView调用Native的三种方式：

##### 1、通过WebView的addJavascriptInterface进行对象映射。


##### 2、通过WebViewClient的shouldOverrideUrlLoading方法回调拦截url：一般根据scheme（协议格式）、authority（协议名）来判定。


##### 3、通过WebChromeClient的 onJSAlert() onJsPrompt() onJsConfirm() 回调拦截Js对话框alert() confirm() prompt()。




#### Native调用WebView的两种方案：

##### 1、loadUrl("javascript:callJS()")：方法简洁、效率低。当不需要返回值且对性能要求较低时可以考虑使用。

##### 2、evaluateJavascript(script,resultCallback)：4.4以上可以使用，效率高有返回值。




#### 新增JsBridgeActivity + jsbridge.html，用于实现Native + h5 之间的调用协议的实践。