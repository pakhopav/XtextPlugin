import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()

sourceSets.getByName("main") {
    java.srcDir("src/main/java")
    java.srcDir("src/main/gen")
}

sourceSets.getByName("test") {
    java.srcDir("src/test/java")
}

plugins {
    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "1.5.10"
    // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.intellij") version "1.0"
    // gradle-changelog-plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
    id("org.jetbrains.changelog") version "1.1.2"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

// Configure project's dependencies
repositories {
    mavenCentral()
}
dependencies {
    testCompileOnly(group = "junit", name = "junit", version = "4.12")

    implementation(
        files(
            "libs/org.eclipse.emf.common_2.16.0.v20190528-0845.jar",
            "libs/org.eclipse.emf.ecore.change_2.14.0.v20190528-0725.jar",
            "libs/org.eclipse.emf.ecore.xmi_2.16.0.v20190528-0725.jar",
            "libs/org.eclipse.emf.ecore_2.18.0.v20190528-0845.jar",
            "libs/EntityEmf.jar",
            "libs/CalcEmf.jar",
            "libs/StatemachineEmf.jar",
            "libs/SimpleEmf.jar",
            "libs/SmallJavaEmf.jar",
            "libs/org.xtext.domainmodel.model.jar",
            "libs/org.xtext.xtext.model.jar",
            "libs/org.eclipse.xtext.xbase_2.22.0.v20200602-1114.jar"
        )
    )
    testCompileOnly(files("libs/org.eclipse.emf.common_2.16.0.v20190528-0845.jar"))
    testCompileOnly(files("libs/org.eclipse.emf.ecore.change_2.14.0.v20190528-0725.jar"))
    testCompileOnly(files("libs/org.eclipse.emf.ecore.xmi_2.16.0.v20190528-0725.jar"))
    testCompileOnly(files("libs/org.eclipse.emf.ecore_2.18.0.v20190528-0845.jar"))
    testCompileOnly(files("libs/EntityEmf.jar"))
    testCompileOnly(files("libs/ArithmeticsEmf.jar"))
    testCompileOnly(files("libs/CalcEmf.jar"))
    testCompileOnly(files("libs/StatemachineEmf.jar"))
    testCompileOnly(files("libs/SimpleEmf.jar"))
    testCompileOnly(files("libs/SmallJavaEmf.jar"))
    testCompileOnly(
        files(
            "libs/org.xtext.domainmodel.model.jar",
            "libs/org.xtext.xtext.model.jar",
            "libs/org.eclipse.xtext.xbase_2.22.0.v20200602-1114.jar"
        )
    )

}

// Configure gradle-intellij-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    pluginName.set(properties("pluginName"))
    version.set("2020.3")
    type.set(properties("platformType"))
    downloadSources.set(properties("platformDownloadSources").toBoolean())
    updateSinceUntilBuild.set(true)

    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
//    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
    plugins.set(
        listOf(
            "java",
            "maven", "gradle", "gradle-java", "Groovy", "org.jetbrains.idea.grammar:2020.3.1", "Kotlin"
        )
    )
}

// Configure gradle-changelog-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    version = properties("pluginVersion")
    groups = emptyList()
}

tasks {
    // Set the compatibility versions to 1.8
    withType<JavaCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(
            File(projectDir, "README.md").readText().lines().run {
                val start = "<!-- Plugin description -->"
                val end = "<!-- Plugin description end -->"

                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end))
            }.joinToString("\n").run { markdownToHTML(this) }
        )

        // Get the latest available change notes from the changelog file
        changeNotes.set(provider { changelog.getLatest().toHTML() })
    }

    runPluginVerifier {
        ideVersions.set(properties("pluginVerifierIdeVersions").split(',').map(String::trim).filter(String::isNotEmpty))
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token.set(System.getenv("PUBLISH_TOKEN"))
        // pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels.set(listOf(properties("pluginVersion").split('-').getOrElse(1) { "default" }.split('.').first()))
    }
}
