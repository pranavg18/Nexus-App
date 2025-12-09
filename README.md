# Nexus-App
Nexus App is a minimal, backend-only chat platform built using Spring Boot. It is designed to mimic WhatsApp-like messaging functionality without a database. Instead, all the data (users, conversations, group chats, message metadata) is stored in JSON files.

## Table of Contents
- [About](#about)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Screenshots / Demo](#screenshots--demo)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [API Reference](#api-reference)
- [Roadmap / Future Improvements](#roadmap--future-improvements)
- [Known Issues](#known-issues)
- [Credits](#credits)
- [License](#license)

## About
Nexus App is a lightweight communication backend that supports:
- User registration and authentication
- One-on-one direct messaging (DM) between users
- Group creation, joining, and group messaging
- Clearing chats
- Deleting messages for everyone in group
- JSON file-based database system

Swagger UI was used for easy testing.

## Features
### User Management
- Register new users
- Strong password validation
- Login/Logout
- Delete account
- Tracks active login sessions

### Direct Messaging
- Send message to any registered user
- Stores conversation in `<user1>_<user2>.json` file
- Retrieve chat history
- Message timestamps
- Delete a specific message for everyone
- Clear chat only for yourself

### Group Chat
- Create groups
- Join groups
- Send group messages
- Retrieve group history
- Delete messages in groups
- Clear group chat only for yourself

### JSON Files
Stored inside:
- `/data/users.json`
- `/data/<user1>_<user2>.json`
- `/data/groups.json`
- `/data/group_{groupName}.json`

### Utilities
- Used Swagger UI for local testing
- Password validation regex
- Safe read/write operations with ObjectMapper
- Local login session tracking

## Tech Stack
### Backend
- Java
- Spring Boot
- Spring Web
- Spring Context
- Lombok
- Jackson (ObjectMapper for JSON)
### Frontend
- None (API-only backend)
- Swagger UI used for testing requests
### Database
- Pure JSON file storage
### Other Tools
- Swagger/OpenAPI
- Maven build system
- IntelliJ IDEA

## Screenshots / Demo
Pending video upload.

## Installation
Run the following commands in terminal.

### 1. Clone the repository
```bash
git clone https://github.com/htserhsluk/Nexus-App
cd nexus-app
```

### 2. Ensure Java (atleast 17+) and Maven are installed
```java -version
mvn -version
```

### 3. Install Lombok plugin (IntelliJ)
- Go to File -> Settings -> Plugins -> Search "Lombok"
- Install and restart
- Enable annotation processing:
 - Settings -> Build -> Compiler -> Annotation Processors -> Enable

### 4. Run the application
- Using IntelliJL
 - Run -> NexusApplication
- Or using Maven:
 - `mvn spring-boot:run`

Check which port it runs on, and then open the URL `http://localhost:<port_number>` in your browser.

## Configuration
No configuration required, although you can modify in `src/main/resources/application.properties`: `server.port=8084`. This allows you to run it on the URL `http://localhost:8084` always.

## Usage
- Open Swagger UI at `http://localhost:<port_number>/swagger-ui/index.html`
- Try out the commands according to the API Reference table.

## API Reference
### Auth Endpoints (`/api/auth`)
| Method | Endpoint      | Description                  |
|--------|---------------|------------------------------|
| POST   | `/register`   | Register a new user          |
| POST   | `/login`      | Login user (session tracked) |
| POST   | `/logout`     | Logout user                  |
| DELETE | `/delete`     | Delete user account          |

### Direct Messaging (`/api/chat/group`)
| Method | Endpoint            | Description                                     |
|--------|---------------------|-------------------------------------------------|
| POST   | `/send`             | Send direct message                             |
| GET    | `/history`          | Get chat history (requires password + login)    |
| DELETE | `/clear-chat`       | Clear chat for the requester                    |
| DELETE | `/delete-message`   | Delete a specific message for everyone          |

### Group Messaging (`/api/chat/group`)
| Method | Endpoint       | Description                     |
|--------|----------------|---------------------------------|
| POST   | `/create`      | Create a new group              |
| POST   | `/join`        | Join an existing group          |
| POST   | `/send`        | Send a message to a group       |
| GET    | `/history`     | View group chat history         |


## Roadmap / Future Improvements
- Typing indicators, online status
- Frontend UI (React or Angular)
- Message search
- File transfer of images, videos, etc.
- Expansion of group admin roles

## Known Issues
- JSON files may grow large over time
- Anyone can join group without needing approval from admin

## Credits
- Kulshresth (IMT2024065)
 - Team Leader
 - a
 - b
 - c
- Pranav Goyal (BT2024086)
 - a
 - b
 - c
- Hrishabh Sharrma (BT2024070)
 - a
 - b
 - c
- Aksha Alkesh Jain (BT2024015)
 - a
 - b
 - c

## License
This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for details.
