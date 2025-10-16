# You don't know Hibernate

A collection of small, focused examples that demonstrate common pitfalls and performance traps in Hibernate/JPA (N+1, fetching strategies, etc.), plus a "common-starter" to keep shared configuration in one place. Includes a Reveal.js presentation for talks/workshops.

## Project structure
- common-starter — Shared Spring Boot starter used by example modules (common configs, deps).
- eager_n_plus_1 — Example project showing eager fetching causing N+1 queries.
- eager_n_plus_1_multiple_to_one — Variation of the eager N+1 case with multiple-to-one relations.
- lazy_multiple_to_one — Example focusing on lazy loading with multiple-to-one relations.
- lazy_to_many — Example focusing on lazy loading of to-many relations.
- presentation — Slides built with Vite + Reveal.js. See presentation/README.md.
- gradle, gradlew, gradlew.bat, build.gradle.kts, settings.gradle.kts — Gradle build files (uses Java toolchain 21, versions from gradle.properties).
- docker-compose.yml — Local Postgres instance for demos (exposes 15432 -> 5432).

## Requirements
- Java 21 (Gradle toolchain will download if missing)
- Docker + Docker Compose (for the sample Postgres)
- Node.js 22.x (only for the presentation) or nvm to switch versions
- Graphviz (for diagrams referenced by the presentation): macOS via Homebrew → `brew install graphviz`

## Quick start
1) Start the database (Postgres 17 on port 15432):
   - docker compose up -d
2) Build everything:
   - ./gradlew build
3) Run an example app (pick one):
   - ./gradlew :eager_n_plus_1:bootRun
   - ./gradlew :eager_n_plus_1_multiple_to_one:bootRun
   - ./gradlew :lazy_multiple_to_one:bootRun
   - ./gradlew :lazy_to_many:bootRun
4) Run tests:
   - ./gradlew test

Database defaults (from docker-compose.yml): DB=issue_tracker, user=admin, password=&ZjWlGXYkU6cATGwf5iy, host=localhost, port=15432.

## Presentation
See presentation/README.md for setup and running. In short:
- cd presentation && npm ci
- npm run dev (press o then Enter to open in the browser)

## Housekeeping
The TODOs previously listed here were migrated to .github/ISSUES_TO_CREATE.md on 2025-10-16. Please create GitHub issues from that file and track progress there.