# Intructor Feedback

## Progress evaluation :scroll:
- :white_check_mark: There is progress on Game Setup Phase --- there should be code already. But at least there should be some planning.
- :white_check_mark: The project magemenet board, GitHub Projects, is used.
- :white_check_mark: Spotbugs and Checkstyle are set up.

  
## Comments :speech_balloon:
1. Make sure to do git pull origin main often so the feature branch is up to date of the current state of the project.
2. Make sure to structure your project using the MVC pattern (see GUI Development session) --- it will make testing and development a lot easier, and make SpotBug errors easier to fix (as many bad coding practices are results of poor design)
3. Run SpotBugs and Checkstyle locally so you can fix the issue before pushing them into the repository (how: Gradle icon --> Tasks --> verification --> test, if the build script is all set up)
4. Consider this workflow: Team decides to do items and document them in the project management board --> assign the item to a person --> the person creates a feature branch --> once there is any commit made to the branch, open a DRAFT PR. This way, it is easier to see who is working on what.
5. Comment review comments: I reviewed the code in main and everything looks good to me.
6. Very good branch naming convention!
7. Very good commit message convention!
