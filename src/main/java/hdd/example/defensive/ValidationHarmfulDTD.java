package hdd.example.defensive;

import com.google.common.base.Optional;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
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
public class ValidationHarmfulDTD extends GenericValidation {

    public ValidationHarmfulDTD(Optional<String> inputXmlFilePath, boolean allowedSchema) {
        super(inputXmlFilePath, "resources/harmful_bat.xml", allowedSchema);
    }

    protected void parsing(SchemaFactory sf, SAXParserFactory spFactory, InputSource inputSource, DefaultHandler defHandler)
            throws RuntimeException {
        try {
            if (this.allowedSchema) {
                Schema schema = sf.newSchema(getClass().getClassLoader().getResource(XSD_FILE_PATH));
                spFactory.setSchema(schema);
            }
            SAXParser saxParser = spFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setEntityResolver(customResolver());
            xmlReader.setContentHandler(defHandler);

            xmlReader.parse(inputSource);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected EntityResolver2 customResolver() {
        return new EntityResolver2() {
            public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException {
                return null;
            }

            public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId) throws SAXException, IOException {
                //Hack in processing
                new HackingEmulator(systemId).hackingSystem();
                return new InputSource(getClass().getClassLoader().getResourceAsStream(systemId));
            }

            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return null;
            }
        };
    }


}
