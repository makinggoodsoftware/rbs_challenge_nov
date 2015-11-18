package com.mgs.rbsnov.client.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHelper {

	private final String baseUrl;
	private final Gson gson;
	private final static Logger logger = Logger.getLogger(JsonHelper.class.getName());
	private final HttpClient client = HttpClientBuilder.create().build();
	private final String userName;
	private final String password;

	public JsonHelper(String baseUrl, String userName, String password) {
		this.baseUrl = baseUrl;
		this.gson = new GsonBuilder(). create();
		this.userName = userName;
		this.password = password;
	}

	public <T> T processGetRequest(String url, Class<T> clazz) {
		HttpGet get = new HttpGet(baseUrl + url);
		String encoding = Base64.encodeBase64String((userName + ":" + password).getBytes());
		get.setHeader("Authorization", "Basic " + encoding);
		T result = null;
		String json = null;
		try {
			HttpResponse response = client.execute(get);
			if (response.getEntity().getContent() != null )
				json = readResponse(response.getEntity().getContent());		
			result = gson.fromJson(json, clazz);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	public <T, P> T processPostRequest(String url, P arg, Class<T> clazz) throws IllegalStateException, IOException {
		HttpPost post = new HttpPost(baseUrl + url);		
		String encoding = Base64.encodeBase64String((userName + ":" + password).getBytes());
		post.setHeader("Authorization", "Basic " + encoding);
		T result = null;
		try {
			if (arg != null) {
				String message = gson.toJson(arg);
				post.setHeader("Content-Type", "application/json; charset=utf-8");
				post.setEntity(new StringEntity(message));
			}
			HttpResponse response = client.execute(post);			
			if (response != null) {
				InputStream content = response.getEntity().getContent();
				String json = readResponse(content);				
				result = gson.fromJson(json, clazz);
				
			}			 
		} catch (IOException e) {

			logger.error(this.getClass().getSimpleName() + ": " + getStackTrace(e));
		}
		return result;
	}

	private String readResponse(InputStream inputStream) throws IOException {
		StringBuilder responseText = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));		
		String line;
		while ((line = reader.readLine()) != null) {
			responseText.append(line);
		}
		return responseText.toString();
	}

	public static String getStackTrace(final Throwable throwable) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}

	public boolean ping(int timeout) {
		// Otherwise an exception may be thrown on invalid SSL certificates:

		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(baseUrl).openConnection();
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);
			connection.setRequestMethod("HEAD");
			int responseCode = connection.getResponseCode();
			return (200 <= responseCode && responseCode <= 399);
		} catch (IOException exception) {
			return false;
		}
	}
	
	public <T> T getClassfromJson(String json,Class<T> clazz)
	{
		return  gson.fromJson(json, clazz);
	}
}
