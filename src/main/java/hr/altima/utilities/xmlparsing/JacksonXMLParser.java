package hr.altima.utilities.xmlparsing;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * Class for parsing JSON files
 */
public class JacksonXMLParser {

    // Static because we only need 1 object mapper
    private static ObjectMapper objectMapper = getDefaultObjectMapper();

    private static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper defaultObjectMapper = new XmlMapper();

        // Jackson feature that filters out files that do not have required attributes upon deserialization
        defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return defaultObjectMapper;
    }

    /**
     * Static because we don't want to create a new
     * JSON object everytime it is called.
     */
    public static ObjectNode parse(String src) throws IOException {
        return (ObjectNode) objectMapper.readTree(src);
    }

    public static <A> A fromJson(ObjectNode node, Class<A> clazz) throws JsonProcessingException {
        return objectMapper.treeToValue(node, clazz);
    }

    public static ObjectNode toXML(Object a) {
        return objectMapper.valueToTree(a);
    }

    public static String stringify(ObjectNode node) throws JsonProcessingException {
        return generateString(node, false);
    }

    public static String prettyStringify(ObjectNode node) throws JsonProcessingException {
        return generateString(node, true);
    }

    private static String generateString(ObjectNode jsonNode, boolean pretty) throws JsonProcessingException {
        ObjectWriter objectWriter = objectMapper.writer();
        if (pretty) {
            objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        }

        return objectWriter.writeValueAsString(jsonNode);
    }
}
