package com.lib.request;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

/*
*
* Consistent way of making a request
*
* */
public class RequestWrapper {

    private final static Logger logger = LogManager.getLogger(RequestWrapper.class);

    public static HttpResponse<String> get(String targetURL) throws Exception {
        return get(targetURL, null, null);
    }

    public static HttpResponse<String> get(String targetURL, Map<String, String> requestHeaders, List<NameValuePair> requestParameters) throws Exception {

        try{
            logger.debug("Creating GET: " + targetURL);
            HttpRequest request = createHttpGetRequest(targetURL, requestHeaders, requestParameters);
            HttpClient client = HttpClient.newHttpClient();

            logger.info("Sending GET: " + targetURL);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Sent GET: " + targetURL);
            return response;
        } catch (Exception e) {
            throw e;
        }
    }

    private static HttpRequest createHttpGetRequest(String targetUrl, Map<String, String> requestHeaders, List<NameValuePair> requestParameters){

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        String url = targetUrl;
        if(requestParameters != null){
            String paramString = URLEncodedUtils.format(requestParameters, "UTF-8");
            url = targetUrl+ "?" + paramString;
        }
        requestBuilder.uri(URI.create(url));

        if(requestHeaders != null)
            for(String key: requestHeaders.keySet())
                requestBuilder.header(key,requestHeaders.get(key));
        // requestBuilder.header("Accept", "application/json");
        requestBuilder.GET();
        HttpRequest request = requestBuilder.build();
        return request;
    }

    public static HttpResponse<String> post(String targetURL, Map<String, String> requestHeaders, String postBody) throws Exception {

        try{
            logger.debug("Creating POST: " + targetURL);
            HttpRequest request = createHttpPostRequest(targetURL, requestHeaders, postBody);
            HttpClient client = HttpClient.newHttpClient();

            logger.info("Sending POST: " + targetURL);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Sent POST: " + targetURL);
            return response;
        } catch (Exception e) {
            throw e;
        }
    }

    private static HttpRequest createHttpPostRequest(String targetUrl, Map<String, String> requestHeaders, String postBody){

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        requestBuilder.uri(URI.create(targetUrl));
        if(requestHeaders != null)
            for(String key: requestHeaders.keySet())
                requestBuilder.header(key,requestHeaders.get(key));
        // requestBuilder.header("Accept", "application/json");
        requestBuilder.POST(HttpRequest.BodyPublishers.ofString(postBody != null ? postBody : ""));

        HttpRequest request = requestBuilder.build();
        return request;
    }

}


