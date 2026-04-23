# Smart Campus RESTful API Implementation Using JAX-RS

**Module:** Client-Server Architectures (5COSC022W)  
**Academic Year:** 2025/26

---

## Overview

This project is a RESTful API developed for the **Client-Server Architectures** coursework using **JAX-RS (Jersey)**, **Apache Tomcat**, **Maven**, and **NetBeans**.

The system models a **Smart Campus** environment where rooms, sensors, and sensor readings are managed through REST endpoints.

The API follows a resource-based design and includes:

- a **discovery endpoint** at `/api/v1`
- **room management** at `/api/v1/rooms`
- **sensor management** at `/api/v1/sensors`
- **nested sensor reading management** at `/api/v1/sensors/{sensorId}/readings`
- **custom exception handling** with structured JSON error responses
- **request and response logging** using JAX-RS filters

The project uses **in-memory data structures only** (`HashMap`, `ArrayList`) and does **not** use a database, in accordance with coursework requirements.

---

## API Design Summary

### Base Path

All endpoints are exposed under:

```text
/api/v1
```

### Main Resources

#### 1) Discovery

- `GET /api/v1`
- Returns API metadata such as version, admin contact, and available primary resources.

#### 2) Rooms

- `GET /api/v1/rooms` → returns all rooms
- `POST /api/v1/rooms` → creates a new room
- `GET /api/v1/rooms/{roomId}` → returns one room by ID
- `DELETE /api/v1/rooms/{roomId}` → deletes a room if it has no assigned sensors

#### 3) Sensors

- `GET /api/v1/sensors` → returns all sensors
- `GET /api/v1/sensors?type=CO2` → filters sensors by type
- `POST /api/v1/sensors` → creates a new sensor after validating that the referenced `roomId` exists

#### 4) Sensor Readings

- `GET /api/v1/sensors/{sensorId}/readings` → returns reading history for a sensor
- `POST /api/v1/sensors/{sensorId}/readings` → appends a new reading and updates the sensor's `currentValue`

---

## Error Handling

The API is designed to be leak-proof and does not expose raw Java stack traces to clients.

Implemented exception mapping includes:

- **409 Conflict** → attempting to delete a room that still has assigned sensors
- **422 Unprocessable Entity** → attempting to create a sensor with a non-existent `roomId`
- **403 Forbidden** → attempting to add a reading to a sensor in `MAINTENANCE`
- **500 Internal Server Error** → unexpected runtime failures handled by a global exception mapper

All errors are returned in structured JSON format.

---

## Technology Stack

- Java
- JAX-RS (Jersey)
- Apache Tomcat 9
- Maven
- NetBeans
- Postman (for testing)

---

## Project Structure

```text
src/main/java
  com.example.smartcampus
    config
    dao
    exception
    filter
    model
    resources

src/main/webapp
  WEB-INF
```

---

## How to Build and Run the Project

### Prerequisites

Make sure the following are installed:

- Apache NetBeans
- Apache Tomcat 9
- Java JDK 8 or compatible
- Maven
- Postman (recommended for testing)

---

### Step-by-Step Instructions

#### 1. Clone the repository

```bash
git clone https://github.com/Ahinthaj/SmartCampusAPI.git
```

#### 2. Open the project in NetBeans

1. Open NetBeans
2. Go to **File → Open Project**
3. Select the project folder

#### 3. Configure Apache Tomcat in NetBeans

1. Open the **Services** tab
2. Right-click **Servers**
3. Choose **Add Server**
4. Select **Apache Tomcat**
5. Browse to your Tomcat installation folder
6. Finish setup

#### 4. Check `pom.xml`

Ensure Maven resolves the required dependencies:

- `jersey-container-servlet`
- `jersey-hk2`
- `jersey-media-json-jackson`

#### 5. Clean and Build the project

1. Right-click the project
2. Click **Clean and Build**

#### 6. Run the project

1. Right-click the project
2. Click **Run**

Tomcat should start and deploy the application.

#### 7. Test the API

Once deployed, the API should be available at:

```text
http://localhost:8080/SmartCampusAPI/api/v1
```

---

## Sample cURL Commands

### 1) Discovery endpoint

```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1
```

### 2) Get all rooms

```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/rooms
```

### 3) Create a new room

```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \
-H "Content-Type: application/json" \
-d "{\"id\":\"BUS-105\",\"name\":\"Business Seminar Room\",\"capacity\":50,\"sensorIds\":[]}"
```

### 4) Get a room by ID

