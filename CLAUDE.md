# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Educational project demonstrating common Hibernate/JPA pitfalls (N+1 queries, fetching strategies, etc.) with a Reveal.js presentation for talks/workshops. Built as a Gradle multi-module Spring Boot project with a shared starter.

## Build & Run Commands

### Java (Gradle)
- **Build all:** `./gradlew build`
- **Run tests:** `./gradlew test` (all modules) or `./gradlew :eager_n_plus_1:test` (single module)
- **Run single test class:** `./gradlew :eager_n_plus_1:test --tests "dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1.EagerNPlus1SelectTest"`
- **Run example app:** `./gradlew :eager_n_plus_1:bootRun` (or any other module)
- **Start database first:** `docker compose up -d` (Postgres 17 on port 15432)

Tests use `spring.profiles.active=test` automatically (configured in root `build.gradle.kts`). The `application-test.yml` in `common_starter` enables Docker Compose integration, so Postgres starts automatically during tests if Docker is available.

### Presentation (Node.js)
- **Setup:** `cd presentation && npm ci`
- **Dev server:** `npm run dev` (press `o` then Enter to open browser)
- **Build:** `npm run build`
- **Tests:** `npm run test:run` (Vitest)

## Architecture

### Gradle Modules
- **common_starter** — `java-library` (no Spring Boot plugin). Shared Spring Boot auto-configuration, datasource proxy for SQL counting, and test harness (`SqlStatementAssertions`, `DatasourceProxyBeanPostProcessor`, `TransactionRunner`, `TestDataPopulator`). All example modules depend on this.
- **eager_n_plus_1** — Demonstrates eager `@ManyToOne` causing N+1 selects with various query mechanisms (JPQL, Criteria, Specification, QueryDSL).
- **eager_n_plus_1_multiple_to_one** — Same pattern with multiple `@ManyToOne` associations and DTO projections.
- **lazy_multiple_to_one** — Lazy `@ManyToOne` with `@NamedEntityGraph`, proxy-based association setting.
- **lazy_to_many** — Lazy `@OneToMany` with pagination, JOIN FETCH, bulk fetching, naive vs optimal export patterns.

### Key Libraries
- Spring Boot 3.5.x, Hibernate 6.x, Java 21 toolchain
- QueryDSL (OpenFeign fork), Hypersistence Utils, Lombok
- datasource-proxy (net.ttddyy) for SQL statement interception and counting

### Test Harness Pattern
The `common_starter` provides `SqlStatementAssertions` which intercepts SQL via datasource-proxy to assert exact query counts in tests. Example modules use `@SpringBootTest` with the shared test profile.

### Presentation
Vite + Reveal.js + TypeScript + Mermaid diagrams. Slides are filesystem-based under `presentation/slides/` (numbered directories with `.md` and `.html` files). A Vite plugin copies referenced Java source files into `public/src-to-present/` at build time for code display in slides. Feature specs are documented in `presentation/specs/`.

## Conventions
- Always use `application.yml` (never `.properties`) for Spring Boot config across all modules.
- Base package: `dev.somlyaip.blog.youdontknowhibernate.<module_name>`
- Database: Postgres on `localhost:15432`, DB name `issue_tracker`, user `admin`