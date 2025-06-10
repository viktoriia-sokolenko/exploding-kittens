package domain;

import domain.*;
import org.easymock.EasyMock;
import org.junit.jupiter.api.*;
import ui.UserInterface;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class UserInterfaceTest {
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final InputStream originalIn = System.in;

    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    @BeforeEach
    public void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setIn(originalIn);
    }

    @Test
    void displayWelcome_printsExpectedHeader() {
        UserInterface ui = new UserInterface();
        assertDoesNotThrow(ui::displayWelcome);
        String out = outContent.toString();
        assertTrue(out.contains("================================="));
        assertTrue(out.contains("   EXPLODING KITTENS"));
        assertTrue(out.contains("================================="));
    }

    @Test
    void displayHelp_printsAllCommands() {
        UserInterface ui = new UserInterface();
        ui.displayHelp();
        String out = outContent.toString();
        assertTrue(out.contains("Available commands:"));
        assertTrue(out.contains("play <index>"));
        assertTrue(out.contains("draw"));
        assertTrue(out.contains("hand"));
        assertTrue(out.contains("status"));
        assertTrue(out.contains("help"));
        assertTrue(out.contains("quit"));
    }

    @Test
    void displayError_printsToStderr() {
        UserInterface ui = new UserInterface();
        ui.displayError("oops");
        String err = errContent.toString();
        assertTrue(err.contains("Error: oops"));
    }
}
