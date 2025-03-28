package comp3350.gymbuddy.tests.logic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import comp3350.gymbuddy.logic.StringFormatter;

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
}