```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/rooms/BUS-105
```

### 5) Delete an empty room

```bash
curl -X DELETE http://localhost:8080/SmartCampusAPI/api/v1/rooms/BUS-105
```

### 6) Get all sensors

```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/sensors
```

### 7) Create a valid sensor

```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
-H "Content-Type: application/json" \
-d "{\"id\":\"OCC-001\",\"type\":\"Occupancy\",\"status\":\"ACTIVE\",\"currentValue\":0.0,\"roomId\":\"SCI-101\"}"
```

### 8) Create a sensor with an invalid room reference

```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
-H "Content-Type: application/json" \
-d "{\"id\":\"TEMP-999\",\"type\":\"Temperature\",\"status\":\"ACTIVE\",\"currentValue\":22.5,\"roomId\":\"UNKNOWN-ROOM\"}"
```

### 9) Filter sensors by type

```bash
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=CO2"
```

### 10) Get reading history for a sensor

```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001/readings
```

### 11) Add a reading to an active sensor

```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d "{\"value\":25.3}"
```

### 12) Attempt to add a reading to a sensor in maintenance

```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/CO2-001/readings \
-H "Content-Type: application/json" \
-d "{\"value\":700.0}"
```

---

## Conceptual Report (Q&A)

### Part 1: JAX-RS Fundamentals

#### Question 1: JAX-RS Resource Class Lifecycle

**Q:** In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.

**A:** By default, in JAX-RS, the lifecycle of a resource class is request-based, which implies creating a new instance of the resource class for each HTTP request received by the application. As far as the runtime is concerned, resource classes are not considered singletons unless otherwise specified. In general, having an instance of a resource class created for each request provides more predictability regarding what happens inside the code and prevents any possible data leaks or unexpected data sharing with client applications.

As far as the storage of data is concerned, all fields stored inside the resource class will lose their values after the HTTP request ends since the corresponding resource class instance will no longer exist. For maintaining data, a developer needs to introduce another component that will keep all shared information within a static structure. This, however, leads to a problem of concurrent access that should be handled via thread-safety mechanisms such as synchronization.

#### Question 2: HATEOAS and Hypermedia in RESTful Design

