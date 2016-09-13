package hdd.example.defensive;

import com.google.common.base.Optional;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;

/**
 * Created by hieudang on 8/11/2016.
 */
public class ValidationSafetyXSD extends GenericValidation {

    public ValidationSafetyXSD(Optional<String> inputXmlFilePath, boolean allowedSchema) {
        super(inputXmlFilePath, "resources/harmful_xsd.xml", allowedSchema);
    }

    protected void parsing(SchemaFactory sf, SAXParserFactory spFactory, InputSource inputSource, DefaultHandler defHandler) throws RuntimeException {
        try {
            if (this.allowedSchema) {
                Schema schema = sf.newSchema(getClass().getClassLoader().getResource(XSD_FILE_PATH));
                spFactory.setSchema(schema);
            }
            SAXParser saxParser = spFactory.newSAXParser();
            saxParser.parse(inputSource, defHandler);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected EntityResolver2 customResolver() {
        return null;
    }
}
