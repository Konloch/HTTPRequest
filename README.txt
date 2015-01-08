This is a very simple HTTPRequest wrapper I wrote in Java, it currently supports setting post data, cookies, useragent and it has proxy support.

Example:

HTTPRequest request = new HTTPRequest(new URL("http://the.bytecode.club"));
request.setTimeout(10000);
request.setPostData("cool=yes&awesome=yup");
request.setReferer("http://google.com");
request.setCookie("cookies=yes;cool=sure");
request.setProxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 81));
String[] webpage = request.read();
for (Map.Entry<String, List<String>> k : request.getLastConnectionHeaders())
	System.out.println(k.toString());

Ofcourse you can just create a simple request by doing:

HTTPRequest request = new HTTPRequest(new URL("http://the.bytecode.club"));
String[] webpage = request.read();

And that will set everything for you automatically.