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

We store CSV data as a List of String Lists and CSV headers as a List of Strings. 

## Runtime/Space Optimizations You Made

For a user taking advantage of the Searching for Broadband Coverage in a Given U.S. State and County functionality, the first step is converting a user's STATE string into the State ID used by the Census API. Rather than repeating queries to the list of state names and state IDs each time a user requests a broadband value in a new state, we store this information for the life of the server. We store this as a HashMap from State Names to State IDs. 

# Errors/Bugs
No known bugs. 

# Tests

// TODO WRITE TESTS

# How To…
## Run the Tests You Wrote/Were Provided

// TODO WRITE TESTS

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

http://localhost:2020/broadband?state=California&county=Kings%20County,%20California

Louisiana refers to its county-equivalents as Parishes, so, for example:

http://localhost:2020/broadband?state=Louisiana&county=Jefferson%20Parish,%20Louisiana

Depending on the choice of browser, each space may need to be replaced with %20.

### Bound box query 

The user is able to filter the geoJSON by providing the filepath to the JSON, the min lat and long, and the "step" parameter. The lat and longs are used to create the minimum values for the box, and the step value To query the bound box on the backend, 

# API Key
d9ab70f1962723dec8d9c8a8ffde26a35fe97524

# FRONTEND DOCUMENTATION

# Design Choices

We chose to have three main classes create the front end of the projcet: input, history, and submit. History is in charge of updating the history of the queries, while input is responsible for handling the input and adding to the history. This is done using a shared state in the REPL class. Submit is a typescript function that handles the query and sends the query to the backend.

mockedJson is responsible for mocking the data and returning it to submit. This is done by creating three different hashmaps, and changing which hashmap is currently being used depending on which (mocked) file is loaded. 

## Relationships Between Classes/Interfaces

The general structure for some classes was explained above, but we explain here further:

App - Responsible for creating the "Mock" header and instantiating the REPL class below.

REPL - Manages shared states between input and history, while also formatting error messaging and mode notification.

REPL History - Responsible for formatting the history array into html. Done by parsing an array of either strings of string double arrays into strings or tables, respectively.

REPL Input - Responsible for recieving user query, sending to the backend, and then recieving the response and sending to history.

Controlled input - Used to update the query string so that upon pressing submit, the query is accurately recieved.

Submit - function which connects the front end to the backend. Handles the query by connecting to the response of the backend and sends the handles the promise to the input class. 

MockedJson - Mocks load, view, and search using double string arrays and distinct hashmaps representing distinct files.

## Data Structures

We store the maps of possible view and search queries associated with the different mocked files in the mocked json class.

We use various states in REPL, REPL History, and REPL input to keep the communication between these classes linked with React.

## Runtime/Space Optimizations You Made

One algorithmic improvement made was to store the functions in a hashmap rather than create branches of if statements to 
decide which function to call. We did not think of this, it was advised to do this in the handout. 

# Errors/Bugs
No known bugs. 

# Tests

Our testing suite utilizes both unit testing and integration testing using playwright to test our mock. We wrote intergration tests to ensure that the correct elements were displayed on the UI webpage at the correct times. We did this by testing a series of commands, like loading, switching mode, viewing, searching, switching mode again, etc. We had multiple tests similar to this, each testing a different series of commands. We also tested to make sure that correct error messages were displayed on the screen when incorrect or invalid commands were entered. Ultimately, these tests also tested the state and dependency injection functionality of our program just by virtue of how the tests were setup. Finally, unit testing was implemented within the integrations tests, as individual functions were called within the integration tests. WE tested the integration of the backend with both real calls to the API and mock calls using a switch button. 

# How To…
## Run the Tests You Wrote/Were Provided

Running the tests first involve installing playwright. This can be done through running the "npm init playwright@latest" in terminal. Once playwright is installed, there are two ways to run the tests. (1) Run "npx playwright test" in the terminal. This will run the tests in the terminal. (2) Run "npx playwright test --ui" in the terminal. This will open a new application that simiulates the application running with a ui. The latter method is what was primarily used to test this program, so that is our personal recommendaation when running the tests.

## Build and Run Your Program

Our program supports the following functionalities:

Loading local CSV files
Viewing local CSV files
Searching local CSV files
Querying broadband data from the API
Switching between brief and verbose modes
NOTE: To support Viewing or Searching local CSV files, a file must first be loaded.

To run any of the functionalities, start the program using "npm start" in the terminal and navigate to the local server, while also starting the backend server in Intellij. 

### Loading Local CSV Files

Loading a CSV places its contents, and headers if applicable, in memory. Loading a CSV enables the CSV to be viewed or searched.

Loading a CSV requires three arguments: the first is the call to load the file, which is load_file, the second is a filepath to a CSV file, and the third is either true or false depending on if there are or aren't headers. We use the path from the content root for queries. Some sample file paths and queries are as follows: 

load_file data/census/dol_ri_earnings_disparity.csv true

load_file data/custom/noheaders.csv false

load_file data/census/postsecondary_education.csv true

### View Local CSV Files

Viewing a CSV displays a previously loaded CSV's data contents and headers, if the loaded CSV has headers. The query format for viewing a CSV is:

view

### Searching Local CSV Files

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

### Searching for Broadband Coverage in a Given U.S. State and County

This server also supports returning the percent of households in a given U.S. state and county that have access to broadband. The program queries the American Community Survey (ACS) 1-Year API for a given state and county, returning the value of the S2802_C03_022E variable, the "Estimate!!Percent Broadband Internet Subscription!!With a computer!!Total population in households!!EMPLOYMENT STATUS!!Civilian population 16 years and over!!Not in labor force" if that county is present in the ACS 1-Year Data. Counties with a population below 65,000 are suppressed for data privacy reasons, so the variable is not returned for them. 

Note: Similar to search, you must replace spaces that do not distinguigh command arguments with underscores. In order to query a given STATE and COUNTY, the following backend commands with their respective front end queries are shown: 

http://localhost:3232/broadband?state=STATE&county=COUNTY
broadband STATE COUNTY

Note that the COUNTY value must be the full name of a county. For example, to retrieve the broadband value from the backend for Kings County, California, STATE is California and COUNTY is Kings County, California, as displayed below. For the front end query, this is not necessary as shown below:

http://localhost:3232/broadband?state=California&county=Kings%20County,%20California
broadband California Kings_County

Louisiana refers to its county-equivalents as Parishes, so, for example:

http://localhost:3232/broadband?state=Louisiana&county=Jefferson%20Parish,%20Louisiana
broadband Louisiana Jefferson_Parish

Depending on the choice of browser, each space may need to be replaced with %20.

# API Key
d9ab70f1962723dec8d9c8a8ffde26a35fe97524
