# Utilizing Legacy Systems

| CS-665     | Software Designs & Patterns |
|------------|-----------------------------|
| Name       | Nurdos Ramazan              |
| Date       | November 10, 2026           |
| Course     | Fall 2026                   |
| Assignment | 4                           |

A command line client uses the new system's customer interface to retrieve
records through the legacy system's API. The demo simulates the legacy USB system with
four fictional in-memory customers.

## Features included

- Java 25 Maven configuration with `maven.compiler.release` set to `25`
- Log4j 2 logging configuration in `src/main/resources/log4j2.xml`
- JUnit testing support
- JaCoCo coverage reports
- SpotBugs static analysis
- Checkstyle configuration under `config/`

## Prerequisites

Before building, install:

- Java 25 JDK
- Apache Maven 3.9 or newer

Check your environment with:

```bash
java -version
mvn -version
```

If Maven reports an older Java version, update `JAVA_HOME` and `PATH` so Maven uses JDK 25. For example on macOS:

```bash
export JAVA_HOME="$(/usr/libexec/java_home -v 25)"
export PATH="$JAVA_HOME/bin:$PATH"
mvn -version
```

## Build and run

Compile the project:

```bash
mvn clean compile
```

Run the unit tests:

```bash
mvn clean test
```

Generate coverage and build reports:

```bash
mvn clean verify
```

Run the sample application:

```bash
mvn exec:java
```

Launch the SpotBugs GUI:

```bash
mvn spotbugs:gui
```

Generate the Checkstyle report:

```bash
mvn checkstyle:checkstyle
```

## Useful reports

After running verification, inspect the generated reports in the `target/` directory:

- JaCoCo: `target/site/jacoco/index.html`
- Checkstyle HTML: `target/site/checkstyle.html`

The four JUnit 4 tests cover lookup, printing and scripted
CLI sessions. Each test uses Given, When, and Then comments.

### User example

Enter these five lines to loook up Nora, print Julian, and exit.

```text
1
303
2
404
0
```

## Design and implementation

### Object Adapter pattern

`CustomerDataViaHttps` is the target interface expected by the new system.
`CustomerDataViaUsb` is the legacy interface, implemented by `LegacyUsbCustomerData`.
The CLI only knows `CustomerDataViaHttps`. `Main` supplies the adapter at startup.
`UsbToHttpsAdapter` implements the target interface and holds a reference to a legacy
provider. A call to `getCustomerViaHttps(101)` becomes `getCustomerViaUsb(101)` when the
adapter forwards it to the legacy provider. The same `Customer` value returns to the CLI
through the adapter.

This is loose coupling because the CLI depends on the new interface and the adapter
depends on the legacy interface. `Main` chooses the concrete implementations.

### Why composition fits this adapter

As discussed in class, composition lets an Object Adapter wrap any provider that
implements the adaptee interface and keeps the adapter separate from the provider's
implementation. Here, `UsbToHttpsAdapter`
uses `CustomerDataViaUsb` to talk to the in-memory provider. The adapter can also
accept another provider that preserves the behavior promised by that interface.

A subclass-based adapter would be tied to a specific legacy class and its constructor.
It would also require that class to permit subclassing. `LegacyUsbCustomerData` is
`final`, and the current adapter can wrap it directly.

### Flexibility

A provider backed by a remote HTTPS REST API could implement `CustomerDataViaHttps` and
be supplied to the CLI in `Main`.
Demo records can be added or removed in `Main`. The lookup and print operations work
with the supplied collection of customers.

### Simplicity and avoiding duplication

`Main` wires the components together. `CustomerLookupCli` handles prompts and user
feedback. The adapter translates calls, the USB provider retrieves records, and the
immutable `Customer` record holds the result.

The adapter forwards `printCustomer(101)` to the legacy provider, preserving its print
behavior. The legacy print method calls the same `getCustomerViaUsb` method used for
lookups, so both operations share ID validation and record retrieval. For example,
requesting customer 0 through either operation reaches the same positive-ID check.
An unknown ID also produces the same missing-customer message. Updating those rules
in `getCustomerViaUsb` applies to both operations.

The CLI lookup and legacy print method both call `Customer.displayText()`. Changing
the order of the name and email, or adding a label to the output, takes one edit in
that method and applies to both menu choices. The four sample customers all use these
same methods, so adding a customer adds a record to the list in `Main`. This keeps
validation and display behavior consistent as the customer list grows.

## Assumptions and behavior

- The adapted calls run locally, so the demo needs no server, credentials, or physical
  USB device.
- The legacy provider stores the supplied records in memory by customer ID.
  A customer ID is a positive Java `int`.
- An absent record raises `NoSuchElementException`.
- The in-memory records are fictional. Ada Morgan is 101, Mateo Silva is 202, Nora Patel
  is 303, and Julian Brooks is 404. Both operations show the ID, name, and email.
  Lookup returns a customer to its caller for display, while print writes through the
  legacy provider's output.
- Choose `0` to exit. End of input also exits.

## UML class diagram

The [UML class diagram](output/uml-class.pdf) shows the Object Adapter relationships.
The client (`CustomerLookupCli`) uses the target interface (`CustomerDataViaHttps`).
`UsbToHttpsAdapter` implements that target and holds the adaptee through the
`CustomerDataViaUsb` interface. `LegacyUsbCustomerData` implements that legacy interface
and looks up records in its in-memory map.
