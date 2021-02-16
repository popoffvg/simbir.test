package ru.popoffvg.simbir;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TextParser {

    private static Pattern SPLITTER = Pattern.compile("'(.+?)'");
    private final char[] delimiters;
    private final int bufferSize;
    private final Map<String, Integer> parseResult = new HashMap<>();

    public TextParser(int bufferSize) {
        delimiters = new char[]{' ', ',', '.', '!', '?', '"', ';', ':', '[', ']', '(', ')', '\n', '\r', '\t'};
        Arrays.sort(delimiters);
        this.bufferSize = bufferSize;
    }

    public Map<String, Integer> parse(Reader input) throws IOException {
        StringBuilder builder = new StringBuilder();

        char[] buffer = new char[bufferSize];

        int readed;
        while ((readed = input.read(buffer, 0, bufferSize)) != -1) {
            for (int i = 0; i < readed; i++) {
                char symbol = buffer[i];
                if (Arrays.binarySearch(delimiters, symbol) >= 0) {
                    String word = builder.toString();

                    parseResult.compute(word, (key, count) -> {
                        if(key.isEmpty()) return null;
                        if(count == null) count = 0;
                        return ++count;
                    });

                    builder = new StringBuilder();
                } else {
                    builder.append(Character.toLowerCase(symbol));
                }
            }
        }

        if (builder.length() != 0) {
            parseResult.compute(builder.toString(), (key, count) -> {
                if(count == null) count = 0;
                return ++count;
            });
        }

        return parseResult;
    }
}
