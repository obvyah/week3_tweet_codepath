package com.codepath.apps.mysimpletweet.service;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.client.utils.URLEncodedUtils;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class RestClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "YYkBnoLW1z44vXVubXLVrUpTk";       // Change this
	public static final String REST_CONSUMER_SECRET = "0qkeSfBtkx6zbAQ4GibZ1EKIPxV7kCtN99uAQK61jCPIMT35eC"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cprest"; // Change this (here and in manifest)
	private static final String TAG = "Client";

	public RestClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(int count, Long max_id, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count",count);
		if(max_id!=null) {
			params.put("max_id", max_id);
		}
		client.get(apiUrl, params, handler);
	}

	public void getMentionTimeline(int count, Long max_id, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count",count);
		if(max_id!=null) {
			params.put("max_id", max_id);
		}
		client.get(apiUrl,params,handler);
	}

	public void getAccountSetting(AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("account/settings.json");
		client.get(apiUrl,handler);
	}

	public void getUserShowById(String user_id,AsyncHttpResponseHandler handler){
		getUserShow(user_id,null,handler);
	}

	public void getUserShowByScreenName(String screen_name,AsyncHttpResponseHandler handler){
		getUserShow(null,screen_name,handler);
	}

	private void getUserShow(String user_id,String screen_name, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("users/show.json");
		RequestParams params = new RequestParams();
		if(user_id!=null){
			params.add("user_id",user_id);
			client.get(apiUrl,params,handler);
		}else if(screen_name!=null){
			params.add("screen_name",screen_name);
			client.get(apiUrl,params,handler);
		}else {
			Log.e(TAG,"para null");
		}
	}

	public void postTweet(String status, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		try {
			params.add("status", URLEncoder.encode(status,"utf8"));
			client.post(apiUrl,params,handler);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
