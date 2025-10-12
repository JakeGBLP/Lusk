import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.github.patrick.gradle.remapper.RemapTask
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    java
    `maven-publish`
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
    id("io.github.patrick.remapper") version "1.4.2"
    id("com.gradleup.shadow") version "8.3.3"
}

group = "it.jakegblp"
version = "2.0.0-alpha1"
var latestSkriptVersion = "2.12.2"
var latestMinecraftVersion = "1.21.10"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.destroystokyo.com/repository/maven-public/")
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
    compileOnly("org.apache.commons:commons-text:1.14.0")
    compileOnly("com.github.SkriptLang:Skript:$latestSkriptVersion")
    implementation("org.jspecify:jspecify:1.0.0")
    compileOnly("org.jetbrains:annotations:20.1.0")
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testCompileOnly("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")
    paperweight.paperDevBundle("$latestMinecraftVersion-R0.1-SNAPSHOT")
}

tasks {
    runServer {
        minecraftVersion(latestMinecraftVersion)
        downloadPlugins {
            url("https://github.com/SkriptLang/Skript/releases/download/$latestSkriptVersion/Skript-$latestSkriptVersion.jar")
            url("https://github.com/SkriptLang/skript-reflect/releases/download/v2.6.1/skript-reflect-2.6.1.jar")
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
tasks.withType(xyz.jpenilla.runtask.task.AbstractRun::class) {
    javaLauncher = javaToolchains.launcherFor {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(21)
    }
    jvmArgs("-XX:+AllowEnhancedClassRedefinition")
}
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
        if (!detectedVersion.contains("1.17")) {
            apply(plugin = "io.papermc.paperweight.userdev")
            dependencies {
                paperweight.paperDevBundle("$detectedVersion-R0.1-SNAPSHOT")
            }
        } else {
            apply(plugin = "io.github.patrick.remapper")
            dependencies {
                specialCompileOnly("org.spigotmc:spigot:$detectedVersion-R0.1-SNAPSHOT")
                specialCompileOnly("io.papermc.paper:paper-api:$detectedVersion-R0.1-SNAPSHOT")
            }
            project.tasks.named<RemapTask>("remap") {
                this.version = detectedVersion
            }
            project.tasks.named("build") {
                dependsOn("remap")
            }
        }
    } else if (!isSkriptModule){
        dependencies {
            compileOnly("io.papermc.paper:paper-api:$detectedVersion-R0.1-SNAPSHOT")
        }
    } else if (project.name != "build") {
        dependencies {
            specialCompileOnly("com.github.SkriptLang:Skript:$detectedVersion")
        }
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }

    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://jitpack.io/")
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.destroystokyo.com/repository/maven-public/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://libraries.minecraft.net")
    }

    dependencies {
        if (name != "common") {
            implementation(project(":common"))
        }
        if (name == "lusk") {
            compileOnly("com.github.SkriptLang:Skript:$latestSkriptVersion")
            compileOnly(project(":nms-core"))
            compileOnly(project(":skript-core"))
            compileOnly(project(":skript-factory"))
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

        if (name == "skript-factory" || isSkriptModule) {
            compileOnly(project(":skript-core"))
        }
        if (name == "skript-factory") {
            rootProject.subprojects.forEach {
                if (it.path.startsWith(":skript:")) {
                    specialCompileOnly(project(it.path))
                }
            }
        }
        if (name == "skript-shade") {
            implementation(project(":skript-factory"))
            implementation(project(":skript-core"))
        }
        compileOnly("org.apache.commons:commons-text:1.14.0")
        compileOnly("com.mojang:datafixerupper:1.0.20")
        compileOnly("io.netty:netty-all:4.1.87.Final")
        compileOnly("it.unimi.dsi:fastutil:8.5.15")
        compileOnly("org.jspecify:jspecify:1.0.0")
        compileOnly("org.jetbrains:annotations:20.1.0")
        compileOnly("org.projectlombok:lombok:1.18.36")
        annotationProcessor("org.projectlombok:lombok:1.18.36")
        testCompileOnly("org.projectlombok:lombok:1.18.36")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.36")
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

project(":skript-shade") {
    apply(plugin = "java")
    apply(plugin = "maven-publish")
    apply(plugin = "com.gradleup.shadow")

    dependencies {
        rootProject.subprojects.forEach {
            if (it.path.startsWith(":skript:")) {
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
                artifactId = "skript-shade"
                version = rootProject.version.toString()

                artifact(tasks.named("shadowJar"))
            }
        }
        repositories {
            mavenLocal()
        }
    }
}