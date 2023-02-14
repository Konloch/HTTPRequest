# HTTPRequest
This is a very simple HTTPRequest wrapper.

Supports defining cookies, proxies, post data, and custom useragents.

## Example Usage:

**Simple Request:**
```java
HTTPRequest request = new HTTPRequest(new URL("http://the.bytecode.club/"));
String[] webpage = request.read();
```

**Complex Request:**
```java
HTTPRequest request = new HTTPRequest(new URL("http://the.bytecode.club/"));
request.setTimeout(10000);
request.setPostData("postdata=yes&awesome=yup");
request.setReferer("http://google.com/");
request.setCookie("cookies=yes;cool=sure");
request.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 81)));
String[] webpage = request.read();
for (Map.Entry<String, List<String>> k : request.getLastConnectionHeaders())
	System.out.println(k.toString());
``
