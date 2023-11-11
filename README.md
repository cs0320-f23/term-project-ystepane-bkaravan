# maps-bkaravan-ibrauns

Project Name: Maps

Team Members and Contributors 

Ilan Brauns - ibrauns

Bohdan Karavan - bkaravan

Contributions:
We both contributed roughly equally -- most of the code was written asyncrounously. 

Bohdan wrote a greater portion of user story 3, while Ilan wrote a greater part of user story 4

Total Estimated Time for Completion
12 hours each 

Repository Link
https://github.com/cs0320-f23/maps-bkaravan-ibrauns

# BACKEND DOCUMENTATION

# Design Choices

We choose to have two main files for searching: bound box and area search, representing user stroies 3 and 4 respectively. We also kept our broadband data search from previous sprints. 

## Relationships Between Classes/Interfaces

The Main class creates the Server, and has it listen on Port 2020. It also calls the Handler functions in bound box, area search, and broadband. Our functionality for server is still intact for this project should it need to be used. 

The bound box class represents user story 3 for this project. The user can submit a query (described below) and recieve data only in that given bound box. 

The area search class represents user story 4 for this project. The user can submit a file and keyword (described below) and receive a highlighted portion of the map with that keyword as part of the search description. 

The broadband class is from previous project, and connects to the API to get broadband data for a given state and county. 

## Data Structures
We store a Map with both keys and values being strings to represent the correspondence between state names and state IDs from the CensusAPI. 

We store CensusData and Geolocations as records. 

## Runtime/Space Optimizations You Made

For a user taking advantage of the Searching for Broadband Coverage in a Given U.S. State and County functionality, the first step is converting a user's STATE string into the State ID used by the Census API. Rather than repeating queries to the list of state names and state IDs each time a user requests a broadband value in a new state, we store this information for the life of the server. We store this as a HashMap from State Names to State IDs. 

# Errors/Bugs
No known bugs. 

# Tests

We have numerous tests documenting the functionality of broadband and other components of past projects. For this project's functionality, we have random testing and unit testing for bound box and area search in the MapTest file. 

# How To…
## Run the Tests You Wrote/Were Provided

Navigate the map test file and run the JUnit tests. 

## Build and Run Your Program

Our program supports 3 functionalities: 

1. Querying broadband data and other functionality of past projects
2. Finding data in a given bound box (user story 3)
3. Finding areas with a given keyword in the description (user story 4)

To run any of the 4 functionalities, navigate to Server.java and run Server.java from IntelliJ. From there, enter the appropriate query, as detailed below, into the URL bar of your Internet browser of choice (e.g. Google Chrome, Microsoft Edge, Safari, etc.). 

### Searching for Broadband Coverage in a Given U.S. State and County

This server also supports returning the percent of households in a given U.S. state and county that have access to broadband. The program queries the American Community Survey (ACS) 1-Year API for a given state and county, returning the value of the S2802_C03_022E variable, the "Estimate!!Percent Broadband Internet Subscription!!With a computer!!Total population in households!!EMPLOYMENT STATUS!!Civilian population 16 years and over!!Not in labor force" if that county is present in the ACS 1-Year Data. Counties with a population below 65,000 are suppressed for data privacy reasons, so the variable is not returned for them. 

In order to query a given STATE and COUNTY, the following URL should be typed into the URL bar of an internet browser:  

http://localhost:2020/broadband?state=STATE&county=COUNTY

Note that the COUNTY value must be the full name of a county. For example, to retrieve the broadband value for Kings County, California, STATE is California and COUNTY is Kings County, California, as displayed below:

http://localhost:2020/broadband?state=California&county=Kings

Depending on the choice of browser, each space may need to be replaced with %20. Additionally, "Rhode Island" can also be queries by just typing "rhode" and other two name states. 

### Bound box query 

The user is able to filter the geoJSON by providing the filepath to the JSON, the min lat and long, and the "step" parameter. The lat and longs are used to create the minimum values for the box, and the step value. To query the bound box on the backend, the following query: 

