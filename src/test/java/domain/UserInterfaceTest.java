package domain;

import org.junit.jupiter.api.*;
import ui.UserInterface;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
		System.setOut(new PrintStream(outContent, true,
				StandardCharsets.UTF_8));
		System.setErr(new PrintStream(errContent, true,
				StandardCharsets.UTF_8));
	}


	@AfterEach
	public void restoreStreams() {
		System.setOut(originalOut);
		System.setErr(originalErr);
		System.setIn(originalIn);
	}

	@Test
	public void getUserInput_withNullMessageAndNonEmptyConsoleInput_returnsConsoleInput() {
		System.setIn(new ByteArrayInputStream("hello world\n"
				.getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface();

		String result = ui.getUserInput(null);
		assertEquals("hello world", result);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("> "));
	}

}
