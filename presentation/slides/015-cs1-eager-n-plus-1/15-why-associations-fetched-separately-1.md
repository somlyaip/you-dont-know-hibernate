## Why associations are fetched separately? #1

- It is the infamous N+1 query problem
    - Most of the DB access performance bottlenecks are caused by that symptom
    
Notes:
Vlad Michalcea - High-Performance Java Persistence: 14.3.3.2.1 The N+1 query problem