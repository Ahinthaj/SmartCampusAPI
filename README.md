<<<<<<< HEAD
# SmartCampusAPI
=======
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
- `POST /api/v1/sensors/{sensorId}/readings` → appends a new reading and updates the sensor’s `currentValue`

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

## Notes

- This project uses in-memory mock storage, so data resets when the server restarts.
- Postman was used for API testing and coursework video demonstration.
- The implementation is intentionally aligned with JAX-RS lecture and tutorial material.

---
>>>>>>> 51915e07e62291101aed76730e7596d38f94fd18