http://localhost:2020/boundbox?minlat=35&minlon=35&step=10 

would create a box from 35-45 for both latitude and longitude. When step = 0, the entire dataset is returned. 

### Area search query

The user is able to search a key word from an area description from a given file path. The query paramters are the filepath and the keyword. The following query can be made: 

http://localhost:2020/areasearch?filepath=sample/file/path&keyword=schools

This returns the feature collection with edited holc-grades to "H", which is used in the front end to highlight an appropriate color. 

# API Key
d9ab70f1962723dec8d9c8a8ffde26a35fe97524

# FRONTEND DOCUMENTATION

# Design Choices

The front end contains numerous implementation from past projects that remain such as CSV functionality. The new additions to the project are the map box class and the overlays.ts file. The map box class is responsible for setting up the map, identical to the gear up code besides one small difference to the overlays. The mapbox class takes in a state which dictates what file gets read by the overlay data. This is used to switch between files for our stakeholder and also to submit queries which highlight the map. The overlays.ts file deals with the query to the backend which gets the area search with "H" as the holc_grade and highlights the map

## Relationships Between Classes/Interfaces

The general structure for some classes was explained above, but we explain here further:

App - Responsible for creating the "Mock" header and instantiating the REPL class below.

REPL - Manages shared states between input, history, and MapBox, while also formatting error messaging and mode notification.

REPL History - Responsible for formatting the history array into html. Done by parsing an array of either strings of string double arrays into strings or tables, respectively.

REPL Input - Responsible for recieving user query, sending to the backend, and then recieving the response and sending to history.

REPL Function - Responsible for the logic of the REPL functions, including the area search

## Data Structures

We use a map to house our REPL functions in the class. 

We use various states in REPL, REPL History, and REPL input to keep the communication between these classes linked with React.

## Runtime/Space Optimizations You Made

One algorithmic improvement made was to store the functions in a hashmap rather than create branches of if statements to 
decide which function to call. We did not think of this, it was advised to do this in the handout. 

# Errors/Bugs
No known bugs. 

# Tests

Appropriate Playwright tests for the behavior of the front end web app were written.

Different shapes of command and result were tested. From different reachable states, tests were performed.

# How To…
## Run the Tests You Wrote/Were Provided

To run the tests, we used the GearUp guide and worked with Playwright. To build and run a test, input npx playwright test which runs tests headless (does not open the browser) in the background. You can also use npx playwright show-report which gives more detailed information on test progression.

npx playwright test --ui opens a UI to explore what the web app looks like as the test is occurring. There, a user can navigate between App.spec.ts and mockTests.spec.ts, where the two main testing suits are located.

## Build and Run Your Program

Our program supports the following functionalities:

1. All previous project functionality
2. The map
3. Searching an area based on a keyword

To run any of the functionalities, start the program using "npm start" in the terminal and navigate to the local server, while also starting the backend server in Intellij. 

### RUN ALL PREVIOUS FUNCTIONALITY

This does not need to be read for the purposes of the map, but still documented as funcitonality. 

Loading a CSV places its contents, and headers if applicable, in memory. Loading a CSV enables the CSV to be viewed or searched.

Loading a CSV requires three arguments: the first is the call to load the file, which is load_file, the second is a filepath to a CSV file, and the third is either true or false depending on if there are or aren't headers. We use the path from the content root for queries. Some sample file paths and queries are as follows: 

load_file data/census/dol_ri_earnings_disparity.csv true

load_file data/custom/noheaders.csv false

load_file data/census/postsecondary_education.csv true

### View Local CSV Files

Viewing a CSV displays a previously loaded CSV's data contents and headers, if the loaded CSV has headers. The query format for viewing a CSV is:

view

There are three ways to search a loaded CSV for a given keyword: 

0: Search all columns for instances of a keyword.
1: Search a single column specified by column index
2: Search a single column specified by column name (REQUIRES LOADED DATA TO HAVE HEADERS)

