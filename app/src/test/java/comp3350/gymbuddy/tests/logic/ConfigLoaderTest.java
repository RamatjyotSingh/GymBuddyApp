package comp3350.gymbuddy.tests.logic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import comp3350.gymbuddy.logic.util.ConfigLoader;

@RunWith(MockitoJUnitRunner.class)
public class ConfigLoaderTest {

    @Test
    public void testBuilderCreatesValidConfig() {
        ConfigLoader config = ConfigLoader.builder()
                .scriptPath("test/script/path")
                .configPath("test/config/path")
                .testMode(true)
                .dbAlreadyExists(false)
                .build();

        assertEquals("test/script/path", config.getScriptPath());
        assertEquals("test/config/path", config.getConfigPath());
        assertTrue(config.isTestMode());
        assertFalse(config.isDbAlreadyExists());
    }

    @Test(expected = IllegalStateException.class)
    public void testBuilderMissingScriptPath() {
        ConfigLoader.builder()
                .configPath("test/config/path")
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuilderMissingConfigPath() {
        ConfigLoader.builder()
                .scriptPath("test/script/path")
                .build();
    }

    @Test
    public void testBuilderWithEmptyStrings() {
        try {
            ConfigLoader.builder()
                    .scriptPath("")
                    .configPath("test/config/path")
                    .build();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            //expected
        }

        try {
            ConfigLoader.builder()
                    .scriptPath("test/script/path")
                    .configPath("")
                    .build();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            //expected
        }
    }

    @Test
    public void testDefaultBuilderValues() {
        ConfigLoader config = ConfigLoader.builder()
                .scriptPath("test/script/path")
                .configPath("test/config/path")
                .build();

        assertFalse(config.isTestMode());
        assertFalse(config.isDbAlreadyExists());
    }

    @Test
    public void testConfigLoaderImmutable() {
        ConfigLoader config = ConfigLoader.builder()
                .scriptPath("test/script/path")
                .configPath("test/config/path")
                .build();

        assertEquals("test/script/path", config.getScriptPath());
    }

    @Test
    public void testBuilderChaining() {
        ConfigLoader.Builder builder = spy(ConfigLoader.builder());

        builder.scriptPath("script")
                .configPath("config")
                .testMode(true)
                .dbAlreadyExists(true);

        verify(builder).scriptPath("script");
        verify(builder).configPath("config");
        verify(builder).testMode(true);
        verify(builder).dbAlreadyExists(true);
    }
}