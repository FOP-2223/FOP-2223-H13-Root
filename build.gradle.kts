@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    java
    application
    // alias(libs.plugins.style)
    alias(libs.plugins.jagr.gradle)
    alias(libs.plugins.javafxplugin)
}

version = file("version").readLines().first()

javafx {
    version = "17.0.1"
    modules("javafx.controls", "javafx.fxml", "javafx.swing", "javafx.graphics", "javafx.base")
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
                implementation("org.mockito:mockito-inline:5.0.0")
                implementation("org.testfx:testfx-core:4.0.16-alpha")
                implementation("org.testfx:testfx-junit5:4.0.16-alpha")
                implementation("org.mockito:mockito-junit-jupiter:4.9.0")
                implementation("org.junit-pioneer:junit-pioneer:1.7.1")
                implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
                compileOnly("org.testfx:openjfx-monocle:jdk-12.0.1+2")
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
