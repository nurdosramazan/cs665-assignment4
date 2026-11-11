/*
 * Name: Nurdos Ramazan
 * Course: CS-665 Software Designs & Patterns
 * Date: 11/10/2026
 * File Name: CustomerSystemTest.java
 * Description: Verifies the USB adapter and complete command-line sessions.
 */

package edu.bu.met.cs665;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import edu.bu.met.cs665.cli.CustomerLookupCli;
import edu.bu.met.cs665.customer.Customer;
import edu.bu.met.cs665.customer.CustomerDataViaHttps;
import edu.bu.met.cs665.customer.LegacyUsbCustomerData;
import edu.bu.met.cs665.customer.UsbToHttpsAdapter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import org.junit.Test;

/** Tests API operations and CLI sessions with in-memory customer records. */
public class CustomerSystemTest {

  private static final Customer ADA = new Customer(101, "Ada Morgan", "ada@example.test");
  private static final Customer MATEO = new Customer(202, "Mateo Silva", "mateo@example.test");

  /** Verifies that the new API returns the record matching the requested ID. */
  @Test
  public void testLookupReturnsMatchingCustomer() {
    // Given: the legacy provider contains two customers.
    StringWriter transcript = new StringWriter();
    CustomerDataViaHttps api = newApi(List.of(ADA, MATEO), new PrintWriter(transcript));

    // When: a new-system client requests the second customer.
    Customer customer = api.getCustomerViaHttps(202);

    // Then: the lookup returns the matching record and leaves printing to the caller.
    assertEquals(MATEO, customer);
    assertEquals("", transcript.toString());
  }

  /** Verifies that printing through the adapter reaches the legacy output. */
  @Test
  public void testPrintWritesLegacyCustomerDetails() {
    // Given: Ada's record and a captured legacy print destination.
    StringWriter transcript = new StringWriter();
    CustomerDataViaHttps api = newApi(List.of(ADA), new PrintWriter(transcript));

    // When: the new API is asked to print customer 101.
    api.printCustomer(101);

    // Then: the legacy output contains the full customer record.
    assertEquals(List.of("Customer #101 | Ada Morgan | ada@example.test"),
        transcript.toString().lines().toList());
  }

  /** Verifies the missing-customer contract for both API operations. */
  @Test
  public void testMissingCustomerIsReportedByBothOperations() {
    // Given: an empty customer store.
    StringWriter transcript = new StringWriter();
    CustomerDataViaHttps api = newApi(List.of(), new PrintWriter(transcript));

    // When: the client tries to retrieve and print a missing record.
    NoSuchElementException lookupError =
        assertThrows(NoSuchElementException.class, () -> api.getCustomerViaHttps(999));
    NoSuchElementException printError =
        assertThrows(NoSuchElementException.class, () -> api.printCustomer(999));

    // Then: both operations identify the missing customer and print no partail result.
    assertEquals("Customer #999 was not found.", lookupError.getMessage());
    assertEquals(lookupError.getMessage(), printError.getMessage());
    assertEquals("", transcript.toString());
  }

  /** Verifies a full lookup, print, and exit session. */
  @Test
  public void testCliLooksUpAndPrintsCustomersBeforeExit() {
    // Given: two available records and a user who selects each operation.
    String commands = "1\n 101 \n2\n202\n0\n";

    // When: the CLI runs through the commands.
    List<String> transcript = runSession(commands).lines().toList();

    // Then: both operations show the correct details in order and the session ends.
    assertEquals(
        List.of(
            "Customer data lookup",
            "1 Look up customer | 2 Print customer | 0 Exit",
            "Enter customer ID:",
            "Customer #101 | Ada Morgan | ada@example.test",
            "1 Look up customer | 2 Print customer | 0 Exit",
            "Enter customer ID:",
            "Customer #202 | Mateo Silva | mateo@example.test",
            "1 Look up customer | 2 Print customer | 0 Exit",
            "Goodbye."),
        transcript);
  }

  /** Builds an adapter with the supplied customers and captured print output. */
  private CustomerDataViaHttps newApi(List<Customer> customers, PrintWriter output) {
    return new UsbToHttpsAdapter(new LegacyUsbCustomerData(customers, output::println));
  }

  /** Runs a session with scripted input and captures output from the CLI and legacy printer. */
  private String runSession(String commands) {
    StringWriter transcript = new StringWriter();
    PrintWriter output = new PrintWriter(transcript);
    try (Scanner input = new Scanner(commands)) {
      new CustomerLookupCli(newApi(List.of(ADA, MATEO), output)).run(input, output);
    }
    return transcript.toString();
  }
}
