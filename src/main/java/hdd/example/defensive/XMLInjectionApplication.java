package hdd.example.defensive;

import hdd.example.defensive.healthcheck.XMLInjectionHealthCheck;
import hdd.example.defensive.resource.XMLInjectionResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by hieudang on 8/31/2016.
 */
public class XMLInjectionApplication extends Application<XMLInjectionConfiguration> {

    public static void main(String[] args) throws Exception {
        new XMLInjectionApplication().run(args);
    }

    public String getName() {
        return "defensive-programming";
    }

    public void initialize(Bootstrap<XMLInjectionConfiguration> bootstrap) {

    }

    public void run(XMLInjectionConfiguration configuration, Environment environment) throws Exception {
        final XMLInjectionResource resource = new XMLInjectionResource(configuration.getTemplate(), configuration.getDefaultName());
        environment.jersey().register(resource);

        final XMLInjectionHealthCheck healthCheck = new XMLInjectionHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
    }
}