**Q:** Why is the provision of "Hypermedia" (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

**A:** As defined by HATEOAS (Hypermedia as the Engine of Application State), hypermedia is considered as one of the indicators of the proper RESTful design because it enables dynamic guidance from the server side via including links in the response of the client. Instead of knowing about all potential endpoints beforehand, a client should be offered to navigate around an API using the corresponding links (e.g., next, create, details). The main advantage of such an approach is that the application will become self-descriptive and new features will be discovered during the work process.

Using HATEOAS will help clients avoid the necessity of working with static documentation as well will allow building applications more efficiently. For instance, in case some changes have been made within an API structure, clients will still work without any issues simply because they will follow new links embedded in the API response. Besides, there will be no need for a developer to build URLs and write logic of navigation manually.

---

### Part 2: Room Management

#### Question 1: Implications of Returning IDs vs Full Objects

**Q:** When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client-side processing.

**A:** Responding with only room IDs will be better in terms of network bandwidth as the returned data will have very little information. This is important where a lot of data or constrained network resources are involved. But this puts more burden on the client to make follow-up calls for retrieving full details of rooms if that is needed. That will result in increased latency and a higher number of API calls.

In the other method, the return value contains all necessary information within itself without requiring additional requests from the client's side. Thus, this will enhance the efficiency of the client but only in cases where most of the room details are needed. However, in this case, the size of the response is larger, and that could increase bandwidth consumption.

#### Question 2: DELETE Operation Idempotency

**Q:** Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.

**A:** Yes, the DELETE method is idempotent in this case, as making multiple DELETE requests to the API will lead to the same end state for the system. When a DELETE request is made on the first occasion, the room will be deleted from the data store in memory if it exists in the system and there are no devices assigned to it at that point.

However, after such a request, the room will cease to exist within the system. This means that the desired end state has been reached, and it is "the room has been removed." If a client sends the same DELETE request again by mistake, the API will not make any changes to the system, and will most likely respond with a 404 Not Found response.

Even though the response codes could be different in these two situations, what is important is that the second DELETE request will not affect the final state of the system.

---

### Part 3: Sensor Operations & Linking

#### Question 1: @Consumes Media Type Annotation

**Q:** We explicitly use the @Consumes(MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?

**A:** Specifying @Consumes(MediaType.APPLICATION_JSON) indicates that only requests whose Content-Type is set to application/json will be accepted by the JAX-RS service. Any data sent in other formats such as text/plain and application/xml cannot be converted into their corresponding Java objects due to the lack of any suitable message body readers, Thus the request will be considered invalid and will not be served.

The expected response generated by JAX-RS in such a scenario will be an HTTP 415 (Unsupported Media Type). The use of this status code makes it mandatory for the data sent by the client to strictly adhere to the format supported by the server.

#### Question 2: Query Parameters vs Path-Based Filtering

**Q:** You implemented filtering using @QueryParam. Contrast this with an alternative design where type is part of the URL path (e.g., /api/v1/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?

**A:** However, using @QueryParam would be a better option since it does not change the URI of the primary resource and it allows to add additional information to it which acts as an extension to the entire collection itself. Using /api/v1/sensors means that we have access to the whole sensor collection, whereas adding the ?type=CO2 will help us narrow down the results.

Another reason why using @QueryParam should be preferred over changing the URI itself, is because making a call to the resource /api/v1/sensors/type/CO2 will make it appear as a different sub-resource of the original resource. Using query parameters is far more flexible since you can use multiple filter values, /api/v1/sensors?type=CO2&status=ACTIVE. For instance, which is much cleaner than having to create deeply nested URIs.

---

### Part 4: Deep Nesting with Sub-Resources

#### Question 1: Sub-Resource Locator Pattern

**Q:** Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive controller class?

**A:** This pattern can help enhance REST API architecture because the logic of nested resources will be implemented in different classes instead of having one class perform all operations. For instance, the responsibility for managing sensor-related operations is assigned to the class SensorResource, while SensorReadingResource implements logic related to readings of a particular sensor. This pattern ensures the clear division of responsibilities and maintains the correlation between the resource hierarchy and the actual connection of objects.

For larger APIs, the implementation of this design pattern can help simplify the development process because each class deals with a certain task only. If the developer decided to implement all nested paths inside one controller, the result would be one huge and unreadable class. Such an implementation could have been harder to understand and would require much effort to add functionality and ensure correct performance of operations.

---

### Part 5: Advanced Error Handling, Exception Mapping & Logging

#### Question 1: HTTP 422 vs 404 for Invalid References

**Q:** Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

**A:** The 422 Unprocessable Entity HTTP status code is more appropriate here since the request itself is processed properly and is sent to the right endpoint. Moreover, the format of the sent JSON is also correct. The only issue is that a referenced value (e.g., roomId) in a request body does not exist in the database. This implies that the request itself was received by the server, but there is some logical or semantic error in the provided data.

The reason why the 404 Not Found HTTP status code is not the best option in this case is because 404 is returned when there is no such resource on a specific URL. Hence the endpoint /api/v1/sensors exists, which is why sending a 404 response would be misleading.

#### Question 2: Security Risks of Exposing Stack Traces

**Q:** From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

**A:** Exposing stack traces in the Java internal system to external clients using the API represents another cybersecurity threat because stack traces provide information about how the system operates internally. They include information about packages, classes, methods, file names, lines, and other details that an outside person does not need to see. This type of information needs to be protected since it will allow people from the outside to gain information about the system architecture and technology stack.

An attacker can leverage such knowledge when trying to find weaknesses in the system. Information about classes, frameworks, or libraries that can have vulnerabilities allows the attacker to look for exploits and attacks on particular pieces of software. Moreover, stack traces can contain information about files and methods that are involved in processing data, which can make it possible to find vulnerabilities related to data processing. It might happen that a stack trace will contain sensitive information about the server environment or its configuration, including information about database structures. For these reasons, stack traces should not be exposed by the API.

#### Question 3: JAX-RS Filters for Cross-Cutting Concerns

**Q:** Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?

**A:** The use of JAX-RS filters for addressing cross-cutting concerns like logging is an effective solution as it helps to keep the logic responsible for logging all of the requests and responses in one place, without using the same Logger info statements in each method of the resource class. In this way, it becomes possible to maintain a cleaner codebase where resource classes will not include unnecessary technical information, and will contain only the business logic required.

Such a solution can greatly facilitate maintaining and scaling the project in the future, as any changes in the logic of logging can be implemented only once. This means that for big APIs it will be easy to introduce new rules related to the logging process and maintain consistency in terms of implementation.

---

## Notes

- This project uses in-memory mock storage, so data resets when the server restarts.
- Postman was used for API testing and coursework video demonstration.
- The implementation is intentionally aligned with JAX-RS lecture and tutorial material.

---
