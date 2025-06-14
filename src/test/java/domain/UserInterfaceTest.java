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

	@Test
	public void getUserInput_withEmptyMessageAndNonEmptyConsoleInput_returnsConsoleInput() {
		System.setIn(new ByteArrayInputStream("hello world\n"
				.getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface();

		String emptyMessage = "";
		String result = ui.getUserInput(emptyMessage);
		assertEquals("hello world", result);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("> "));
	}

	@Test
	public void getUserInput_withNonEmptyMessageAndEmptyConsoleInput_keepsAskingForInput() {
		String input = String.join("\n", "", "hello world");
		System.setIn(new ByteArrayInputStream((input + "\n")
				.getBytes(StandardCharsets.UTF_8)));

		UserInterface ui = new UserInterface();
		String message = "message";
		String result = ui.getUserInput(message);

		assertEquals("hello world", result);

		String output = outContent.toString(StandardCharsets.UTF_8);
		int promptCount = output.split("> ", -1).length - 1;
		final int EXPECTED_PROMPTS = 2;
		assertEquals(EXPECTED_PROMPTS, promptCount);
	}

	@Test
	public void getUserInput_withValidMessageAndInput_returnsConsoleInputAndPrintsMessage() {
		System.setIn(new ByteArrayInputStream("hello world\n"
				.getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface();

		String message = "";
		String result = ui.getUserInput(message);
		assertEquals("hello world", result);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("> "));
	}

	@Test
	public void getNumericUserInput_withNullMessage_returnsConsoleInput() {
		System.setIn(new ByteArrayInputStream("1\n"
				.getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface();

		int result = ui.getNumericUserInput(null);
		assertEquals(1, result);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("> "));
	}

	@Test
	public void getNumericUserInput_withEmptyMessage_returnsConsoleInput() {
		System.setIn(new ByteArrayInputStream("1\n"
				.getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface();

		String emptyMessage = "";
		int result = ui.getNumericUserInput(emptyMessage);
		assertEquals(1, result);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("> "));
	}

}
