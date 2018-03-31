project {

  modelVersion '4.0.0'
  groupId 'com.github.yassine.artifacts'
  artifactId 'guice-artifacts'
  version '0.1.1'

  properties {
    'deps.version.spock' '1.1-groovy-2.4'
    'deps.version.typetools' '0.5.0'
    'deps.version.commons-io' '2.5'
    'deps.version.groovy' '2.4.13'
    'deps.version.jboss-logging' '3.3.1.Final'
    'deps.version.slf4j' '1.7.21'
    'deps.version.log4j' '2.10.0'
    'deps.version.guava' '21.0'
    'deps.version.guice' '4.1.0'
    'deps.version.lombok' '1.16.18'
    'deps.version.logging-annotations' '2.1.0.Final'
    'deps.version.junit' '4.12'
    'deps.version.commons-lang3' '3.5'
    'project.build.sourceEncoding' 'UTF-8'
  }

  dependencies {
    dependency 'com.fasterxml.jackson.core:jackson-annotations:2.8.5'
    dependency 'com.fasterxml.jackson.core:jackson-core:2.8.5'
    dependency 'com.fasterxml.jackson.core:jackson-databind:2.8.5'
    dependency 'com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.8.5'
    dependency 'io.github.lukehutch:fast-classpath-scanner:2.0.8'
    dependency 'org.glassfish:javax.el:3.0.1-b08'
    dependency 'org.hibernate.validator:hibernate-validator:6.0.5.Final'
    dependency 'com.google.auto.service:auto-service:1.0-rc3'
    dependency 'org.freemarker:freemarker:2.3.26-incubating'
    dependency 'com.google.guava:guava:${deps.version.guava}'
    dependency 'com.google.inject:guice:${deps.version.guice}'
    dependency 'com.google.inject.extensions:guice-multibindings:${deps.version.guice}'
    dependency 'org.apache.commons:commons-lang3:${deps.version.commons-lang3}'
    dependency 'commons-io:commons-io:${deps.version.commons-io}'
    dependency 'org.projectlombok:lombok:${deps.version.lombok}:provided'
    dependency 'net.jodah:typetools:${deps.version.typetools}'
    //logging
    dependency 'org.apache.logging.log4j:log4j-api:${deps.version.log4j}'
    dependency 'org.apache.logging.log4j:log4j-core:${deps.version.log4j}'
    dependency 'org.apache.logging.log4j:log4j-slf4j-impl:${deps.version.log4j}'
    dependency 'org.apache.logging.log4j:log4j-1.2-api:${deps.version.log4j}'
    dependency 'org.slf4j:slf4j-api:${deps.version.slf4j}'
    //testing
    dependency 'org.spockframework:spock-core:${deps.version.spock}:test'
    dependency 'org.spockframework:spock-guice:${deps.version.spock}:test'
    dependency 'org.codehaus.groovy:groovy-all:${deps.version.groovy}:test'
    dependency 'junit:junit:${deps.version.junit}:test'
    dependency 'cglib:cglib-nodep:3.2.5:test'
  }

  build {
    pluginManagement {
      plugins {
        plugin {
          artifactId 'maven-compiler-plugin'
          version '3.7.0'
          configuration {
            source '8'
            target '8'
          }
        }
      }
    }
    plugins {
      plugin {
        artifactId 'maven-compiler-plugin'
      }
      plugin {
        groupId 'org.jacoco'
        artifactId 'jacoco-maven-plugin'
        version '0.8.0'
        executions {
          execution {
            id 'prepare-agent'
            phase 'test-compile'
            goals {
              goal 'prepare-agent'
            }
            configuration {
              propertyName 'surefireArgLine'
              destFile '${project.build.directory}/coverage-reports/jacoco-ut.exec'
            }
          }
          execution {
            id 'post-test-reports'
            phase 'post-integration-test'
            goals {
              goal 'report'
            }
            configuration {
              dataFile '${project.build.directory}/coverage-reports/jacoco-ut.exec'
              outputDirectory '${project.reporting.outputDirectory}/code-coverage'
            }
          }
        }
      }
      plugin {
        groupId 'org.codehaus.gmavenplus'
        artifactId 'gmavenplus-plugin'
        version '1.6'
        executions {
          execution {
            id 'generate-unit-tests'
            goals {
              goal 'compileTests'
              goal 'addTestSources'
            }
            configuration {
              testSources {
                testSource {
                  directory '${project.basedir}/src/test/unit-tests'
                  includes {
                    include '**/*.groovy'
                  }
                }
              }
              outputDirectory '${project.build.directory}/unit-tests'
            }
          }
          execution {
            id 'generate-functional-tests'
            goals {
              goal 'compileTests'
              goal 'addTestSources'
            }
            configuration {
              testSources {
                testSource {
                  directory '${project.basedir}/src/test/functional-tests'
                  includes {
                    include '**/*.groovy'
                  }
                }
              }
              outputDirectory '${project.build.directory}/functional-tests'
            }
          }
        }
      }
      plugin {
        artifactId 'maven-surefire-plugin'
        version '2.20.1'
        executions {
          execution {
            id 'functional-tests'
            goals {
              goal 'test'
            }
            configuration {
              testClassesDirectory '${project.build.directory}/functional-tests'
            }
          }
        }
        configuration{
          useFile 'false'
          testClassesDirectory '${project.build.directory}/unit-tests'
          includes {
            include '**/*Spec'
          }
          additionalClasspathElements {
            additionalClasspathElement '${project.basedir}/src/test/resources'
            additionalClasspathElement '${project.build.testOutputDirectory}'
          }
          argLine '${surefireArgLine}'
        }
      }
      plugin {
        groupId 'org.eluder.coveralls'
        artifactId 'coveralls-maven-plugin'
        version '4.3.0'
        configuration {
          repoToken '${env.COVERALLS_REPO_KEY}'
          jacocoReports '${project.reporting.outputDirectory}/code-coverage/jacoco.xml'
        }
      }
    }
  }
}
