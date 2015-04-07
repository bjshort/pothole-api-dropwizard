package com.sixpointsix.pothole.api;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.sixpointsix.pothole.api.domains.Pothole;
import com.sixpointsix.pothole.api.resources.PotholeResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.mongojack.JacksonDBCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;


public class App extends Application<Config>{
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    @Override
    public void initialize(Bootstrap<Config> b) {

    }

    @Override
    public void run(Config conf, Environment env) throws Exception {
        try {
            Mongo mongo = new Mongo(conf.getMongohost(),
                    conf.getMongoport());
            DB db = mongo.getDB(conf.getMongodb());

            JacksonDBCollection<Pothole, String> potholeCollection = null;
            if (db.collectionExists("pothole")) {
                potholeCollection = JacksonDBCollection.wrap(
                        db.getCollection("pothole"), Pothole.class, String.class);
            } else {
                db.createCollection("pothole", BasicDBObjectBuilder.start().get());
            }

            env.jersey().register(new PotholeResource(potholeCollection));
//            configureCors(env);
        } catch (Exception ex) {
            LOGGER.info("Could not register environment variables: %s", ex.getMessage());
        }
    }

    private void configureCors(Environment environment) {
        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        filter.setInitParameter("allowCredentials", "true");
    }

    public static void main( String[] args ) throws Exception {
        new App().run(args);
    }
}
