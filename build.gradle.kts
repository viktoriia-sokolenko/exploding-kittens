import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.Effort

plugins {
    application
    id("java")
    checkstyle
    id("com.github.spotbugs") version "6.0.25"
    jacoco
    id("info.solidsoft.pitest") version "1.15.0"
}

group = "nu.csse.sqe"
version = "1.0"

repositories {
    mavenCentral()
}

application {
    mainClass = "Code.Main"
}

dependencies {
    implementation("com.puppycrawl.tools:checkstyle:10.18.2")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // https://mvnrepository.com/artifact/org.easymock/easymock
    testImplementation("org.easymock:easymock:5.4.0")
    spotbugs("com.github.spotbugs:spotbugs:4.8.6")
    spotbugsPlugins("com.h3xstream.findsecbugs:findsecbugs-plugin:1.13.0")
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
    toolVersion = "10.18.2"
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

tasks.spotbugsTest {
    reports.create("html") {
        required = true
        outputLocation = file("build/reports/spotbugs/spotbugs_test.html")
        setStylesheet("fancy-hist.xsl")
    }
}

jacoco {
    toolVersion = "0.8.13"
}

tasks.jacocoTestReport {
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("reports/jacoco")
    }
}

tasks.build {
    dependsOn("pitest")
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
    finalizedBy(tasks.pitest)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

pitest {
    targetClasses = setOf("domain.*") //by default "${project.group}.*"
    targetTests = setOf("domain.*")
    junit5PluginVersion = "1.2.1"
    pitestVersion = "1.15.0" //not needed when a default PIT version should be used

    threads = 4
    outputFormats = setOf("HTML")
    timestampedReports = false
    testSourceSets.set(listOf(sourceSets.test.get()))
    mainSourceSets.set(listOf(sourceSets.main.get()))
    jvmArgs.set(listOf("-Xmx1024m"))
    useClasspathFile.set(true) //useful with bigger projects on Windows
    fileExtensionsToFilter.addAll("xml")
    exportLineCoverage = true
}