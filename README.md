# Market Spread Reporter
This program automatically creates a report every minute of the state of all markets on Kanga Exchange.

## Usage
### Setup
Make sure you have Gradle installed. Then, inside the main directory build the project:
```shell
./gradlew build   # Unix
```
```shell
gradle build      # Microsoft Windows
```

### Run
To run the program use these:
```shell
./gradlew run     # Unix
```
```shell
gradle run        # Microsoft Windows
```

### State of the app
Currently the app will create a report a minute after the previous one. All the reports are generated in the `app` directory.
