rootProject.name = "you-dont-know-hibernate"

pluginManagement {
    plugins {
        id("org.springframework.boot") version extra["springBootVersion"] as String
        id("io.spring.dependency-management") version extra["springDependencyManagementVersion"] as String
        id("io.freefair.lombok") version extra["lombokVersion"] as String
    }
}

include("common_starter")
include("eager_n_plus_1")
include("eager_n_plus_1_multiple_to_one")
include("lazy_multiple_to_one")
include("lazy_to_many")
include("partial_fetch")
