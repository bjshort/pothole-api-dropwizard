package com.sixpointsix.pothole.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PotholeUtil {
    public static String parseGeoCode(String location){
        String locationClean = location.replaceAll(" ","+");
        try {
            JSONObject parsedJson = getLocationDetailsFromGoogle(locationClean);
            Map<String, String> loc = getLatAndLongFromJsonObject(parsedJson);

//            System.out.println("The geo location of " + location + " is: " + loc.get("lat") + "," + loc.get("lng"));

            return loc.get("lat") + "," + loc.get("lng");
        }
        catch (Exception e){
            System.out.println(e);
            return "LOCATION NOT SET";
        }
    }

    public static JSONObject getLocationDetailsFromGoogle(String location) throws Exception {
        URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?address=" + location);
        ObjectMapper mapper = new ObjectMapper();
        return new JSONObject(mapper.readValue(url, Map.class));
    }

    public static Map<String, String> getLatAndLongFromJsonObject(JSONObject data){
//        System.out.println("---- Inside lat long method ----");
//        System.out.println(data);

        HashMap<String, String> ret = new HashMap<>();

        if(data.getJSONArray("results").length() > 0){
            JSONObject results = data.getJSONArray("results").getJSONObject(0);
            JSONObject geometry = results.getJSONObject("geometry");
            JSONObject loc = geometry.getJSONObject("location");
            ret.put("lat", loc.get("lat").toString());
            ret.put("lng", loc.get("lng").toString());
        } else {
            ret.put("lat", "");
            ret.put("lng", "");
        }

        return ret;
    }
}
