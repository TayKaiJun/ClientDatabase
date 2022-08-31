# README

This is an old project built for a school assignment, but I did so with the intention of making it applicable to my father's work as an independent insurance agent. Having helped him with his work, I recognised the following issues he faced:
- Keeping track of clients data in numerous Excel files were outdated and inefficient
- Large quantity of new & old client data will make newer, relevant information become obscured
- Making advanced payments for friends/families made it confusing to track collection of payment
- Tracking referrals with partnering insurance agents were tedious
- Data being stored offline in his PC made it harder for him to tend to clients when he is not as his desk

Hence, the goal of this database is to collate all this information in one centralised location for easier management and tracking of accounts. At the same time, it aims to provide ease of access (both offline and online) to whatever information is needed.

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

## Web Application Folder
This folder contains the HTML, php, css, and js files to run the web application.

## Java Application Folder
Code to generate a standalone Java exe program that can access the SQL database.

## Data Models and Schematics Folder
Drawings of the database ER model and Relation schema.
