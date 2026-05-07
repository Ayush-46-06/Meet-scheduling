# Step 1: Build the application
FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /app

# Copy the maven wrapper and pom file
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Make the maven wrapper executable
RUN chmod +x mvnw

# Download dependencies (this will be cached)
RUN ./mvnw dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN ./mvnw clean install -DskipTests

# Step 2: Create the runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/MeetSchedulling-0.0.1-SNAPSHOT.jar app.jar

# Expose the port (Render will override this with $PORT)
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT:8080}"]
