# README

This is an old project built for a school assignment, but I did so with the intention of making it applicable to my father's work as an independent insurance agent. Having helped him with his work, I recognised the following issues he faced:
- Keeping track of clients data in numerous Excel files were outdated and inefficient
- Large quantity of new & old client data will make newer, relevant information become obscured
- Making advanced payments for friends/families made it confusing to track collection of payment
- Tracking referrals with partnering insurance agents were tedious
- Data being stored offline in his PC made it harder for him to tend to clients when he is not as his desk

Hence, the goal of this database is to collate all this information in one centralised location for easier management and tracking of accounts. Furthermore, it includes a online form that allows interested insurance buyers to submit their information directly to the database such that my father will not be tied down to his desk when tending to potential clients.

#### NOTE:
- All data used in this project are randomly generated
- This project serves as a proof of concept and thus, no encryption code was written for clients data

#### UPDATE:
- Project needs to be refractored to work with more recent technologies (e.g. PHP has been removed in macOS Monterey)
- Troubleshooting needed to ensure all components of the project works seemlessly

## Explanation of Use
### SQL scripts and "Test Data" folder
| File/Folder | Purpose |
| ------ | ------ |
| DDL script.sql | sets up the database and its tables |
| DML Data Population Script.sql | load data in csv files in "Test Data" folder into the database |
| DML script.sql | some queries written to simulate potential use of this data management application |

### Java Application Folder
Helper Java application to read CSV files and populate the SQL database when starting up the program.

### Clients Database
Main Java application that allows user to interact with the database. Some features are:
- Query & filter through database to search for specific information
- Shows all expiring policies
- Shows all transactions made by the user and any outstanding loans to are dued to the user
- Adds new client/data entry to database


### Web Application Folder
This folder contains the HTML, php, css, and js files to run the web application.
The website allows a user to create an account, enter all the details necessary for an insurance quotation, and update them accordingly in the future. This information will be saved in the SQL database so that it can be accessed from the main Java application.


### Data Models and Schematics Folder
Drawings of the database ER model and Relation schema.
