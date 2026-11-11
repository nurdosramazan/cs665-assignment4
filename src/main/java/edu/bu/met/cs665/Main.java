/*
 * Name: Nurdos Ramazan
 * Course: CS-665 Software Designs & Patterns
 * Date: 11/10/2026
 * File Name: Main.java
 * Description: Wires the USB customer store to the command-line application.
 */

package edu.bu.met.cs665;

import edu.bu.met.cs665.cli.CustomerLookupCli;
import edu.bu.met.cs665.customer.Customer;
import edu.bu.met.cs665.customer.CustomerDataViaHttps;
import edu.bu.met.cs665.customer.LegacyUsbCustomerData;
import edu.bu.met.cs665.customer.UsbToHttpsAdapter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

/** Starts the customer lookup application. */
public final class Main {
    /**
   * Runs the CLI with in-memory examples.
   *
   * @param args unused command-line arguments
   */
  public static void main(String[] args) {
    PrintWriter output = new PrintWriter(System.out, true);
    LegacyUsbCustomerData legacyData = new LegacyUsbCustomerData(
        List.of(
            new Customer(101, "Ada Morgan", "ada@example.test"),
            new Customer(202, "Mateo Silva", "mateo@example.test"),
            new Customer(303, "Nora Patel", "nora@example.test"),
            new Customer(404, "Julian Brooks", "julian@example.test")),
        output::println);
    CustomerDataViaHttps customerData = new UsbToHttpsAdapter(legacyData);
    new CustomerLookupCli(customerData)
        .run(new Scanner(System.in), output);
  }
}
