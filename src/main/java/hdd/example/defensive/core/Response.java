package hdd.example.defensive.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by hieudang on 8/31/2016.
 */
public class Response {
    private String message = "XML Document is valid";

    public Response() {
        // Jackson deserialization
    }

    public Response(String message) {
        this.message = message;
    }

    public Response(Map<String, String> map) {
        this.message = map.toString();
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    public void setMessage(String message) {
        this.message = message;
    }
}
