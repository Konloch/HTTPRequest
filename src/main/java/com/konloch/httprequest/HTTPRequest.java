package com.konloch.httprequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A simple wrapper for Java SE classes to write/read an HTTP Request
 * 
 * @author Konloch
 * @since Jan 8, 2015
 */
public class HTTPRequest
{
	public URL url;
	private int timeout = 30_000;
	private String cookie;
	private String referer;
	private String postData;
	private String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0";
	private Proxy proxy;
	private boolean setFollowRedirects = true;
	private BufferedReader reader;
	private DataOutputStream writer;
	private HttpURLConnection connection;
	private Set<Entry<String, List<String>>> lastConnectionHeaders;
	
	/**
	 * Creates a new HTTPRequest object
	 *
	 * @param url any valid URL path
	 * @throws MalformedURLException if no protocol is specified, or an unknown protocol is found, or spec is null.
	 */
	public HTTPRequest(String url) throws MalformedURLException
	{
		this(new URL(url));
	}
	
	/**
	 * Creates a new HTTPRequest object
	 *
	 * @param url any supplied URL object
	 */
	public HTTPRequest(URL url)
	{
		this.url = url;
	}
	
	/**
	 * Sets a referer to send in the HTTP request
	 *
	 * @param referer the referer that will be sent in the HTTP request
	 */
	public void setReferer(String referer)
	{
		this.referer = referer;
	}
	
	/**
	 * Set a cookie string to send in the HTTP request
	 *
	 * @param cookie the cookie that will be sent in the HTTP request
	 */
	public void setCookie(String cookie)
	{
		this.cookie = cookie;
	}
	
	/**
	 * Sets post data to send in the HTTP request
	 *
	 * @param postData the post data that will be sent in the HTTP request
	 */
	public void setPostData(String postData)
	{
		this.postData = postData;
	}
	
	/**
	 * Sets a custom UserAgent, default 'Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0'
	 *
	 * @param userAgent the UserAgent string
	 */
	public void setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;
	}
	
	/**
	 * Sets the seconds till timeout, default 30,000 milliseconds
	 *
	 * @param timeout the timeout in milliseconds
	 */
	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}
	
	/**
	 * Sets a proxy to connect through
	 *
	 * @param proxy
	 */
	public void setProxy(Proxy proxy)
	{
		this.proxy = proxy;
	}
	
	/**
	 * Used to get the headers the webserver sent on our last connection
	 *
	 * NOTE: You can only get these headers after a read function has been called
	 *
	 * @return returns the last connection headers sent from the web server
	 */
	public Set<Entry<String, List<String>>> getLastConnectionHeaders()
	{
		return lastConnectionHeaders;
	}
	
	/**
	 * Follow redirects mean if a 302 or a 301 are encounted it will follow them.
	 *
	 * By default, follow redirects are enabled
	 *
	 * @param setFollowRedirects true enables follow redirects
	 */
	public void setFollowRedirects(boolean setFollowRedirects)
	{
		this.setFollowRedirects = setFollowRedirects;
	}
	
	/**
	 * Reads from a remote URL and returns a String Array
	 *
	 * @return the HTTP request contents as a String Array
	 * @throws IOException if an I/O exception occurs.
	 */
	public String[] readArray() throws IOException
	{
		return readArray(-1);
	}
	
	/**
	 * Reads from a remote URL and returns a String Array, optionally it can do a read for X lines
	 *
	 * @param linesToRead the amount of lines to read, -1 for unlimited
	 * @return the HTTP request contents as a String Array
	 * @throws IOException if an I/O exception occurs.
	 */
	public String[] readArray(int linesToRead) throws IOException
	{
		ArrayList<String> lines = read(linesToRead);
		return lines.toArray(new String[lines.size()]);
	}
	
	/**
	 * Read the first line from a remote URL and returns a single line
	 *
	 * @return the HTTP request contents as a String
	 * @throws IOException if an I/O exception occurs.
	 */
	public String readSingle() throws IOException
	{
		return read(1).get(0);
	}
	
	/**
	 * Reads from a remote URL and returns a String ArrayList
	 *
	 * @return the HTTP request contents as a String ArrayList
	 * @throws IOException if an I/O exception occurs.
	 */
	public ArrayList<String> read() throws IOException
	{
		return read(-1);
	}
	
	/**
	 * Reads from a remote URL and returns a String ArrayList
	 *
	 * @param linesToRead the amount of lines to read, -1 for unlimited
	 * @return the HTTP request contents as a String ArrayList
	 * @throws IOException if an I/O exception occurs.
	 */
	public ArrayList<String> read(int linesToRead) throws IOException
	{
		ArrayList<String> lines;
		
		try {
			setup();
			
			lines = new ArrayList<>();
			String s;
			int counter = 0;
			while((s = reader.readLine()) != null)
			{
				lines.add(s);
				
				if(linesToRead > 0 && counter++ >= linesToRead)
					break;
			}
			
			lastConnectionHeaders = connection.getHeaderFields().entrySet();
		} catch(Exception e) {
			cleanup();
			throw e;
		} finally {
			cleanup();
		}
		
		return lines;
	}
	
	/**
	 * Used to set up the connection to read the content.
	 * @throws IOException if an I/O exception occurs.
	 */
	private void setup() throws IOException
	{
		if(proxy != null)
			connection = (HttpURLConnection) url.openConnection(proxy);
		else
			connection = (HttpURLConnection) url.openConnection();
		
		if(cookie != null)
			connection.setRequestProperty("Cookie", cookie);
		if(referer != null)
			connection.addRequestProperty("Referer", referer);
		
		connection.setRequestProperty("User-Agent", userAgent);
		connection.setReadTimeout(timeout);
		connection.setConnectTimeout(timeout);
		connection.setUseCaches(false);
		HttpURLConnection.setFollowRedirects(setFollowRedirects);
		
		if(postData != null) {
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			writer = new DataOutputStream(connection.getOutputStream());
			writer.writeBytes(postData);
			writer.flush();
		}
		
		reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	}
	
	/**
	 * Used to clean up the connection, closes the connections and nulls the objects
	 */
	private void cleanup() {
		try { reader.close(); } catch(Exception e) {}
		try { writer.close(); } catch(Exception e) {}
		try { connection.disconnect(); } catch(Exception e) {}
		reader = null;
		writer = null;
		connection = null;
	}
	
	/**
	 * Alert that this is a library
	 *
	 * @param args program launch arguments
	 */
	public static void main(String[] args)
	{
		throw new RuntimeException("Incorrect usage");
	}
}