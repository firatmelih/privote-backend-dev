```
__________        .__             __          
\______   \_______|__|__  _______/  |_  ____  
 |     ___/\_  __ \  \  \/ /  _ \   __\/ __ \ 
 |    |     |  | \/  |\   (  <_> )  | \  ___/ 
 |____|     |__|  |__| \_/ \____/|__|  \___  >
                                           \/ 
                                           
                                           Backend Dev Documentation
```

# Table of Contents

- [Setup](#-project-setup)
- [API Endpoints](#api-endpoints)
    - [Poll Controller](#poll-controller)
    - [Auth Controller](#auth-controller)
    - [Vote Controller](#vote-controller)
- [Request Details](#request-details)
    - [Poll](#poll)
    - [Auth](#auth)
    - [Vote](#vote)

## 🛠️ Project Setup

### 1. To run Privote, follow these steps to configure the project correctly:

```shell
git clone git@github.com:firatmelih/privote-backend-dev.git
cd privote-backend-dev
```

### 2. Edit Configuration File: Update the configuration in

`/src/main/java/resources/application.properties`

Here’s what you need to set up:

- DATASOURCE_NAME: The name of your MySQL database.
- USERNAME: Your MySQL username.
- PASSWORD: Your MySQL password.
- DOMAIN_NAME: Domain of the database url, set localhost for local development
- PORT_NAME: Port you defined for MySQL server
- BASE64_SECRET: A Base64-encoded secret key (minimum 128 characters).
- JWT_EXPIRATION: Token expiration time (in milliseconds).

Example configuration:

```properties
spring.application.name=example
spring.datasource.name=example
spring.datasource.url=jdbc:mysql://<localhost>:<3306>/example
spring.datasource.username=rootuser
spring.datasource.password=verysecretpassword
spring.jpa.hibernate.ddl-auto=update
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
jwt.secret=verylongbase64formattedstring
jwt.expiration=60000
```

### 3. Build and Run: After editing the configuration file, use the following commands to build and run the project:

```shell
./mvnw clean install
./mvnw spring-boot:run
```

### 4. Run Postman and check `http:localhost:8080` if project is running

# Project Information 🖥🖨

## API Endpoints

### Poll Controller

| HTTP Method | Endpoint        | Description                   | Authorization |
|-------------|-----------------|-------------------------------|---------------|
| POST        | `/poll`         | Create a new poll             | JWT Token     |
| GET         | `/poll/{id}`    | Get a poll by its ID          | No Auth       |
| GET         | `/poll`         | Get all polls                 | No Auth       |
| PUT         | `/poll/{id}`    | Update an existing poll by ID | JWT Token     |
| DELETE      | `/poll/{id}`    | Delete a poll by its ID       | JWT Token     |
| GET         | `/poll/added`   | Get polls user added to       | JWT Token     |
| GET         | `/poll/created` | Get polls user created        | JWT Token     |

### Auth Controller

| HTTP Method | Endpoint             | Description                       | Authorization |
|-------------|----------------------|-----------------------------------|---------------|
| POST        | `/auth/register`     | Register a new user               | No Auth       |
| POST        | `/auth/authenticate` | Authenticate and obtain JWT token | No Auth       |

### Vote Controller

| HTTP Method | Endpoint     | Description                  | Authorization |
|-------------|--------------|------------------------------|---------------|
| POST        | `/vote/{id}` | Vote for an option in a poll | JWT Token     |

## Request Details

### Poll

- **Create Poll** (POST `/poll`)
- **Request Body**:

```json
        {
  "title": "Your poll title",
  "description": "Details about your poll",
  "options": [
    "Option 1",
    "Option 2",
    "Option 3"
  ],
  "voters": [
    "user1@example.com",
    "user2@example.com"
  ]
} 
```

- **Authorization**: Requires JWT token in `Authorization` header.


- **Get Poll by ID** (GET `/poll/{id}`)
- **Path Parameter**: `id` is the ID of the poll.
- **Authorization**: No token required.


- **Update Poll** (PUT `/poll/{id}`)
    - **Request Body**:

      ```json
          {
        "title": "Your poll title",
        "description": "Details about your poll",
        "options": [
          "Option 1",
          "Option 2",
          "Option 3"
        ],
        "voters": [
          "user1@example.com",
          "user2@example.com"
        ]
      }
      ```

    - **Path Parameter**: `id` is the ID of the poll.
    - **Authorization**: Requires JWT token in `Authorization` header.


- **Delete Poll** (DELETE `/poll/{id}`)
    - **Path Parameter**: `id` is the ID of the poll.
    - **Authorization**: Requires JWT token in `Authorization` header and token needs to have credentials of poll
      creator or
      role `ADMIN`.

- **Get Polls Where User is a Voter** (`/poll/{id}`)
    - **Path Parameter**: `id` is the ID of the poll.
    - **Authorization**: Requires JWT token in `Authorization` header and token needs to have credentials of any voter.

- **Get Polls Where User is a Creator** (`/poll/{id}`)
    - **Path Parameter**: `id` is the ID of the poll.
    - **Authorization**: Requires JWT token in `Authorization` header and token needs to have credentials of creator.

## Auth

- **Register**: (POST `/auth/register`)
    - Request Body:
      ```json
      {
        "username": "your_username",
        "password": "your_password",
        "email": "your_email@example.com",
        "firstName": "YourFirstName",
        "lastName": "YourLastName"
      } 
      ```

- **Authentication**: (POST `/auth/authenticate`)
    - **Request Body**:
  ```json
  {
    "username": "your_username",
    "password": "your_password"
  }
  ```
    - **Response**: Returns a JWT token if credentials are valid.

## Vote

- **Authentication**: (POST `/vote/{id}`)
    - **Request Body**:
  ```json
  {
    "optionId": 2
  }
  ```
    - **Path Parameter**: `id` is the ID of the poll.
    - **Authorization**: Requires JWT token in `Authorization` header and token needs to have credentials of any voter.
