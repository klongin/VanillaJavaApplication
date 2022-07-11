package hr.altima.jsonparsing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import hr.altima.dataaccess.tables.Report;
import hr.altima.jsonparsing.pojo.SimpleTestCaseJsonPojo;
import hr.altima.utils.jsonparsing.JsonParser;
import org.junit.jupiter.api.Test;

class JsonTest {

    private final String simpleTestCaseJsonSource = "{\"title\": \"Test text for testing.\"}";

    @Test
    void parse() throws IOException {
        JsonNode node = JsonParser.parse(simpleTestCaseJsonSource);

        assertEquals(node.get("title").asText(), "Test text for testing.");
    }

    @Test
    void fromJson() throws IOException {
        JsonNode node = JsonParser.parse(simpleTestCaseJsonSource);
        SimpleTestCaseJsonPojo pojo = JsonParser.fromJson(node, SimpleTestCaseJsonPojo.class);

        assertEquals(pojo.getTitle(), "Test text for testing.");
    }

    @Test
    void toJson() {
        SimpleTestCaseJsonPojo pojo = new SimpleTestCaseJsonPojo();
        pojo.setTitle("Test text for testing.");

        JsonNode node = JsonParser.toJson(pojo);

        assertEquals(node.get("title").asText(), "Test text for testing.");
    }

    @Test
    void stringify() throws JsonProcessingException {
        SimpleTestCaseJsonPojo pojo = new SimpleTestCaseJsonPojo();
        pojo.setTitle("Test text for testing.");

        JsonNode node = JsonParser.toJson(pojo);

        System.out.println(JsonParser.stringify(node));
        System.out.println(JsonParser.prettyStringify(node));
    }

}