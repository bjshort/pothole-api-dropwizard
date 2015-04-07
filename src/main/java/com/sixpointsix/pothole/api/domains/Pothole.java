package com.sixpointsix.pothole.api.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sixpointsix.pothole.api.util.PotholeUtil;
import org.mongojack.ObjectId;
import org.mongojack.Id;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Pothole {
    @Id @ObjectId
    private String key;

    private String location;
    Date dateReported;
    Boolean fixed;

    public Pothole(String location){
        this.location = location;
        this.dateReported = new Date();
        this.fixed = false;
    }

    //dummy constructor
    public Pothole() {
        this.dateReported = new Date();
        this.fixed = false;
    }

    public String getKey(){
        return key;
    }

    public Pothole setKey(final String key){
        this.key = key;
        return this;
    }

    public String getLocation(){
        return location;
    }

    public Pothole setLocation(final String location){
        this.location = location;
        return this;
    }

    public String getDateReported(){
        SimpleDateFormat dt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        return dt.format(this.dateReported);
    }

    public Pothole setDateReported(final Date dateReported){
        this.dateReported = dateReported;
        return this;
    }

    public Boolean getFixed(){
        return this.fixed;
    }

    public Pothole setFixed(final Boolean fixed){
        this.fixed = fixed;
        return this;
    }

    @JsonProperty("formattedAddress")
    private String getFormattedAddress() {
        return PotholeUtil.parseGeoCode(this.location);
    }

    @JsonIgnore
    @JsonProperty("formattedAddress")
    private String setFormattedAddress(final String str) {
        return "";
    }

    @Override
    public String toString(){
        String str = null;
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            str = ow.writeValueAsString(this);
        } catch (JsonProcessingException ex){
            System.out.println("-- There was an error parsing json. --");
            System.out.println(ex.toString());
        }

        return str;
    }

    private static SimpleDateFormat dateFormat (){
        return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    }
}