Each search returns all rows that contain that keyword in either all columns (type 0 search) or one column (type 1 and 2 search). Note: if you would like to query something with a space (ex. keyword: Rhode Island), this should be input with an underscore instead of a space (Rhode_Island). Spaces should only be used to distinguish between arguments. 
Some sample backend queries followed by their respective front end queries are as follows:

0: 

search_type: 0
keyword: the keyword to search ALL columns for

EXAMPLE:
http://localhost:3232/searchcsv?keyword=RI&search_type=0
search RI 0

1: 

search_type: 1
col_index: the column index to be searched
keyword: the keyword for search ONE column with specified column index in

EXAMPLE:
http://localhost:3232/searchcsv?keyword=RI&col_index=0&search_type=1
search RI 1 0

2:

search_type: 2
col_name: the column header name to be searched
keyword: the keyword for search ONE column with specified column name in

EXAMPLE:
http://localhost:3232/searchcsv?keyword=RI&col_name=race&search_type=2
search RI 2 race

# Searching for Broadband Coverage in a Given U.S. State and County

This server also supports returning the percent of households in a given U.S. state and county that have access to broadband. The program queries the American Community Survey (ACS) 1-Year API for a given state and county, returning the value of the S2802_C03_022E variable, the "Estimate!!Percent Broadband Internet Subscription!!With a computer!!Total population in households!!EMPLOYMENT STATUS!!Civilian population 16 years and over!!Not in labor force" if that county is present in the ACS 1-Year Data. Counties with a population below 65,000 are suppressed for data privacy reasons, so the variable is not returned for them. 

Note: Similar to search, you must replace spaces that do not distinguigh command arguments with %20. In order to query a given STATE and COUNTY, the following backend commands with their respective front end queries are shown: 

http://localhost:3232/broadband?state=STATE&county=COUNTY
broadband STATE COUNTY

Note that the COUNTY value must be the full name of a county. For example, to retrieve the broadband value from the backend for Kings County, California, STATE is California and COUNTY is Kings County, California, as displayed below. For the front end query, this is not necessary as shown below:

http://localhost:3232/broadband?state=California&county=Kings%20County,%20California
broadband California Kings%20County

## Area Search

To highlight areas on the map in purple based on a keyword in the area description:

highlight filepath keyword

A more direct example: 
highlight data/GeoJSON/geodata.json schools

will highlight much of the map purple because schools is a very common description 

## Reflection

Our finished Maps product is built using many systems: programming languages, development environments, software packages, hardware, etc. 
Here is rough list of things we used while working on Maps: 

• React: a JavaScript library for building user interfaces, developed by Facebook and a community of individual developers and companies.

• TypeScript: a programming language that extends JavaScript by adding types, developed and maintained by Microsoft.

• Mapbox: a platform for creating and displaying maps and location data, developed by Mapbox Inc.

• Spark: a micro web framework for Java and Kotlin, developed by Per Wendel, David Åse, and contributors.

• Playwright: a Node.js library for end-to-end testing of web applications, developed by Microsoft.

• JUnit: a unit testing framework for Java, developed by Kent Beck, Erich Gamma, and contributors.

• HTML: a markup language for creating web pages and web applications, developed and maintained by the World Wide Web Consortium (W3C) and the Web Hypertext Application Technology Working Group (WHATWG).

• CSS: a style sheet language for describing the presentation of web pages, developed and maintained by the W3C.

• GitHub: a platform for hosting and collaborating on software development projects, developed by GitHub Inc.

• IntelliJ IDEA: an integrated development environment for Java and other languages, developed by JetBrains.

• Chat GPT: a web-based text-generator that allowed us to use the chat-bot AI to debug and figure out specific syntax questions.

• Stackoverflow: a website for developers to learn, share, and solve programming problems, powered by a community of experts and enthusiasts. We used it to resolve the most common issues and squigglies.

## Sources / Credits

- GearUp, Mock and Server code served as source codes for this project.
- StackOverflow and ChatGPT generated code was used to figure out some frameworks and set up details.
- shared code and ideas with ystepane


# API Key
d9ab70f1962723dec8d9c8a8ffde26a35fe97524
