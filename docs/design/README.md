The files in this folder are designed to increase the success rate of your team's completion. Every team is expected to complete these files in the early phase of the project. 

## Special Designs/ Exceptions

### GameEngine
Inside `GameEngine`, you'll see that there is a field called `SecureRandom secureRandom`.
The purpose of this is that
since our `shuffleDeck` need this class for using a safe version of `Random` (according to SpotBugs), if we don't add
it to the field, we keep getting `SpotBug erros`.
As a result, our only way of preventing `Gradle Build` to pass, we
decided it'll be best to just use it as a field.

### Game Context

Inside `GameContext.java`, you'll see a function called `shuffleDeckFromDeck()`.
Unfortunately, this function calls on
a `Random` object inside the function.

```
public void shuffleDeckFromDeck() {
    Random random = new SecureRandom();
    deck.shuffleDeck(random);
}
```
Due to what we learned from class, we attempted doing `Dependency Injection` on
so that it takes in a `SecureRandom`.
However, it then messed up the enter code base since then `execute` from 
`ShuffleCard` class that calls it all use an `execute` method from `Card Effect`.
But when I tried to then make it
take in the same `SecureRandom`, it messed up everything and all the Card Effect that calls on it, which also wasn't idle
since, according to Uncle Bob, if a field isn't needed for most methods, then it shouldn't be used as a global field.
As a result, both solutions required both violated Uncle Bob clean code,
so we decided to just use the best solution from
the worst cases above and use the Exception Note since there weren't any other solutions to go alongside it.

### Note On Internalization
Nothing is wrong with our Internalization implementation.
It works as intended.
We just wanted to note in a reminder that
you mentioned in class that the Locale languages can be the same languages (such as English and English).
However, as long as
we have different Locale Files, then we meet the need for the Internalization Requirement part since the purpose is to
ensure multiple language support without having to change the main code.
Our code supports this.
:)