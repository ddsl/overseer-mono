rootProject.name = "overseer-mono"

include("auth")
project(":auth").projectDir = file("apps/auth")

include("common-lib")
project(":common-lib").projectDir=file("libs/common-lib")
//include("libs:common-lib")