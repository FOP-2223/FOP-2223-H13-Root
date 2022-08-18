import org.sourcegrade.submitter.submit

plugins {
    java
    application
    id("org.sourcegrade.style") version "1.3.0"
    id("org.sourcegrade.submitter") version "0.4.0"
    id("org.openjfx.javafxplugin") version "0.0.13"
}

version = "0.1.0-SNAPSHOT"

repositories {
    mavenLocal()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    mavenCentral()
}

javafx {
    version = "17.0.1"
    modules("javafx.controls", "javafx.fxml")
}

submit {
    assignmentId = "h13"
    studentId = "ab12cdef"
    firstName = "sol_first"
    lastName = "sol_last"
    requireTests = false
}

val grader: SourceSet by sourceSets.creating {
    val test = sourceSets.test.get()
    compileClasspath += test.output + test.compileClasspath
    runtimeClasspath += output + test.runtimeClasspath
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    "graderCompileOnly"("org.sourcegrade:jagr-launcher:0.5.0") {
        exclude("org.jetbrains", "annotations")
    }
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("org.tudalgo:algoutils-student:0.1.0-SNAPSHOT")
    "graderImplementation"("org.tudalgo:algoutils-tutor:0.1.0-SNAPSHOT")
}

application {
    mainClass.set("h13.SpaceInvaders")
}

tasks {
    val runDir = File("build/run")
    named<JavaExec>("run") {
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
    val graderTest by creating(Test::class) {
        group = "verification"
        doFirst {
            runDir.mkdirs()
        }
        workingDir = runDir
        testClassesDirs = grader.output.classesDirs
        classpath = grader.compileClasspath + grader.runtimeClasspath
        useJUnitPlatform()
    }
    named("check") {
        dependsOn(graderTest)
    }
    val graderJar by creating(Jar::class) {
        group = "build"
        afterEvaluate {
            archiveFileName.set("FOP-2223-H13-Root-${project.version}.jar")
            from(sourceSets.main.get().allSource)
            from(sourceSets.test.get().allSource)
            from(grader.allSource)
        }
    }
    val graderLibs by creating(Jar::class) {
        group = "build"
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        // don't include Jagr's runtime dependencies
        val jagrRuntime = configurations["graderCompileClasspath"]
            .resolvedConfiguration
            .firstLevelModuleDependencies
            .first { it.moduleGroup == "org.sourcegrade" && it.moduleName == "jagr-launcher" }
            .allModuleArtifacts
            .map { it.file }

        val runtimeDeps = grader.runtimeClasspath.mapNotNull {
            if (it.path.toLowerCase().contains("h13") || jagrRuntime.contains(it)) {
                null
            } else if (it.isDirectory) {
                it
            } else {
                zipTree(it)
            }
        }
        from(runtimeDeps)
        archiveFileName.set("FOP-2223-H13-Root-${project.version}-libs.jar")
    }
    create("graderAll") {
        group = "build"
        dependsOn(graderJar, graderLibs)
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    jar {
        enabled = false
    }
}
