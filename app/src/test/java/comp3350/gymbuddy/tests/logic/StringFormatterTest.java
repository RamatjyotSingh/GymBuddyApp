package comp3350.gymbuddy.tests.logic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import comp3350.gymbuddy.logic.util.StringFormatter;

public class StringFormatterTest {
    private StringFormatter formatter;

    @Before
    public void setup() {
        formatter = new StringFormatter();
    }

    //Test formatTime() method
    @Test
    public void testFormatTime_SecondsOnly() {
        assertEquals("45s", formatter.formatTime(45));
        assertEquals("59s", formatter.formatTime(59.4));
        assertEquals("1s", formatter.formatTime(0.6));
    }

    @Test
    public void testFormatTime_Zero() {
        assertEquals("0s", formatter.formatTime(0));
        assertEquals("0s", formatter.formatTime(0.4));
    }

    @Test
    public void testFormatTime_LargeValue() {
        assertEquals("27h 46m 39s", formatter.formatTime(99999));
    }

    //Test formatWeight() method
    @Test
    public void testFormatWeight_WholeNumbers() {
        assertEquals("150 lbs", formatter.formatWeight(150));
        assertEquals("0 lbs", formatter.formatWeight(0));
        assertEquals("200 lbs", formatter.formatWeight(200));
    }

    @Test
    public void testFormatWeight_DecimalRounding() {
        assertEquals("150 lbs", formatter.formatWeight(149.6));
        assertEquals("151 lbs", formatter.formatWeight(150.5));
        assertEquals("100 lbs", formatter.formatWeight(99.9));
    }

    @Test
    public void testFormatWeight_LargeValue() {
        assertEquals("999 lbs", formatter.formatWeight(999.4));
        assertEquals("1000 lbs", formatter.formatWeight(999.9));
    }

    @Test
    public void testFormatInstructions_BasicCase() {
        String input = "Step 1\\nStep 2\\nStep 3";
        List<String> expected = Arrays.asList("Step 1", "Step 2", "Step 3");
        assertEquals(expected, formatter.formatInstructions(input));
    }

    @Test
    public void testFormatInstructions_SingleLine() {
        String input = "Single instruction";
        List<String> expected = Arrays.asList("Single instruction");
        assertEquals(expected, formatter.formatInstructions(input));
    }

    @Test
    public void testFormatInstructions_EmptyInput() {
        String input = "";
        List<String> expected = Arrays.asList();
        assertEquals(expected, formatter.formatInstructions(input));
    }

    @Test
    public void testFormatInstructions_OnlyWhitespace() {
        String input = "   \\n  \\n  \\n   ";
        List<String> expected = Arrays.asList();
        assertEquals(expected, formatter.formatInstructions(input));
    }
}