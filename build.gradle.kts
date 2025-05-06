import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.Effort

        plugins {
            id("java")
            checkstyle
            id("com.github.spotbugs") version "6.0.25"
        }

group = "nu.csse.sqe"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

tasks.compileJava {
    options.release = 11
}

tasks.test {
    useJUnitPlatform()
}
tasks.withType<Checkstyle>().configureEach {
    reports {
        xml.required = false
        html.required = true
        html.stylesheet = resources.text.fromFile("config/xsl/checkstyle-noframes-severity-sorted.xsl")
    }
}

checkstyle {
    isIgnoreFailures = false
}
// Spotbugs README: https://github.com/spotbugs/spotbugs-gradle-plugin#readme
// SpotBugs Gradle Plugin: https://spotbugs.readthedocs.io/en/latest/gradle.html
spotbugs {
    ignoreFailures = false
    showStackTraces = true
    showProgress = true
    effort = Effort.DEFAULT
    reportLevel = Confidence.DEFAULT
    //omitVisitors = listOf("FindNonShortCircuit")
    reportsDir = file("spotbugs")
    //includeFilter = file("include.xml")
    //excludeFilter = file("exclude.xml")
    //onlyAnalyze = listOf("com.foobar.MyClass", "com.foobar.mypkg.*")
    maxHeapSize = "1g"
    extraArgs = listOf("-nested:false")
    //jvmArgs = listOf("-Duser.language=ja") // set user language to japanese
}

tasks.spotbugsMain {
    reports.create("html") {
        required = true
        outputLocation = layout.buildDirectory.file("reports/spotbugs/spotbugs.html")
        setStylesheet("fancy-hist.xsl")
    }
}

        // I made this so we can have an easier way to convert tabs to space
        // mostly needd this for my env though

        tasks.register("fixIndentation") {
            description = "Converts space indentation to tabs in Java files"
            group = "formatting"

            doLast {
                println("Converting spaces to tabs in Java files...")
                val mainJavaFiles = project.fileTree("src/main/java") {
                    include("**/*.java")
                }
                val testJavaFiles = project.fileTree("src/test/java") {
                    include("**/*.java")
                }
                val allJavaFiles = mainJavaFiles + testJavaFiles
                allJavaFiles.forEach { file ->
                    println("Processing file: ${file.path}")

                    val content = file.readText()
                    val fixedContent = content.replace(Regex("(?m)^([ ]{4})+")) { match ->
                        "\t".repeat(match.value.length / 4)
                    }

                    if (content != fixedContent) {
                        file.writeText(fixedContent)
                        println("Fixed indentation in: ${file.path}")
                    } else {
                        println("No changes needed in: ${file.path}")
                    }
                }

                println("Indentation conversion complete.")
            }
        }