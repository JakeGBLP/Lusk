import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    java
    `maven-publish`
    id("xyz.jpenilla.run-paper") version "3.1.0"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
    id("com.gradleup.shadow") version "9.6.1"
}

group = "it.jakegblp"
version = "2.0.0-alpha1"
var latestSkriptVersion = "2.16.2"
var latestMinecraftVersion = "26.2"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.skriptlang.org/releases")
}

dependencies {
    subprojects.forEach {
        if (it.path.startsWith(":nms:")) {
            runtimeOnly(project(it.path))
        } else if (it.path != ":nms") {
            implementation(project(it.path))
        }
    }
    implementation("org.bstats:bstats-bukkit:3.0.2")
    compileOnly(project(":nms-core"))
    compileOnly("org.apache.commons:commons-text:1.15.0")
    compileOnly("com.google.guava:guava:33.6.0-jre")
    compileOnly("com.github.SkriptLang:Skript:$latestSkriptVersion")
    implementation("org.jspecify:jspecify:1.0.0")
    compileOnly("io.netty:netty-all:4.2.13.Final")
    compileOnly("org.jetbrains:annotations:20.1.0")
    compileOnly("org.projectlombok:lombok:1.18.46")
    annotationProcessor("org.projectlombok:lombok:1.18.46")
    testCompileOnly("org.projectlombok:lombok:1.18.46")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.46")
    paperweight.paperDevBundle("$latestMinecraftVersion.build.+")

}

tasks {
    runServer {
        minecraftVersion(latestMinecraftVersion)
        downloadPlugins {
            url("https://github.com/SkriptLang/Skript/releases/download/$latestSkriptVersion/Skript-$latestSkriptVersion.jar")
            url("https://github.com/SkriptLang/skript-reflect/releases/download/v2.6.3/skript-reflect-2.6.3.jar")
        }

    }
    shadowJar {
        relocate("org.bstats", "it.jakegblp.lusk")
        archiveClassifier.set("")
    }
    build {
        dependsOn("shadowJar")
    }
    processResources {
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version.toString()))
    }
}
//tasks.withType(xyz.jpenilla.runtask.task.AbstractRun::class) {
//    javaLauncher = javaToolchains.launcherFor {
//        vendor = JvmVendorSpec.JETBRAINS
//        languageVersion = JavaLanguageVersion.of(25)
//    }
//    jvmArgs("-XX:+AllowEnhancedClassRedefinition")
//}
subprojects {
    apply(plugin = "java")

    val specialCompileOnly by configurations.creating {
        isCanBeConsumed = false
        isCanBeResolved = true
        extendsFrom(configurations.compileOnly.get())
    }

    val versionRegex = Regex("""\d+\.\d+(?:\.\d+)?""")
    val versionMatch = versionRegex.find(name)
    val detectedVersion = versionMatch?.value ?: latestMinecraftVersion
    var isNmsModule = project.path.startsWith(":nms:")
    var isSkriptModule = project.path.startsWith(":skript:")
    if (isNmsModule && project.name != "build") {
        apply(plugin = "io.papermc.paperweight.userdev")
        dependencies {
            paperweight.paperDevBundle(properVersion(detectedVersion))
        }
    } else if (!isSkriptModule) {
        dependencies {
            compileOnly("io.papermc.paper:paper-api:${properVersion(detectedVersion)}")
        }
    } else if (project.name != "build") {
        dependencies {
            specialCompileOnly("com.github.SkriptLang:Skript:${properVersion(detectedVersion)}")
        }
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(25))
    }

    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://jitpack.io/")
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://libraries.minecraft.net")
    }

    dependencies {
        if (name != "common") {
            implementation(project(":common"))
        } else {
            compileOnly("com.github.SkriptLang:Skript:${latestSkriptVersion}")
        }
        if (name == "lusk") {
            compileOnly("com.github.SkriptLang:Skript:$latestSkriptVersion")
            compileOnly(project(":nms-core"))
        }

        if (name == "nms-factory" || isNmsModule) {
            compileOnly(project(":nms-core"))
        }
        if (name == "nms-factory") {
            rootProject.subprojects.forEach {
                if (it.path.startsWith(":nms:")) {
                    specialCompileOnly(project(it.path))
                }
            }
        }
        if (name == "nms-shade") {
            implementation(project(":nms-factory"))
            implementation(project(":nms-core"))
        }
        if (name == "nms-core") {
            dependencies {
                compileOnly("org.apache.logging.log4j:log4j-api:2.25.3")
                compileOnly("org.apache.logging.log4j:log4j-core:2.25.3")

            }
        }
        compileOnly("com.google.guava:guava:33.6.0-jre")
        compileOnly("org.apache.commons:commons-text:1.15.0")
        compileOnly("com.mojang:datafixerupper:8.0.16")
        compileOnly("io.netty:netty-all:4.2.9.Final")
        compileOnly("it.unimi.dsi:fastutil:8.5.15")
        compileOnly("org.jspecify:jspecify:1.0.0")
        compileOnly("org.jetbrains:annotations:20.1.0")
        compileOnly("org.projectlombok:lombok:1.18.46")
        annotationProcessor("org.projectlombok:lombok:1.18.46")
        testCompileOnly("org.projectlombok:lombok:1.18.46")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.46")
    }

    tasks.compileJava {
        classpath += specialCompileOnly
    }

    tasks.compileTestJava {
        classpath += specialCompileOnly
    }
}

project(":nms-shade") {
    apply(plugin = "java")
    apply(plugin = "maven-publish")
    apply(plugin = "com.gradleup.shadow")

    dependencies {
        rootProject.subprojects.forEach {
            if (it.path.startsWith(":nms:")) {
                implementation(project(it.path))
            }
        }
    }

    tasks.named<ShadowJar>("shadowJar") {
        archiveClassifier.set("")
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                groupId = "it.jakegblp"
                artifactId = "nms-shade"
                version = rootProject.version.toString()

                artifact(tasks.named("shadowJar"))
            }
        }
        repositories {
            mavenLocal()
        }
    }
}

fun properVersion(version: String): String {
    return if (version == "1.21.11") "1.21.11-R0.1-SNAPSHOT" else "$version.build.+"
}