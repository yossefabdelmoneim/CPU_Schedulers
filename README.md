# CPU Schedulers Project

Implementation of various CPU scheduling algorithms with JUnit tests.

## Project Structure

```
CPU_Schedulers/
├── src/                    # Source code and test files
│   ├── Process.java
│   ├── SJFScheduler.java
│   ├── RoundRobin.java
│   ├── PriorityScheduler.java
│   ├── AG_Scheduling.java
│   ├── TestRunner.java     # Manual test runner
│   └── *Test.java          # JUnit test classes
├── test_cases_v3/          # Test case JSON files
├── lib/                    # JAR dependencies (legacy)
└── pom.xml                 # Maven configuration
```

## Requirements

- Java 8 or higher
- Maven 3.6+ (for Maven builds)

## Building with Maven

### Compile the project:
```bash
mvn clean compile
```

### Run all tests:
```bash
mvn test
```

### Run a specific test class:
```bash
mvn test -Dtest=AGTest
mvn test -Dtest=SJFTest
mvn test -Dtest=RRTest
mvn test -Dtest=PriorityTest
```

### Run the TestRunner manually:
```bash
mvn exec:java -Dexec.mainClass="TestRunner"
```

### Package as JAR:
```bash
mvn package
```

## IDE Support

### IntelliJ IDEA
1. Open the project (File → Open → select project folder)
2. IntelliJ will automatically detect `pom.xml` and import as Maven project
3. Wait for Maven to download dependencies
4. Run tests by right-clicking on test classes or methods

### VSCode
1. Install "Extension Pack for Java" (includes Maven support)
2. Open the project folder
3. VSCode will automatically detect Maven project
4. Use the Test Explorer or click "Run Test" above test methods

### Eclipse
1. File → Import → Maven → Existing Maven Projects
2. Select the project folder
3. Run tests via Run → Run As → JUnit Test

## Test Results

The project includes comprehensive JUnit tests:
- **AGTest**: 6 tests for Adaptive General scheduler
- **SJFTest**: 6 tests for Shortest Job First scheduler
- **RRTest**: 6 tests for Round Robin scheduler
- **PriorityTest**: 6 tests for Priority scheduler

All tests should pass when running `mvn test`.

## Dependencies

Managed by Maven (`pom.xml`):
- JUnit 4.13.2
- Hamcrest Core 1.3

## Manual Testing

You can also run tests manually using `TestRunner.java`:
```bash
java -cp "target/classes;target/test-classes" TestRunner
```

## Notes

- Test JSON files are automatically copied to `target/test-classes` during build
- The project uses Java 8 source/target compatibility
- All source files are in the `src` directory (non-standard Maven structure, but configured in `pom.xml`)

