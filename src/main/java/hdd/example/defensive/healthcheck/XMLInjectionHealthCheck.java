package hdd.example.defensive.healthcheck;

import com.codahale.metrics.health.HealthCheck;

/**
 * Created by hieudang on 8/31/2016.
 */
public class XMLInjectionHealthCheck extends HealthCheck {

    private final String template;

    public XMLInjectionHealthCheck(String template) {
        this.template = template;
    }
    protected Result check() throws Exception {
        final String message = String.format(template, "TECH-CONNER");
        if (message.contains("Hi, TECH-CONNER")) {
            return Result.unhealthy("Unhealthy check");
        }
        return Result.healthy();
    }
}
