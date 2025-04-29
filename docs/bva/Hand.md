## Hand class BVA

### Method under test: `add(Card c)`

||System under test (pre-state)|Expected output / state transition|Implemented?|
|---|---|---|---|
|Test Case 1|`c == null`|`NullPointerException`||
|Test Case 2|Hand `size == MAX_HAND` (already full)|`IllegalStateException` (“Hand full”)||
|Test Case 3|Hand `size == MAX_HAND - 1`|Card appended; hand `size == MAX_HAND`||
|Test Case 4|Hand `size == 0` (empty)|Card appended; hand `size == 1`; `isEmpty()` → `false`||

### Method under test: `remove(Card c)`

||System under test (pre-state)|Expected output / state transition|Implemented?|
|---|---|---|---|
|Test Case 1|`c == null`|`NullPointerException`||
|Test Case 2|Hand `size == 0` (empty)|`IllegalStateException` (“Hand empty”)||
|Test Case 3|`c` not present in hand|`IllegalArgumentException` (“Card not in hand”)||
|Test Case 4|Hand `size == 1` and `c` present|Card removed; hand `size = 0`; `isEmpty()` → `true`||
|Test Case 5|Hand `size > 1` and `c` present|Card removed; hand `size` decrements by 1||

### Method under test: `contains(CardType type)`

_just an idea for checking if for example, the player has a defuse card in their hand_

||System under test|Expected output|Implemented?|
|---|---|---|---|
|Test Case 1|Hand contains **≥ 1** card of given `type`|`true`||
|Test Case 2|Hand contains **0** cards of given `type`|`false`||

### Method under test: `isFull()`

||System under test|Expected output|Implemented?|
|---|---|---|---|
|Test Case 1|Hand `size < MAX_HAND`|`false`||
|Test Case 2|Hand `size == MAX_HAND`|`true`||

### Method under test: `isEmpty()`

||System under test|Expected output|Implemented?|
|---|---|---|---|
|Test Case 1|Hand `size == 0`|`true`||
|Test Case 2|Hand `size > 0`|`false`||