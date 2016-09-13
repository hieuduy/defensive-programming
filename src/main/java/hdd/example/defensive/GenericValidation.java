package hdd.example.defensive;

import com.google.common.base.Optional;
import hdd.example.defensive.core.Response;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hieudang on 8/11/2016.
 */
public abstract class GenericValidation {

    protected final String DTD_FILE_NAME = "resources/validate.dtd";
    protected final String XSD_FILE_PATH = "resources/schema.xsd";
    protected String inputXmlFilePath = "resources/";
    protected boolean allowedSchema;

    public GenericValidation(Optional<String> inputXmlFilePath, String defaultXmlFile, boolean allowedSchema) {
        this.inputXmlFilePath += inputXmlFilePath.or(defaultXmlFile);
        this.allowedSchema = allowedSchema;
    }

    public Response upload() throws RuntimeException, IOException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        SAXParserFactory spFactory = SAXParserFactory.newInstance();
        InputSource inputSource = new InputSource(getClass().getClassLoader().getResourceAsStream(inputXmlFilePath));
        UserHandler userHandler = new UserHandler();
        parsing(sf, spFactory, inputSource, userHandler);

        return writeFile(userHandler);
    }

    final class UserHandler extends DefaultHandler {
        boolean bName;
        boolean bPrice;
        boolean bQuantity;

        Map<String, String> productInfo = new HashMap<String, String>();

        public Map<String, String> getProductInfo() {
            return productInfo;
        }

        public void warning(SAXParseException s) throws SAXParseException {
            throw s;
        }

        public void error(SAXParseException s) throws SAXParseException {
            throw s;
        }

        public void fatalError(SAXParseException s) throws SAXParseException {
            throw s;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equalsIgnoreCase("product")) {
            } else if (qName.equalsIgnoreCase("name")) {
                bName = true;
            } else if (qName.equalsIgnoreCase("price")) {
                bPrice = true;
            } else if (qName.equalsIgnoreCase("quantity")) {
                bQuantity = true;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (bName) {
                productInfo.put("p-name", new String(ch, start, length));
                bName = false;
            } else if (bPrice) {
                productInfo.put("p-price", new String(ch, start, length));
                bPrice = false;
            } else if (bQuantity) {
                productInfo.put("p-quantity", new String(ch, start, length));
                bQuantity = false;
            }
        }
    }

    private Response writeFile(UserHandler userHandler) {
        return new Response(userHandler.getProductInfo());
    }

    protected abstract void parsing(SchemaFactory sf, SAXParserFactory spFactory, InputSource inputSource, DefaultHandler defHandler)
            throws RuntimeException;

    protected abstract EntityResolver2 customResolver();
}
