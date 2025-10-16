# Issues to create (migrated from README on 2025-10-16)

Use these ready‑to‑copy titles and descriptions to open GitHub issues. Adjust labels as you see fit (suggested: enhancement, tech‑debt, configuration).

---

## 1) Enable SQL logging and expose Hibernate session statistics

- Scope: all Spring Boot JPA example modules (eager_n_plus_1, eager_n_plus_1_multiple_to_one, lazy_multiple_to_one, lazy_to_many)
- Goal: Make SQL statements visible in logs and provide an easy way to inspect Hibernate session statistics during demos/tests.
- Acceptance criteria:
  - Configure Hibernate to log SQL and bind parameters in dev/test profiles.
  - Optionally add datasource-proxy or p6spy for nicer formatting.
  - Provide a short note in each module README (or central README) describing how to enable/disable these logs.
  - Document any performance considerations.
- Hints:
  - spring.jpa.properties.hibernate.format_sql=true
  - logging.level.org.hibernate.SQL=DEBUG
  - logging.level.org.hibernate.orm.jdbc.bind=TRACE
  - Consider using datasource‑proxy (version defined in gradle.properties).

---

## 2) Investigate and resolve warning: "HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)"

- Scope: modules that log this warning at startup.
- Goal: Either configure an appropriate JTA platform (if truly needed) or suppress the warning by explicitly setting JTA integration to none for non‑JTA setups.
- Acceptance criteria:
  - Startup logs are free of the HHH000489 warning for local/dev runs.
  - Rationale is documented in README or comments (we typically don’t need JTA for a single datasource demo app).
- Hints:
  - spring.jpa.properties.hibernate.transaction.jta.platform=org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform

---

## 3) Use Gradle extra[] (or versions catalog) to source Spring Boot version from a single place

- Scope: build configuration
- Goal: Centralize Spring Boot version management to avoid duplication and align with gradle.properties or a versions catalog.
- Acceptance criteria:
  - Spring Boot plugin and dependencies derive version from a single, declared property.
  - Document where the version is defined.
- Hints:
  - Use settings.gradle.kts or buildSrc/versions catalog; or use gradle.properties + plugins { id("org.springframework.boot") version libs.versions.springBoot } if using catalogs.

---

## 4) Use canonical path from CWD for any file‑system dependent paths

- Scope: build scripts and runtime code that relies on relative paths.
- Goal: Ensure file paths are resolved robustly from the project root/current working directory, avoiding surprises when run from IDE vs CLI.
- Acceptance criteria:
  - All file resolutions use canonical/absolute paths (Paths.get(".").toRealPath()) or Spring Resource abstractions.
  - Add a brief note to README on path assumptions if relevant.
