# BVA Analysis for **UserInterface**

## Method under test: `String getUserInput(String message)`

### Step 1-3 Results

|            | Input 1                                       | Input 2                               | Output                                        | Side‑effect                                               |
|------------|-----------------------------------------------|---------------------------------------|-----------------------------------------------|-----------------------------------------------------------|
| **Step 1** | message to be printed before asking for input | arbitrary line from console           | returned string (same string without newline) | print message and from new line prints "> " prompt        |
| **Step 2** | String                                        | String                                | String                                        | Boolean (does it prints message in new line and then "> " |
| **Step 3** | null, empty string, more than 1 character     | empty string, more than one character | empty string, more than one character         | yes/no                                                    |

### Step 4

|             | System under test                          | Expected output                                           | Implemented?       | Test name                                                                            |
|-------------|--------------------------------------------|-----------------------------------------------------------|--------------------|--------------------------------------------------------------------------------------|
| Test Case 1 | message `null`                             | `NullPointerException`  ("Card cannot be null")           | :white_check_mark: | `getUserInput_withNullMessageAndNonEmptyConsoleInput_returnsConsoleInput`            |
| Test Case 1 | message `""`, input `"hello world"`        | returns "hello world"; stdout contains "> "               |                    | `getUserInput_withEmptyMessageAndNonEmptyConsoleInput_returnsConsoleInput`           |
| Test Case 2 | message `"hello world"`, input `""`        | returns ""; stdout contains "> " and "hello world"        |                    | `getUserInput_withNonEmptyMessageAndEmptyConsoleInput_keepsAskingForInput`           |
| Test Case 2 | message `"message"`, input `"hello world"` | returns "hello world"; stdout contains "> " and "message" |                    | `getUserInput_withNonEmptyMessageAndConsoleInput_returnsConsoleLineAndPrintsMessage` |
