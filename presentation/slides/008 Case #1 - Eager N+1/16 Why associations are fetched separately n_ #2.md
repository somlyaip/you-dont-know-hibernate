* N+1 query problem
  * Associations fetched in separated SQL queries instead of in a combined one which uses JOIN directives
    * select fetching instead of join fetching
  * Although it is associated with the FetchType.LAZY
  * An EAGER one can cause it too
    * When executing a JPQL/HQL query where
      * Entities are not filtered by id
      * Or the EAGER association is not explicitly fetched
      
Notes:
Vlad Michalcea - High-Performance Java Persistence: 14.3.3.2.1 The N+1 query problem