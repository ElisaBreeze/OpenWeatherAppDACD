# Practice 1 - Data Capture from External Sources
- **Name**: Elisa-Marie Breeze Rebstock
- **Subject**: Desarrollo de Aplicaciones para Ciencia de Datos
- **Course**: 2nd Course 2023-2024, group 43
- **Degree**: Ciencia e Ingeniería de Datos
- **School**: Escuela de Ingeniería Informática
- **University**: Universidad de Las Palmas de Gran Canaria

## Summary of Functionality

The main idea of this project is to take data from a public and free API REST, which in this case is https://openweathermap.org/api. This service provides a free plan with 1000 daily requests for current, historical and future weather data predictions.

The objective is to create a Java application that queries the service API periodically every 6 hours during 5 days to obtain the weather forecast predictions for the next 5 days at 12am for each of the 8 Canary Islands. 

The obtained data is then saved in a SQLite database, where each island has its own table, with a separate entry for each of the next 5 days, storing the date and time for the prediction with the temperature, the precipitation probability, the humidity, the cloud cover percentage and the wind speed.
The Data Base will be updated every 6 hours, and it will only change the information already saved, if there is a change in a prediction or if a new date is added.

## Resources Used
### Development Environments
For this project I have used the IntelliJ IDEA Integrated Development Environment (Community Edition), developed by JetBrains, including Java code (version 17) for all the functionalities exposed above. The code also uses libraries like Gson for JSON Parsing, Jsoup for web scraping and SQLite for all the database operations.
### Version Control Tools
For version control, mainly Git was used for tracking changes and saving the versions created during the development.
### Documentation Tools
The main documentation tool used in this project is Markdown, which is a lightweight markup language that is easy to read and write, and to create the class diagram, StarUML was used.

## Design

### Design patterns
In this project, there are two obvious design patterns: 
- The class WeatherController follows a pattern called Dependency Injection, because it receives instances of the classes WeatherProvider and SqliteWeatherStore through their constructors
- The use of the Timer in the runTask method in the WeatherController class, could be viewed as an Observer Pattern, as it's a scheduled task which is observed periodically at fixed time intervals

### Design principles
They are important, because they provide guidelines for the creation of a maintainable, robust and scalable program, and increase the quality of it. This project includes some SOLID principles, like for example:
- Single responsibility Principle: The class Location has a single responsibility, which is the representation of the location information; The class Weather represents the weather data information that can be represented
- Open-Closed Principle: This principle says that the program is open for extension but closed for modification, which means that you can add new functions without changing the code that is there already, which is used in the WeatherProvider and WeatherStore classes, and changes can be done by adding interfaces. Apart from these two classes, the WeatherController class can also be extended without changing its code
- Liskov Substitution Principle: Both interfaces WeatherProvider and WeatherStore can be substituted without affecting or breaking the programs code
- Interface Segregation principle: Both interfaces WeatherProvider and WeatherStore provide specific methods related to their responsibilities
- Dependency Inversion principle: The class WeatherController depends on both WeatherProvider and WeatherStore interfaces, which are abstractions

### Class Diagram
The class Diagram for this project is shown below:

![Class Diagram Image](DiagramaUML.png)

### Dependency Relationships
Dependency Relationships refers to the connections between classes and its components. There are a few cases of these relationships which have been used: 
- The class WeatherProvider and SqliteWeatherStore are independent, but are used together in WeatherController, who depends on both of these interfaces, which allows for more flexibility and a more modular design
- SqliteWeatherStore depends on the class Location when obtaining information about the locations in the creation of the tables in the Data Base
- The class OpenWeatherMapProvider depends on the Jsoup library
- In the class WeatherController, there is a dependency with the method locationLoader

## Usage Instructions
This program uses the java version 17. To run the code properly, you must insert your apikey and the path to the Data Base through arguments in the Main class. 
With this, the only thing left to do is to run the program and see how the weather data is saved in the Data Base for each island. If there has been a change, or a new day has been added to the forecast, it will update the Data Base and insert the new data. If there haven't been any changes, the Data Base will stay the same.
It is important to know that this program is designed to run the task every 6 hours during 5 days, storing the data in the Data Base. Once the 5 days are over, it will cancel the task automatically.
