package hdd.example.defensive.resource;

import com.google.common.base.Optional;
import com.google.common.primitives.Booleans;
import hdd.example.defensive.ValidationHarmfulDTD;
import hdd.example.defensive.ValidationSafetyDTD;
import hdd.example.defensive.ValidationSafetyXSD;
import hdd.example.defensive.core.Response;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by hieudang on 8/31/2016.
 */
@Path("/defensive-programming")
@Produces(MediaType.APPLICATION_JSON)
public class XMLInjectionResource {
    private final String template;
    private final String defaultName;
    private final String REGEX_PATTERN = "[A-Za-z0-9_]+\\.xml";

    public XMLInjectionResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
    }

    @GET
    public Response home() {
        final String message = String.format(template, defaultName);
        return new Response(message);
    }

    @GET
    @Path("/upload")
    public Response uploadXML(@QueryParam("fileName") Optional<String> fileName,
                              @QueryParam("allowedSchema") @DefaultValue("false") Boolean allowedSchema) {
        if (fileName != null) {
            if (!fileName.get().matches(REGEX_PATTERN)) {
                return new Response("FILE TYPE IS NOT SUPPORTED");
            }
            if (fileName.get().equals("harmful_xsd.xml")) {
                try {
                    return new ValidationSafetyXSD(fileName, allowedSchema).upload();
                } catch (Exception e) {
                    return new Response(e.toString());
                }
            }
            else if (fileName.get().equals("safely.xml")) {
                try {
                    return new ValidationSafetyDTD(fileName, allowedSchema).upload();
                } catch (Exception e) {
                    return new Response(e.toString());
                }
            }
            else if (fileName.get().equals("harmful_bat.xml")) {
                try {
                    return new ValidationHarmfulDTD(fileName, allowedSchema).upload();
                } catch (Exception e) {
                    return new Response(e.toString());
                }
            }
        }
        return new Response("NO FILE UPLOADED");
    }
}
