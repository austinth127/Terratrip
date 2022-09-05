FROM openjdk:16 AS build

# Install xargs
RUN microdnf install findutils

# Possible to execute the mave bom build here? Would need to dowload maven...

WORKDIR /build

COPY . .
RUN ./gradlew build --no-daemon -p .

FROM openjdk:16
WORKDIR /app
COPY --from=build /build/build/libs/build-1.0-SNAPSHOT.jar app.jar

# Running the app
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
