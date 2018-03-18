package com.briar.server.config;

import com.briar.server.resources.UsersResource;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.MediaType;

@Component
@ApplicationPath("/")
public class WebServerConfiguration extends ResourceConfig {

    public WebServerConfiguration() {
        jerseyConfiguration();
        configureSwagger();
    }

    public void jerseyConfiguration() {
        register(UsersResource.class);
    }

    private void configureSwagger() {
        register(ApiListingResource.class);
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1");
        beanConfig.setSchemes(new String[] { "http", "https" });
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/");
        beanConfig.setTitle("Briar WebServer API");
        beanConfig.getSwagger().addConsumes(MediaType.APPLICATION_JSON);
        beanConfig.getSwagger().addProduces(MediaType.APPLICATION_JSON);
        beanConfig.setDescription("API documentation of the REST end points");
        beanConfig.setContact("SOEN390");
        beanConfig.setResourcePackage("com.briar.server.resources");
        beanConfig.setPrettyPrint(true);
        beanConfig.setScan(true);
    }
}