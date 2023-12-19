# TRAVEL BUDDY

This is the Term Project for CS32 at Brown University.

### _Completed by Julia Stepanenko (ystepane) and Bohdan Karavan (bkaravan)._

The project took about _*30 hours*_ to complete.

Github [repo link](https://github.com/cs0320-f23/term-project-ystepane-bkaravan)!

## About

This project is a web application that combines the knowledge from the previous sprints in CS32. This project is a web application that allows users to create or join rides with other people who are traveling to the same destination, or travel to places they never thought of visiting! The project uses Google Maps to provide functionality for destination search and route calculation. The project aims to provide a convenient, affordable, and eco-friendly way of traveling.

## Program Design

The project uses HTML and CSS to create the user interface, and TypeScript with React to organize components and process events. The project also uses Google Maps, a third-party React framework, to display maps and map-related data. The back end uses Java to run a local server, from which data is retrieved. The project supports various commands and queries that allow the user to interact with the data and visualize it on the map.

We divided our project into two main folders: `front` and `back`. The `front` folder contains the front end code written in React and TypeScript. The `back` folder contains the back end code that uses Java to run a local server, from which data is retrieved. The project supports various commands and queries that allow the user to interact with the data and create or join rides.

The user can create their own ride and add it to the database, join an existing ride, or filter existing rides based on their criteria. The user can also submit a form with a date and time of preferred traveling, as well as the origin and the destination city’s name and coordinates.

The `Main` class creates the server which is listed under port 2020. The server calls the functions from the Handler in date form, user form, create ride, join ride, and filter ride.

The `DateSubmit` class handles the post request from the date form and adds the date and time information to the database.

The `UserSubmit` class handles the post request from the user form and adds the user information to the database.

The `HandleCreate` class handles the get request from the create ride command and creates a new ride object with the given parameters.

The `HandleJoin` class handles the get request from the join ride command and adds the user to the existing ride object.

The `FilterHandler` class handles the get request from the filter ride command and returns the rides that match the given criteria.

### Commands

`create`: a command that allows the user to create a new ride with the given origin, destination, ride type, number of seats, host, and date.

`join`: a command that allows the user to join an existing ride with the given ride ID and guest.

`filter` + `time` /`distance`/`score`/`id`: a command that allows the user to filter the existing rides by the date, distance from the origin/destination entered, ride score or ride id.

`show`: a command that allows the user to see the details of all rides that are in the database.

### Accessibility

To accomadate the project to be more user-friendly, small features were implemented to support its accessability. For example, CapsLock key serves as a shortcut to zoom in the webpage, and Control on either side can serve as a zoom out shortcut.

## Testing Suites

The test suit has not been created to check the functionality of the tool.

The project uses JUnit to write unit tests and integration tests for the back-end server. The tests cover different endpoints and handlers, as well as different scenarios and edge cases.

## Using the Project

To begin, clone the GitHub repository to your local machine.

• Navigate to the back folder and run the `Main` to start the back-end server.

• Navigate to the front folder and run npm start to start the front-end web app.

• Go to localhost:8000 on your browser to see the web app.

• Enter commands in the command prompt and see the results on the web app.

For developers:

After starting the back-end server, navigate to http://localhost:2020https://code-care.com/blog/ride-sharing-app-development/ and enter your requests as appropriate endpoints with queries.

Here are the examples of how to use the page:

• create: To create a new ride, enter create <origin> <destination> <ride type> <number of seats> <host> <date>.

• join: To join an existing ride, enter join <ride ID> <guest>.

• filter: To filter the existing rides, enter filter <origin> <destination> <ride type> <number of seats> <date>.

• show: To see the details of a specific ride, enter show <ride ID>.
Here are the examples of how to use the page:
`broadband`:
To look for a State and a county, nothing needs to be loaded. Just enter `broadband <state> <county>`. Only 3 arguments are allowed. Not complete search is allowed: this means that a user may input "Rhode" as state and "Rhode Island" will be found.

`reload`:
`reload` endpoint was implemented in order to support for clearing the storage and better testing. It doesn't contain any inputs and serves as an endpoint for back end.

`bb`:
`bb` awaits for the following inputs:
filepath, minimum latitude `minlat`, minimum longitude `minlon`, and step `step`.

## Building tests

Appropriate tests for the behavior of the web app were written.

Different shapes of command and result were tested.
From different reachable states, tests were performed.

To run the tests, we used the GearUp guide and worked with Playwright.
To build and run a test, input `npx playwright test` which runs tests headless (does not open the browser) in the background. You can also use `npx playwright show-report` which gives more detailed information on test progression.

`npx playwright test --ui` opens a UI to explore what the web app looks like as the test is occurring. There, a user can navigate between App.spec.ts and mockTests.spec.ts, where the two main testing suits are located.

To work with unit tests on the backend, navigate to the `tests` folder and run the test classes you desire to execute.

## Some usage notes:

The webpage might look different on different devices (colors may differ, fonts, etc)

## Bugs

Some tests might fail once executed simultaneously but they do work when executed one by one later.

Another implementation bug is if the user wants to input any of the arguments as a two-word (or more) argument, they would have to use %20 instead of the spacebar in order for the arguments not to get parsed in a wrong way.

## Future additions

We planned to add Firebase and authentication with Brown emails to our project, but we did not get to implement them due to the lack of resources and time. We are a group of two and we had to prioritize the core features and functionalities of our app.

We hope to add these features in the future to improve the security and usability of our app.

## Sources / Credits

- MAPS code served as source codes for this project.
- StackOverflow and ChatGPT generated code was used to figure out some frameworks and set up details.
- Erica Song was our TA mentor.
- Collaborators: ystepane-bkaravan

#### Thank you for using this tool, hope it helps you find things faster and more conviniently!
