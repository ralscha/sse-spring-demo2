# Spring Boot SSE Demo

Small demo that streams server-sent events from Spring Boot to an Angular client.

## Requirements

- Java 21 or newer
- Node.js with npm

## Run the backend

```sh
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

The backend starts on `http://localhost:8080` and exposes:

- `GET /register/{clientId}` as an SSE stream

The scheduled demo publisher sends `progress` and `event` messages to `client1` every five seconds. The backend also sends SSE heartbeat comments every 15 seconds so idle infrastructure is less likely to close the stream.

## Run the Angular client

```sh
cd client
npm install
npm start
```

Open `http://localhost:4200`. The client subscribes to:

- `http://localhost:8080/register/client1`

## Verify

```sh
./mvnw test
cd client
npm run build
```

`npm audit --omit=dev` currently reports no production vulnerabilities. Remaining full-audit findings are in Angular build tooling.

