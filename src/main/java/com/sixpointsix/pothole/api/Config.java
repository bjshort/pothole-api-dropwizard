package com.sixpointsix.pothole.api;

import javax.validation.constraints.*;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;

public class Config extends Configuration {

    @Min(1)
    @Max(65535)
    @JsonProperty
    private int mongoport = 27017;

    @JsonProperty
    @NotEmpty
    private String mongohost = "localhost";

    @JsonProperty
    @NotEmpty
    private String mongodb = "potholeDb";

    public int getMongoport() {
        return mongoport;
    }

    public String getMongohost() {
        return mongohost;
    }

    public String getMongodb() {
        return mongodb;
    }

}