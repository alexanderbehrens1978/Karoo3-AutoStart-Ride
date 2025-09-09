/* pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    // Hammerhead karoo-ext (GitHub Packages) — requires credentials in gradle.properties
    maven {
      name = "HammerheadKarooExt"
      url = uri("https://maven.pkg.github.com/hammerheadnav/karoo-ext")
      credentials {
        username = providers.gradleProperty("gpr.user").orNull ?: System.getenv("GPR_USER")
        password = providers.gradleProperty("gpr.key").orNull ?: System.getenv("GPR_TOKEN")
      }
    }
  }
}

rootProject.name = "Karoo3-AutoStart-Ride"
include(":app")
*/


pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "Karoo3-AutoStart-Ride"
include(":app")

// >>> WICHTIG: karoo-ext lokal einbinden (vorher clonen nach external/karoo-ext)
//includeBuild("/home/alex/Dokumente/external/karoo-ext")
includeBuild("external/karoo-ext") {
  dependencySubstitution {
    // ersetzt "io.hammerhead:karoo-ext" durch das lokale Projekt ":lib"
    substitute(module("io.hammerhead:karoo-ext")).using(project(":lib"))
  }
}
