# Practice 1 - Data Capture from External Sources
- **Name**: Elisa-Marie Breeze Rebstock
- **Subject**: Desarrollo de Aplicaciones para Ciencia de Datos
- **Course**: 2nd Course, group 43
- **Degree**: Ciencia e Ingeniería de Datos
- **School**: Escuela de Ingeniería Informática
- **University**: Universidad de Las Palmas de Gran Canaria

## Summary of Functionality

The main idea of this project is to take data from a public and free REST API, which in this case is https://openweathermap.org/api. This service provides a free plan with 1000 daily requests for current, historical and future weather data predictions.

The objective is to create a Java application that queries the service API periodically every 6 hours during 5 days to obtain the weather forecast predictions for the next 5 days at 12am for each of the 8 Canary Islands. 

The obtained data is then saved in a SQLite database, where each island has its own table, with a separate entry for each of the next 5 days, storing the date and time for the prediction with the temperature, the precipitation probability, the humidity, the cloud cover percentage and the wind speed.
The Data Base will be updated every 6 hours, and it will only change the information already saved if there is a change in a prediction, or if a new date is added.

## Resources Used
### Development Environments
For this project I have used the IntelliJ IDEA Integrated Development Environment, developed by JetBrains, including Java code for all the functionalities exposed above. The code also uses libraries like Gson for JSON Parsing, Jsoup for web scraping and SQLite for all the database operations.
### Version Control Tools
For version control, mainly Git was used for tracking changes and saving the versions created during the development.
### Documentation Tools
The main documentation tool used in this project is Markdown, which is a lightweight markup language that is easy to read and write.

## Design

### Design Patterns and Principles

### Class Diagram

### Dependency Relationships

## Usage Instructions
This program uses the java version 17. To run the code properly, you must insert your apikey and the path to the Data Base, through arguments in the Main class. 
With this, the only thing left to do is to run the program and see how the weather data is saved in the Data Base for each island. If there has been a change, or a new day has been added to the forcast, it will update the Data Base and insert the new data. If it's the same, the Data Base will stay the same.
