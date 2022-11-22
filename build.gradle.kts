@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    java
    application
    alias(libs.plugins.style)
    alias(libs.plugins.jagr.gradle)
    alias(libs.plugins.javafxplugin)
}

version = file("version").readLines().first()

javafx {
    version = "17.0.1"
    modules("javafx.controls", "javafx.fxml")
}

jagr {
    assignmentId.set("h13")
    submissions {
        val main by creating {
            studentId.set("ab12cdef")
            firstName.set("sol_first")
            lastName.set("sol_last")
        }
    }
    graders {
        val graderPublic by creating {
            graderName.set("FOP-2223-H13-Public")
            rubricProviderName.set("h13.H13_RubricProvider")
            configureDependencies {
                implementation(libs.algoutils.tutor)
            }
        }
        val graderPrivate by creating {
            parent(graderPublic)
            graderName.set("FOP-2223-H13-Private")
        }
    }
}

dependencies {
    implementation(libs.annotations)
    implementation(libs.algoutils.student)
    testImplementation(libs.junit.core)
}

application {
    mainClass.set("h13.SpaceInvaders")
}

tasks {
    val runDir = File("build/run")
    withType<JavaExec> {
        doFirst {
            runDir.mkdirs()
        }
        workingDir = runDir
    }
    test {
        doFirst {
            runDir.mkdirs()
        }
        workingDir = runDir
        useJUnitPlatform()
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
}
