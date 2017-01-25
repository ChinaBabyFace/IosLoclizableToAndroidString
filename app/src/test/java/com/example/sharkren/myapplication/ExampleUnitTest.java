package com.example.sharkren.myapplication;

import com.example.sharkren.myapplication.praser.IosStringFileParser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testPraseLine() throws Exception {
        IosStringFileParser parser = new IosStringFileParser();
        String line="\"Turn Bluetooth “On” and “Pair” to connect\" = \"\\\"Active\\\" el Bluetooth y \\\"Emparejar\\\" para conectar\";";
        String [] array=parser.parseLineV3(line);
    }
}