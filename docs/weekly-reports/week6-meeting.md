# Instructor Project Meeting Minutes

**Time**: Week 6

**Attendees**:
- Paula Eyituoyo Fregene
- Viktoriia Sokolenko
- Ethan Joshua Pineda-Pardo

**Note Taker**: Dr. Yiji Zhang

## Agenda

### Progress Evaluation
- making good progress on the domain layer classes
- next: making progress on UI
- is design documented? Google doc

### Questions from the team (about design, linter, tests, etc.)
1. UI code --- need TDD? Answer: No. But it's important to structure the code into
Model-View-Controller: View classes don't need automated tests; 
But the Controller needs BVA and TDD and automated tests
2. Integration tests? When? Answer --- wait until after the Integration Testing lecture. All the tests you write 
   should be unit tests.
3. Unit test: testing Deck --- Deck uses 2 external modules, one is Card, and one is List from Java
    - Card: 100% should be mocked/stubbed (Card mockCard = EasyMock.createMock(Card.class); BUT NEVER Card card = new Card();)
    - List: we don't have to mock List for the 3 reasons
4. 3+1 cards? Yes. 
5. Checkstyle: https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/google_checks.xml


### Teamwork
#### Strength: What has been working well
Didn't discuss

#### Concerns: What can be improved
Didn't discuss


#### Notes
1. Make sure to do git pull origin main often so the feature branch is up to date of the current state of the project.


## Instruction: After the meeting, please confirm/review the minutes as a team. Add additional notes if needed. Then merge the PR.