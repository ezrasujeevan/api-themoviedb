/*
 *      Copyright (c) 2004-2014 Stuart Boston
 *
 *      This file is part of TheMovieDB API.
 *
 *      TheMovieDB API is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      TheMovieDB API is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with TheMovieDB API.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.omertron.themoviedbapi.methods;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omertron.themoviedbapi.MovieDbException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.yamj.api.common.http.CommonHttpClient;

/**
 * Abstract methods
 *
 * @author Stuart
 */
public class AbstractMethod {

    /**
     * The API key to be used
     */
    protected String apiKey;
    private CommonHttpClient httpClient;
    /**
     * Jackson JSON configuration
     */
    protected static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Default constructor for the methods
     *
     * @param apiKey
     * @param httpClient
     */
    public AbstractMethod(String apiKey, CommonHttpClient httpClient) {
        this.apiKey = apiKey;
        this.httpClient = httpClient;
    }

    /**
     * Get the API Key
     *
     * @return
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Set the API Key
     *
     * @param apiKey
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Get the HttpClient
     *
     * @return
     */
    public CommonHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * Set the HttpClient
     *
     * @param httpClient
     */
    public void setHttpClient(CommonHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Get a string version of the requested web page at the URL
     *
     * @param url
     * @return
     * @throws MovieDbException
     */
    protected String requestWebPage(URL url) throws MovieDbException {
        return requestWebPage(url, Boolean.FALSE);
    }

    /**
     * Get a string version of the requested web page at the URL using the passed JSON body and requesting a delete
     *
     * @param url
     * @param isDeleteRequest
     * @return
     * @throws MovieDbException
     */
    protected String requestWebPage(URL url, boolean isDeleteRequest) throws MovieDbException {
        String webpage;
        try {
            HttpGet httpGet = new HttpGet(url.toURI());
            httpGet.addHeader("accept", "application/json");

            if (isDeleteRequest) {
                //TODO: Handle delete request
                throw new MovieDbException(MovieDbException.MovieDbExceptionType.UNKNOWN_CAUSE, "Unable to proces delete request");
            }

            webpage = httpClient.requestContent(httpGet);
        } catch (URISyntaxException ex) {
            throw new MovieDbException(MovieDbException.MovieDbExceptionType.CONNECTION_ERROR, null, ex);
        } catch (IOException ex) {
            throw new MovieDbException(MovieDbException.MovieDbExceptionType.CONNECTION_ERROR, null, ex);
        } catch (RuntimeException ex) {
            throw new MovieDbException(MovieDbException.MovieDbExceptionType.HTTP_503_ERROR, "Service Unavailable", ex);
        }
        return webpage;
    }

    /**
     * Make a post request to get information from a web page using the URL and JSON body
     *
     * @param url
     * @param jsonBody
     * @return
     * @throws MovieDbException
     */
    protected String postWebPage(URL url, String jsonBody) throws MovieDbException {
        String webpage = null;

        try {
            HttpPost httpPost = new HttpPost(url.toURI());
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Accept", "application/json");
            httpPost.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            // Copy the content into the string and close the response
            webpage = EntityUtils.toString(entity);
        } catch (URISyntaxException ex) {
            throw new MovieDbException(MovieDbException.MovieDbExceptionType.CONNECTION_ERROR, null, ex);
        } catch (IOException ex) {
            throw new MovieDbException(MovieDbException.MovieDbExceptionType.CONNECTION_ERROR, null, ex);
        }

        return webpage;
    }

    /**
     * Use Jackson to convert Map to JSON string.
     *
     * @param map
     * @return
     * @throws MovieDbException
     */
    protected static String convertToJson(Map<String, ?> map) throws MovieDbException {
        try {
            return MAPPER.writeValueAsString(map);
        } catch (JsonProcessingException jpe) {
            throw new MovieDbException(MovieDbException.MovieDbExceptionType.MAPPING_FAILED, "JSON conversion failed", jpe);
        }
    }

}
