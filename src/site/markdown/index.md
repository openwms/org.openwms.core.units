# Purpose
This library defines some basic UOM (Unit Of Measure) types for OpenWMS.org. Beside the POJO types there are corresponding types for the
persistence layer and an API that uses [Jackson](https://github.com/FasterXML/jackson).

# Motivation
The motivation to create an own library for Units Of Measures is because a UOM is not just a simple string, nor a numeric value it always
consists of two parts: The absolute **amount** and the actual **unit**. To encapsulate these two values into one single new value for the
use in OpenWMS.org this library exists.

# Justification
Other Java libraries like the [JSR-385 Unit Of Measurement](https://github.com/unitsofmeasurement) target the same goal to implement units
in Java and are very comprehensive. The [JSR-385 Unit Of Measurement](https://github.com/unitsofmeasurement) provides additional API and
tools for different measurement systems. After analysis of the [JSR-385 Unit Of Measurement](https://github.com/unitsofmeasurement) we saw
it as too complicated for our use case and started with an own and simple implementation.

# Implementation
There is a general [Measurable](../../src/main/java/org/openwms/core/units/api/Measurable.java) interface that represents an UOM and offers
functionality to add and substract other Measurables. Arbitrary classes may implement this interface where those classes must take care of
the generic type nature of this interface in order to not compare apples with pears.

# Extendability
If someone wants to add additional units of measures, the [Measurable](../../src/main/java/org/openwms/core/units/api/Measurable.java) interface
needs to be implemented and registered in several converter classes, see the [Piece](../../src/main/java/org/openwms/core/units/api/Piece.java)
class for an example.

# Shortcomings
Even the [Measurable](../../src/main/java/org/openwms/core/units/api/Measurable.java) is strictly typed in the type of the amount and the unit,
the usage of this generic typed classes is cumbersome in Java. At least for the persistence layer with JPA this is awkward and annoying. So
instead of using typed interfaces in JPA entity classes we often use the raw type and check the correct type instance afterward in code.
This is a limitation down to the source code level that needs to be addressed.

# Resources
[![Build status](https://github.com/openwms/org.openwms.core.units/actions/workflows/master-build.yml/badge.svg)](https://github.com/openwms/org.openwms.core.units/actions/workflows/master-build.yml)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Quality](https://sonarcloud.io/api/project_badges/measure?project=org.openwms:org.openwms.core.units&metric=alert_status)](https://sonarcloud.io/dashboard?id=org.openwms:org.openwms.core.units)
[![Join the chat at https://gitter.im/openwms/org.openwms](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/openwms/org.openwms?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

### How to release
A release is built from the `master` branch. At first all required feature branches need to be merged into the `master` branch. Only if
the `master` branch builds successfully the release can be done. We follow the simple [Feature branch principle](https://de.atlassian.com/git/tutorials/comparing-workflows#feature-branch-workflow)

Checkout the master branch and build it locally with Javadocs and sources. Then use the release plugin to setup versions, git tags and
upload to artifact repository.

 ````
 $ mvn clean package -Drelease
 $ mvn release:prepare
 $ mvn release:perform
 ````
