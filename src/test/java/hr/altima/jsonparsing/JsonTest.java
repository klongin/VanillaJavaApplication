package hr.altima.jsonparsing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import hr.altima.jsonparsing.pojo.AuthorPojo;
import hr.altima.jsonparsing.pojo.BookPojo;
import hr.altima.jsonparsing.pojo.DayPojo;
import hr.altima.jsonparsing.pojo.SimpleTestCaseJsonPojo;
import hr.altima.utilities.jsonparsing.JsonParser;
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

    @Test
    void dayTestScenario1() throws IOException {
        String dayScenario1 = """
            {
             "date": "2019-12-25",\s
            "name": "Christmas Day"\s
            }\s""".indent(1);
        JsonNode node = JsonParser.parse(dayScenario1);
        DayPojo pojo = JsonParser.fromJson(node, DayPojo.class);

        assertEquals(pojo.getDate().toString(), "2019-12-25");
    }

    @Test
    void authorBookScenario1() throws IOException {
        String authorBookScenario = """
            {
              "authorName": "Karlo",
              "books":[
                  {
                    "title": "Title1",
                    "inPrint": true,
                    "publishDate": "2019-12-25"
                  },
                  {
                  "title": "Title2",
                  "inPrint": false,
                  "publishDate": "2019-12-25"
                  }
                ]
            }""";
        JsonNode node = JsonParser.parse(authorBookScenario);
        AuthorPojo pojo = JsonParser.fromJson(node, AuthorPojo.class);

        System.out.println("Author : " + pojo.getAuthorName());
        for (BookPojo bp : pojo.getBooks()) {
            System.out.println("Book : " + bp.getTitle());
            System.out.println("Is In Print? : " + bp.isInPrint());
            System.out.println("Date : " + bp.getPublishDate());
        }
    }
}