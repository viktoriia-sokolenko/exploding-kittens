The files in this folder are designed to increase the success rate of your team's completion. Every team is expected to
complete these files in the early phase of the project.

Thank you, Professor Zhang, for everything and for working with us throughout all our busy schedule and time conflicts.
It
has been nice having you as a professor
as well as someone we could come to ask for help whenever needed. Thank you so much for the extention granted to us. We
have worked hard and tried our best to complete everything on time. (Thank you so much for believing in us). Working on
this project has taught us many things, such as how sometimes not everything tends to go the way you'd like it, but with
constant grit and hardwork, one can get closer to their goals. Through this course, we have learned many lessons and
improved our coding skills for both for ourselves and for others.

We sincerely believe Uncle Bob (and you) would be very proud of how far we've come. Thank you once again for everything.
Have a great summer!

Below I have included some notes we would like you to keep in mind during the grading.

## Special Designs/ Exceptions

## Mutation Testing

Thank you for all that you have done Professor Zhang throughout the process of this project. This group has been trying
our very best tp have 100% Mutation and Code Coverage
since we were aiming for a A throughout. Initially, all seemed to be going well with 100% Code Coverage and Mutation
testing. However, once we started during Internalization, all
our hardwork crumbled. Things started to break. previous good working stopped working. We were able to fix many of the
issues. However, unfortunately, due to time constraints and personal committments conflicts, we were unable to fully
complete and address the difference in the new mutation test, and test that were failing, and so we were only able to
have about 90% mutation testing and approximately 99% code coverage. We had solutions to them all but on Gradle, the 
test weren't passing and we don't have much time longer to fix it.
As a result, we ask for your understanding in this. Please and thank you.

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
take in the same `SecureRandom`, it messed up everything and all the Card Effect that calls on it, which also wasn't
idle
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