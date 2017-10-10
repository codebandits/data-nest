# data-nest

Data-nest is an opinionated tool for accessing and persisting data for Kotlin applications. It is designed with the belief that an application's domain model should remain pure and not be polluted by persistence or serialization concerns. It builds upon the typesafe SQL DSL and table objects from JetBrains' excellent [Exposed](https://github.com/JetBrains/Exposed) library.

## Advantages of data-nest

### data-nest vs. JPA

JPA encourages using the same object to represent your domain and to manage its persistence concerns. We believe this paradigm can complicate business logic in applications and leads to design compromises. Data-nest builds upon the Exposed library's typesafe SQL DSL and table objects to manage a domain model's persistence and requires developers to provide separate classes to represent the domain model itself.

### data-nest vs. Exposed (on its own)

Exposed provides two layers of database access: a typesafe SQL DSL and data access object entities. The DAO entities provide a simple way to build and use common queries but blend persistence concerns back in to domain objects (akin to Ruby on Rails' ActiveRecord). There is a great discussion on this approach in [Exposed GitHub issue #24](https://github.com/JetBrains/Exposed/issues/24).

Data-nest builds upon Exposed's typesafe SQL DSL and provides an alternative higher level data access mechanism inspired by Spring Data's repository pattern.
