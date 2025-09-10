# Build System Migration

This project has been migrated from Apache Ant (build.xml) to Gradle (build.gradle).

## Original Ant Build

The original build.xml provided:
- `ant jar` - Compile sources and create jar
- `ant clean` - Clean build artifacts

## New Gradle Build

The new Gradle build provides equivalent functionality:
- `./gradlew jar` - Compile sources and create jar (default task)
- `./gradlew clean` - Clean build artifacts
- `./gradlew build` - Full build with tests (standard Gradle)

## Build Features

Both build systems:
1. Compile Java sources from `src/java` and `src/os_stub` directories
2. Create `fw-jwebbrowser.jar` in the `dist/` directory
3. Copy native libraries (`JIExplorer.dll`, `libJWebKit.dylib`) to `dist/`
4. Handle compilation with internal Java APIs using appropriate compiler flags

## Usage

```bash
# Build the project
./gradlew jar

# Clean build artifacts
./gradlew clean

# Show available tasks
./gradlew tasks
```

## Migration Benefits

- Standard Gradle wrapper for consistent builds across environments
- Better dependency management capabilities
- Integration with modern IDEs and CI/CD systems
- Extensible build configuration for future enhancements

## Notes

The project has some Java compatibility issues with newer JVM versions due to usage of internal APIs that have been restricted since Java 9. These compilation issues existed in the original Ant build and are preserved in the Gradle build to maintain equivalent functionality.