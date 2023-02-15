package com.konloch;

import com.konloch.httprequest.HTTPRequest;

import java.util.ArrayList;

/**
 * Used to test the API
 *
 * @author Konloch
 * @since 2/15/2023
 */
public class HTTPRequestTest
{
	public static void main(String[] args) throws Exception
	{
		HTTPRequest request = new HTTPRequest("https://google.com");
		
		ArrayList<String> lines = request.read();
		
		for(String line : lines)
			System.out.println(line);
	}
}
