rootProject.name = "overseer-mono"

pluginManagement{
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement{
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

include("auth")
project(":auth").projectDir = file("apps/auth")

include("common-lib")
project(":common-lib").projectDir=file("libs/common-lib")
//include("libs:common-lib")