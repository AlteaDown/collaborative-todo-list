import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("multiplatform") version "1.5.20"
  kotlin("plugin.serialization") version "1.5.20"
  kotlin("kapt") version "1.5.20"
  kotlin("plugin.spring") version "1.5.20" apply false
  id("org.springframework.boot") version "2.5.3" apply false
  // kotlin("plugin.jpa") version "1.5.20" apply false
  id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
  //id("org.asciidoctor.convert") version "1.5.8" apply false
}


buildscript {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
  dependencies {
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.20")
    // classpath("com.android.tools.build:gradle:$android_build_tools")
  }
}

allprojects {
  repositories {
    google()
    mavenCentral()
  }
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
  mavenCentral()
  maven { url = uri("https://repo1.maven.org/maven2/") }
  maven { url = uri("https://repo.spring.io/milestone") }
  maven { url = uri("https://repo.spring.io/snapshot") }
  maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
}

extra["snippetsDir"] = file("build/generated-snippets")
extra["testcontainersVersion"] = "1.15.3"
extra["testcontainersVersion"] = "1.15.3"

val compilerArgs = listOf("-Xjsr305=strict", "-Xopt-in=kotlin.RequiresOptIn", "-Xopt-in=kotlin.js.ExperimentalJsExport")
tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().configureEach {
  kotlinOptions.freeCompilerArgs += compilerArgs
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
  kotlinOptions.freeCompilerArgs += compilerArgs
}

kotlin {
  jvm("spring") {
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    // apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    // apply(plugin = "org.asciidoctor.convert")

    tasks.withType<Test> {
      useJUnitPlatform()
    }
    /*configurations {
      compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
      }
    }
    dependencyManagement {
      imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
      }
    }

    tasks.withType<Test> {
      useJUnitPlatform()
    }
    tasks.asciidoctor {
      inputs.dir(snippetsDir)
      dependsOn(test)
    }
    tasks.test {
      outputs.dir(snippetsDir)
    }*/
  }
  js("react", IR) {
    binaries.executable()
    browser {
      commonWebpackConfig {
        cssSupport.enabled = true
        outputFileName = "main.js"
        outputPath = File(buildDir, "processedResources/spring/main/static")
      }
    }
  }
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation("com.benasher44:uuid:0.3.0")
        implementation("io.ktor:ktor-client-core:1.6.1")
        implementation("org.jetbrains.kotlinx:kotlinx-html:0.7.3")
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }
    val commonClientMain by creating {
      dependsOn(commonMain)
      dependencies {
        implementation("io.rsocket.kotlin:rsocket-core:0.13.1")
        implementation("io.rsocket.kotlin:rsocket-transport-ktor-client:0.13.1")
      }
    }
    val reactMain by getting {
      dependsOn(commonMain)
      dependsOn(commonClientMain)
      dependencies {
        implementation("io.ktor:ktor-client-js-kotlinMultiplatform:1.6.1")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react:17.0.2-pre.214-kotlin-1.5.20")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:17.0.2-pre.214-kotlin-1.5.20")

        implementation(npm("todomvc-app-css", "2.0.0"))
        implementation(npm("todomvc-common", "1.0.0"))
      }

      repositories {
        mavenCentral()
        maven { url = uri("https://repo1.maven.org/maven2/") }
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
        maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
      }
    }
    val springMain by getting {
      dependsOn(commonMain)
      dependencies {
        implementation("org.springframework.boot:spring-boot-starter-rsocket")
        implementation("org.springframework.boot:spring-boot-starter-webflux")

        implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

        // implementation("org.springframework.boot:spring-boot-starter-actuator")
        // implementation("org.springframework.boot:spring-boot-starter-amqp")
        // implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
        // implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        // implementation("org.springframework.boot:spring-boot-starter-data-redis")
        // implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
        // implementation("org.springframework.boot:spring-boot-starter-data-rest")
        // implementation("org.springframework.boot:spring-boot-starter-jdbc")
        // implementation("org.springframework.boot:spring-boot-starter-mail")
        // implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
        // implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
        // implementation("org.springframework.boot:spring-boot-starter-security")
        // implementation("org.springframework.boot:spring-boot-starter-validation")
        // implementation("org.springframework.boot:spring-boot-starter-webflux")
        // implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        // implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
        // implementation("org.jetbrains.kotlin:kotlin-reflect")
        // implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
        //runtimeOnly("mysql:mysql-connector-java")

        // not needed for now  runtimeOnly("io.micrometer:micrometer-registry-new-relic")
        //developmentOnly("org.springframework.boot:spring-boot-devtools")
        //kapt("org.springframework.boot:spring-boot-configuration-processor")
      }
    }

    val springTest by getting {
      dependencies {
        implementation("org.springframework.boot:spring-boot-starter-test")
        implementation("io.projectreactor:reactor-test")
        //implementation("org.springframework.amqp:spring-rabbit-test")
        //implementation("org.springframework.restdocs:spring-restdocs-webtestclient")
        //implementation("org.springframework.security:spring-security-test")

        // Not working
        /*implementation("org.testcontainers:junit-jupiter")
        implementation("org.testcontainers:mysql")
        implementation("org.testcontainers:rabbitmq")*/
      }
    }
  }
}

tasks.getByName<Copy>("springProcessResources") {
  dependsOn(tasks.getByName("reactBrowserDevelopmentWebpack"))
}
