package hr.altima.utils.txtparsing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TextFileParser {

    public static String parse(String directory) throws IOException {
        return parse(new FileReader(directory));
    }

    public static String parse(FileReader fileReader) throws IOException {
        BufferedReader br = new BufferedReader(fileReader);
        StringBuilder sb = new StringBuilder();
        String st;
        while ((st = br.readLine()) != null) {
            sb.append(st).append("\n");
        }
        return String.valueOf(sb);
    }

}
