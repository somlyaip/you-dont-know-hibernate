- N+1 query problem
  - Associations fetched in separate SQL queries instead of in a combined one that uses JOIN directives
    - select fetching instead of join fetching
  - Although it's usually associated with FetchType.LAZY
  - An EAGER one can cause it too
    - When executing a JPQL/HQL query where
      - Entities are not filtered by id
      - Or the EAGER association is not explicitly fetched

Note:
Vlad Michalcea - High-Performance Java Persistence: 14.3.3.2.1 The N+1 query problem