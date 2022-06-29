package jsonparsing;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jsonparsing.pojo.AuthorPojo;
import jsonparsing.pojo.BookPojo;
import jsonparsing.pojo.DayPojo;
import jsonparsing.pojo.SimpleTestCaseJsonPojo;
import org.junit.jupiter.api.Test;

class JsonTest {

    private String simpleTestCaseJsonSource = "{\"title\": \"Test text for testing.\"}";

    private String dayScenario1 =" {\n "
        + " \"date\": \"2019-12-25\", \n"
        + " \"name\": \"Christmas Day\" \n"
        + " } ";

    private String authorBookScenario = "{\n" +
        "  \"authorName\": \"Karlo\",\n" +
        "  \"books\":[\n" +
        "      {\n" +
        "        \"title\": \"Title1\",\n" +
        "        \"inPrint\": true,\n" +
        "        \"publishDate\": \"2019-12-25\"\n" +
        "      },\n" +
        "      {\n" +
        "      \"title\": \"Title2\",\n" +
        "      \"inPrint\": false,\n" +
        "      \"publishDate\": \"2019-12-25\"\n" +
        "      }\n" +
        "    ]\n" +
        "}";

    @Test
    void parse() throws IOException {
            JsonNode node = JsonParser.parse(simpleTestCaseJsonSource);

            assertEquals(node.get("title").asText(),"Test text for testing.");
    }

    @Test
    void fromJson() throws IOException{
        JsonNode node = JsonParser.parse(simpleTestCaseJsonSource);
        SimpleTestCaseJsonPojo pojo = JsonParser.fromJson(node, SimpleTestCaseJsonPojo.class);

        assertEquals(pojo.getTitle(), "Test text for testing.");
    }

    @Test
    void toJson(){
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
    void dayTestScenario1() throws IOException{
        JsonNode node = JsonParser.parse(dayScenario1);
        DayPojo pojo = JsonParser.fromJson(node, DayPojo.class);

        assertEquals(pojo.getDate().toString(), "2019-12-25");
    }

    @Test
    void authorBookScenario1() throws IOException{
        JsonNode node = JsonParser.parse(authorBookScenario);
        AuthorPojo pojo = JsonParser.fromJson(node, AuthorPojo.class);

        System.out.println("Author : " + pojo.getAuthorName());
        for(BookPojo bp : pojo.getBooks()){
            System.out.println("Book : " + bp.getTitle());
            System.out.println("Is In Print? : " + bp.isInPrint());
            System.out.println("Date : " + bp.getPublishDate());
        }
    }
}