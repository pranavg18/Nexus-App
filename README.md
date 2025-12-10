# Nexus-App
Nexus App is a minimal, backend-only chat platform built using Spring Boot. It is designed to mimic WhatsApp-like messaging functionality without a database. Instead, all the data (users, conversations, group chats, message metadata) is stored in JSON files.

## Table of Contents
- [About](#about)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Demo](#demo)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [API Reference](#api-reference)
- [Known Issues](#known-issues)
- [Roadmap / Future Improvements](#roadmap--future-improvements)
- [Credits](#credits)
- [License](#license)

## About
Nexus App is a lightweight communication backend that supports:
- User registration, authentication, logout, and deletion
- One-on-one direct messaging (DM) between users
- Group creation, joining, and group messaging
- Clearing chats
- Deleting messages for everyone, both in DMs as well as in group chats
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

## Demo
### Swagger UI - Home Page Preview
![Swagger Homepage](https://github.com/user-attachments/assets/262bc634-da16-40fd-9d6e-3714ee8c3d54)
This is a high-level view of what our backend application looks like on Swagger UI. For more information regarding all the features and how they work, refer to the demo video below.
### Demo Video

See the full walkthrough of Nexus App:

[NexusApp-DemoVideo.mp4](https://github.com/user-attachments/assets/334d5b3b-aa4d-44fc-b1d2-7ae8e5123e25)

## Installation
Run the following commands in terminal.

### 1. Clone the repository

```bash
git clone https://github.com/htserhsluk/Nexus-App
cd nexus-app
```

### 2. Ensure Java (atleast 17+) and Maven are installed

```
java -version
mvn -version
```

### 3. Install Lombok plugin (IntelliJ)
1. Go to File -> Settings -> Plugins -> Search "Lombok"
2. Install and restart
3. Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors -> Enable annotation processing

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

## Known Issues
- JSON files may grow large over time
- Anyone can join group without needing approval from admin

## Roadmap / Future Improvements
- Replace JSON with database to reduce space complexity
- Expansion of group admin roles
- Frontend UI (React or Angular)
- File transfer of images, videos, etc.

## Credits
All four people recorded different parts of the video, which were merged together in the end. The voiceover was done by Kulshresth.

Listed below are the API Endpoints managed by each person.

### 1. Kulshresth (IMT2024065)
- Group Leader
- Demo video voiceover
- POST `/api/auth/logout`
- POST `/api/chat/group/join`
- DELETE `/api/chat/clear-chat`
### 2. Pranav Goyal (BT2024086)
- POST `/api/auth/register`
- POST `/api/chat/send`
- GET `/api/chat/history/`
- Created the [README](./README.md)
### 3. Hrishabh Sharrma (BT2024070)
- POST `/api/chat/group/create`
- DELETE `/api/chat/delete-message`
- DELETE `/api/auth/delete`
### 4. Aksha Alkesh Jain (BT2024015)
- POST `/api/auth/login`
- GET `/api/chat/group/history`
- POST `/api/chat/group/send`

## License
This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for details.
