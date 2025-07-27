# ngo-nabarun-test

This project contains automated tests for the Nabarun application using Cucumber, JUnit, and Selenium.

## Project Structure
- `src/test/` - Test source code and step definitions
- `src/main/` - Main code (if any)
- `target/` - Generated test reports and build artifacts
- `pom.xml` - Maven configuration and dependencies

## Key Technologies
- **Cucumber**: BDD framework for writing feature files and scenarios
- **JUnit**: Test runner and assertions
- **Selenium**: Browser automation for UI tests
- **Allure**: Test reporting

## How to Run Tests
1. Ensure Java 17+ and Maven are installed.
2. Navigate to the project directory:
   ```sh
   cd ngo-nabarun-test
   ```
3. Run tests with Maven:
   ```sh
   mvn clean test
   ```

## Test Reports
- After running tests, reports are generated in the `target/` directory:
  - `cucumber.html`, `cucumber.json`, `cucumber.xml`: Cucumber reports
  - `allure-results/`: Allure report data

## Configuration
- Dependencies and plugins are managed in `pom.xml`.
- Cucumber and JUnit platform are used for running scenarios.
- Allure integration for enhanced reporting.

## Troubleshooting
- Check `logs/test.log` for detailed test execution logs.
- Ensure all dependencies are downloaded by running `mvn clean install` if you encounter issues.

## Contribution
Feel free to add new feature files, step definitions, or improve existing tests. Pull requests are welcome.

---
For more details, refer to the documentation of [Cucumber](https://cucumber.io/), [JUnit](https://junit.org/), [Selenium](https://www.selenium.dev/), and [Allure](https://docs.qameta.io/allure/).
