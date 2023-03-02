# Fun7 REST API

### Prerequisites

* Java 17 or newer
* Docker

### Docker

1. Open a terminal and navigate to the  `/etc/fun7-docker` directory where the docker-compose.yml
   file is located.
2. Run the following command to start the MongoDB container and the data seeder container:

```
docker compose up -d
```

3. Wait for the containers to start up. You can check the status of the containers using the
   following command:

```
docker compose ps
```

4. Once the containers are running, you can access the MongoDB instance
   at `mongodb://localhost:27017`. There should hopefully be some seed users data already in the DB.
5. To stop the containers, run the following command:

```
docker compose down
```

### Spring Boot

1. Open a terminal and navigate to the root directory of the project, where the maven wrapper
   executable `mvnw` is located.
2. Run the following command to build the project:

```
./mvnw clean install
```

3. Run the following command to start the Spring Boot application:

```
./mvnw spring-boot:run
```

### Testing

To run the tests for the application, navigate to the root directory of the project and run the
following command:

```
./mvnw verify
```

This will run the unit and integration tests for the application, and generate a report with the
results.

Some integration tests are using testcontainers and spin up a local MongoDB docker image for the
data repository test slice.

For the ads client integration test, WireMockServer is used and port `35082` needs to be accessible
since it is statically
allocated. This may cause the test to fail, so beware.

# Usage

The REST API can be accessed at `http://localhost:8080` by default.

The API provides endpoints for user management, customer support availability, multiplayer
availability,
and ads availability. The documentation for each endpoint can be found in the Swagger UI, which by
default
is accessible at `http://localhost:8080/swagger-ui.html`.

The REST API also connects to MongoDB, which is deployed in Docker on port `27017`.

# Assumptions

I think I've only made assumptions for the Multiplayer service part, so here they are:

1. The availability of the multiplayer service is dependent on the physical location of the server
   infrastructure,
   so I used the time zone of the user as a proxy for their location, since we don't have any access
   to other location services. Specifically, I assumed the user is in the US if their time zone
   matches one of the time zones associated with the US. This also means, for simplicity's sake,
   that the multiplayer feature is available to all users in the continental US, as well as Alaska
   and Hawaii.

2. I also extended this assumption by using the country code for validating the user's location,
   assuming
   the code represents the user's country that they have set up in their account. As a result, I
   approved
   access to the multiplayer feature to users with the "US" country code, while denying access to
   users from other countries.

3. To determine if a user is a skilled player, the API uses a field called "playCount" in the
   database,
   which tracks the number of games played by the user. The assumption is that a skilled player
   has played a certain number of games, but it was unclear to me how this number
   should be determined/incremented based on API calls, since none of the API calls seem to fit in
   my opinion and incrementing a counter on the Check Services API seemed unintuitive.
   So I've made an assumption that made it quite a lot easier, that another service is responsible
   for tracking/updating
   this playCount number and I only need to read from it.

# General notes

The architecture is pretty straight forward. I've opted for a layered architecture.
The packages are structured in a "feature-first" way. Some configuration is externalized, but most
of it
is statically assigned in code due to it just being quicker to develop. E.g. customer support
operating
hours/location/timezone could be a configuration or taken from some other source, same for
multiplayer minimum number of games, etc.