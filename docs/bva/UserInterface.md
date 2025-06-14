# BVA Analysis for **UserInterface**

## Method under test: `String getUserInput(String message)`

### Step 1-3 Results

|            | Input 1                                       | Input 2                               | Output                                        | Side‑effect                                               |
|------------|-----------------------------------------------|---------------------------------------|-----------------------------------------------|-----------------------------------------------------------|
| **Step 1** | message to be printed before asking for input | arbitrary line from console           | returned string (same string without newline) | print message and from new line prints "> " prompt        |
| **Step 2** | String                                        | String                                | String                                        | Boolean (does it prints message in new line and then "> " |
| **Step 3** | null, empty string, more than 1 character     | empty string, more than one character | empty string, more than one character         | yes/no                                                    |

### Step 4

|             | System under test                                        | Expected output                                           | Implemented?       | Test name                                                                  |
|-------------|----------------------------------------------------------|-----------------------------------------------------------|--------------------|----------------------------------------------------------------------------|
| Test Case 1 | message `null`, input `"hello world"`                    | returns "hello world"; stdout contains "> "               | :white_check_mark: | `getUserInput_withNullMessageAndNonEmptyConsoleInput_returnsConsoleInput`  |
| Test Case 1 | message `""`, input `"hello world"`                      | returns "hello world"; stdout contains "> "               | :white_check_mark: | `getUserInput_withEmptyMessageAndNonEmptyConsoleInput_returnsConsoleInput` |
| Test Case 2 | message `"message"`, input1 `""`, input2 `"hello world"` | returns ""; stdout repeats "> " and "message" twice       | :white_check_mark: | `getUserInput_withNonEmptyMessageAndEmptyConsoleInput_keepsAskingForInput` |
| Test Case 2 | message `"message"`, input `"hello world"`               | returns "hello world"; stdout contains "> " and "message" | :white_check_mark: | `getUserInput_withValidMessageAndInput_returnsConsoleLineAndPrintsMessage` |
