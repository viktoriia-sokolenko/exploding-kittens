## Card class BVA

### Method under test: `Card(CardType type)` _(constructor)_

||System under test|Expected output|Implemented?|
|---|---|---|---|
|Test Case 1|`type == null`|`NullPointerException`||
|Test Case 2|Valid `CardType`|New `Card` created; `getType() == type`||

### Method under test: `equals(Object o)`

||System under test|Expected output|Implemented?|
|---|---|---|---|
|Test Case 1|Compare card instance with **itself**|`true`||
|Test Case 2|Compare with **different** card of _same_ type/value|Depending on design → usually `true`||
|Test Case 3|Compare with **null**|`false`||
|Test Case 4|Compare with object of **diffe<br/>rent class**|`false`||


