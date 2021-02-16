package ru.popoffvg.simbir;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


class TextParserTest {

    private int bufferSize = 2;

    @Test
    void TestParse_MiddleDelimiter() throws IOException {
        var parser = new TextParser(bufferSize);
        var input = new StringReader("start,slovo,finish");
        var result = parser.parse(input).keySet().toArray();

        var template = List.of(new String[]{"start","slovo", "finish"}).toArray();
        Arrays.sort(template);
        Arrays.sort(result);
        assertArrayEquals(template, result);
    }

    @Test
    void TestParse_StartEndDelimetr() throws IOException {
        var parser = new TextParser(bufferSize);
        var initialStream = new StringReader(",start,slovo,finish,");
        var result = parser.parse(initialStream).keySet().toArray();

        var template = List.of(new String[]{"start","slovo", "finish"}).toArray();
        Arrays.sort(template);
        Arrays.sort(result);
        assertArrayEquals(template, result);
    }

    @Test
    void TestParse_EscapingSymbols() throws IOException {

        var parser = new TextParser(bufferSize);
        String builder = ",begin " +
                "1\t" +
                "2\n" +
                "3\r" +
                "4\"" +
                "end,";
        var initialStream = new StringReader(builder);
        var result = parser.parse(initialStream).keySet().toArray();

        var template = List.of(new String[]{"begin","end","1","2","3","4"}).toArray();
        Arrays.sort(template);
        Arrays.sort(result);
        assertArrayEquals(template, result);
    }

    @Test
    void TestParse_EmptyText() throws IOException {

        var parser = new TextParser(bufferSize);
        var initialStream = new StringReader("");
        var result = parser.parse(initialStream);
        assertEquals(0, result.size());
    }
}