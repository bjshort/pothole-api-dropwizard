package com.sixpointsix.pothole.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.base.Optional;
import com.sixpointsix.pothole.api.domains.Pothole;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.base.Optional;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import java.util.List;
public class PotholeResourceSpecTest {

    public static final String WEB_INF_LOCATION = "src/main/webapp/WEB-INF/web.xml";
    public static final String WEB_APP_LOCATION = "src/main/webapp";
    public static final String TEST_PORT = "8080";
    public static final String API_BASE_URL = "http://localhost:" + TEST_PORT;

    CloseableHttpClient client;
    public static String tempPotholeKey;

//    @BeforeClass
//    public static void startUp() throws Exception {
//        startServer();
//    }

    @ClassRule
    public static final DropwizardAppRule<Config> RULE =
            new DropwizardAppRule<Config>(App.class, "config.yaml");

    @Before
    public void setUpHttpClient(){
        client = HttpClients.createDefault();
    }

    @Test
    public void can_hit_potholes_endpoint_and_get_correct_status() throws Exception {
        HttpGet httpGet = new HttpGet(API_BASE_URL + "/potholes");
        CloseableHttpResponse response = client.execute(httpGet);

        try {
            assert (response.getStatusLine().getStatusCode() == 200);
        } finally {
            response.close();
        }
    }

    @Test
    public void can_add_a_new_pothole() throws Exception {
        HttpPost httpPost = new HttpPost(API_BASE_URL + "/potholes");
        StringEntity json = new StringEntity("{\"location\":\"mayfair\"}");
        httpPost.addHeader("content-type", "application/json");
        httpPost.setEntity(json);
        CloseableHttpResponse response = client.execute(httpPost);

        try {
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            assert (response.getStatusLine().getStatusCode() == 200);
            assert (responseString.contains("\"location\":\"mayfair\","));
        } finally {
            response.close();
        }
    }

    @Test
    public void can_search_for_test_location_and_get_results() throws Exception {
        HttpGet httpGet = new HttpGet(API_BASE_URL + "/potholes?location=mayfair");
        CloseableHttpResponse response = client.execute(httpGet);

        try {
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            assert (response.getStatusLine().getStatusCode() == 200);
            assert (responseString.contains("\"location\":\"mayfair\","));
        } finally {
            response.close();
        }
    }

    @Test
    public void can_search_for_non_existant_location_and_get_no_results() throws Exception {
        HttpGet httpGet = new HttpGet(API_BASE_URL + "/potholes?location=emptylocation");
        CloseableHttpResponse response = client.execute(httpGet);

        try {
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            assert (response.getStatusLine().getStatusCode() == 200);
            assert (responseString.equals("[]"));
        } finally {
            response.close();
        }
    }

    @Test
    public void can_search_for_pothole_by_id() throws Exception {
        HttpGet httpGet = new HttpGet(API_BASE_URL + "/potholes?location=mayfair");
        CloseableHttpResponse response = client.execute(httpGet);

        try {
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            //Turn into an object
            ObjectMapper om = new ObjectMapper();
            TypeFactory typeFactory = om.getTypeFactory();
            List<Pothole> pothole = om.readValue(responseString, typeFactory.constructCollectionType(List.class, Pothole.class));

            tempPotholeKey = pothole.get(0).getKey().replace("\"", "");
            httpGet = new HttpGet(API_BASE_URL + "/potholes/" + tempPotholeKey);
            response = client.execute(httpGet);

            entity = response.getEntity();
            responseString = EntityUtils.toString(entity, "UTF-8");

            assert (responseString.contains(tempPotholeKey));
            assert (response.getStatusLine().getStatusCode() == 200);
        } finally {
            response.close();
        }
    }

    @Test
    public void can_delete_a_pothole_by_id() throws Exception {
        HttpDelete httpDelete = new HttpDelete(API_BASE_URL + "/potholes/" + tempPotholeKey);
        CloseableHttpResponse response = client.execute(httpDelete);

        try {
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");


            System.out.println("------------ BODY -------------");
            System.out.println(responseString);
            System.out.println("------------ END BODY -------------");

            assert (response.getStatusLine().getStatusCode() == 200);
        } finally {
            response.close();
        }
    }

}