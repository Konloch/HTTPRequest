# HTTPRequest
HTTPRequest is an easy-to-use zero dependency Java wrapper to read from a URL.

Support for Cookies, proxies, UserAgent, post data and more.

## 💡 Requirements
+ Java Runtime 1.8 **or higher**

## ⚙️ How To Add As Library
Add it as a maven dependency or just [download the latest release](https://github.com/Konloch/HTTPRequest/releases).
```xml
<dependency>
  <groupId>com.konloch</groupId>
  <artifactId>HTTPRequest</artifactId>
  <version>2.2.0</version>
</dependency>
```

## 📚 Links
* [Website](https://konloch.com/HTTPRequest/)
* [Discord Server](https://discord.gg/aexsYpfMEf)
* [Download Releases](https://konloch.com/HTTPRequest/releases)

## 💻 How To Use
**Simple Request:**
```java
HTTPRequest request = new HTTPRequest(new URL("https://google.com/"));
		
ArrayList<String> webpage = request.read();

for(String line : webpage)
    System.out.println(line);
```

**Advanced Request:**
```java
HTTPRequest request = new HTTPRequest(new URL("https://google.com/"));
request.setTimeout(10000);
request.setPostData("postdata=yes&awesome=yup");
request.setReferer("http://google.com/");
request.setCookie("cookies=yes;cool=sure");
request.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 81)));

ArrayList<String> webpage = request.read();
for(String line : webpage)
    System.out.println(line);

for (Map.Entry<String, List<String>> k : request.getLastConnectionHeaders())
    System.out.println("Header Value:" + k.toString());
```
