# Herbiboar Hiscore

A [RuneLite](https://runelite.net/) plugin for tracking Herbiboar hunting high scores across all players on a shared leaderboard.

## Repository structure

```
HerbiboarHiscore/
├── api/                          # Spring Boot REST API (backend)
│   └── src/main/java/com/herbiboarhiscore/api/
│       ├── HerbiboarHiscoreApiApplication.java
│       ├── HiscoreController.java
│       ├── HiscoreEntry.java
│       ├── HiscoreRepository.java
│       ├── HiscoreService.java
│       └── ScoreRequest.java
└── src/main/java/com/herbiboarhiscore/   # RuneLite plugin (client)
    ├── HerbiboarHiscorePlugin.java
    ├── HerbiboarHiscoreConfig.java
    ├── HerbiboarHiscoreClient.java
    ├── HerbiboarHiscorePanel.java
    └── ScoreEntry.java
```

---

## Backend — Spring Boot API

### Requirements

- Java 17+
- Maven 3.6+
- PostgreSQL 14+ database

### Building

```bash
cd api
mvn package -DskipTests
```

This produces `api/target/herbiboar-hiscore-api-1.0-SNAPSHOT.jar`.

### Environment variables

| Variable | Description | Example |
|---|---|---|
| `DATABASE_URL` | JDBC URL for PostgreSQL | `jdbc:postgresql://localhost:5432/herbiboarhiscore` |
| `DATABASE_USERNAME` | Database user | `postgres` |
| `DATABASE_PASSWORD` | Database password | `secret` |
| `PORT` | HTTP port (optional) | `8080` |

Tables are created automatically on first startup (`ddl-auto=update`).

### Running locally

```bash
export DATABASE_URL=jdbc:postgresql://localhost:5432/herbiboarhiscore
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=secret
export PORT=8080
cd api
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

### API endpoints

| Method | Path | Body | Description |
|---|---|---|---|
| `POST` | `/scores` | `{"playerName":"...", "harvestCount":123}` | Upsert a player's score |
| `GET` | `/scores` | — | Returns all scores ranked highest first |

### Deploying to a hosting provider

The API is a standard Spring Boot fat-jar and runs on any provider that supports Java.

#### Render (recommended free tier)

1. Push the repository to GitHub.
2. In [Render](https://render.com), create a new **Web Service** and connect the repo.
3. Set **Root Directory** to `api`.
4. Set **Build Command** to `mvn package -DskipTests`.
5. Set **Start Command** to `java -jar target/herbiboar-hiscore-api-1.0-SNAPSHOT.jar`.
6. Add a **PostgreSQL** database from the Render dashboard and copy the connection values into the four environment variables above.
7. Deploy — Render will build and start the service automatically on every push.

#### Railway

1. Push the repository to GitHub.
2. In [Railway](https://railway.app), create a new project → **Deploy from GitHub repo**.
3. Add a **PostgreSQL** plugin to the project; Railway auto-injects `DATABASE_URL`.
4. Set the start command to `java -jar api/target/herbiboar-hiscore-api-1.0-SNAPSHOT.jar`.
5. Add `DATABASE_USERNAME` and `DATABASE_PASSWORD` from the PostgreSQL plugin's connection details.

#### Fly.io

```bash
cd api
fly launch          # follow prompts, choose a region
fly postgres create # attach a Postgres cluster
fly secrets set DATABASE_USERNAME=... DATABASE_PASSWORD=...
fly deploy
```

---

## Plugin — RuneLite client

### Requirements

- Java 11+
- Maven 3.6+
- RuneLite client

### Building

```bash
mvn install
```

The compiled `.jar` is placed in `target/`.



### Running in development

1. Clone the [RuneLite](https://github.com/runelite/runelite) source and build it locally (or ensure the client artifact is in your local Maven cache).
2. Open this project in IntelliJ IDEA (recommended).
3. Run the RuneLite `client` module with `--developer-mode` to side-load the plugin from your local build.

### Configuration

Once the plugin is loaded in the RuneLite client:

1. Open the **Plugin Panel** (wrench icon on the right sidebar).
2. Search for **Herbiboar Hiscore** and enable it.
3. Click the **gear icon** next to the plugin name to open its settings.

| Setting | Description | Default |
|---|---|---|
| API Base URL | URL of the deployed backend | `https://your-api-host.com` |

Set **API Base URL** to the URL of your deployed backend (e.g. `https://herbiboar-hiscore.onrender.com`).

### Deploying to the RuneLite Plugin Hub

1. Fork the [Plugin Hub](https://github.com/runelite/plugin-hub) repository.
2. Add an entry for this plugin pointing to your fork of this repository.
3. Open a pull request against the Plugin Hub — the RuneLite team will review it.
4. Once merged, the plugin appears in the in-client Plugin Hub and users can install it directly.

See the [Plugin Hub README](https://github.com/runelite/plugin-hub#readme) for the full submission checklist.

---

## Contributing

1. Fork the repository.
2. Create a feature branch: `git checkout -b my-feature`
3. Commit your changes: `git commit -m 'Add my feature'`
4. Push to the branch: `git push origin my-feature`
5. Open a pull request.
