/*
 * Name: Nurdos Ramazan
 * Course: CS-665 Software Designs & Patterns
 * Date: 11/10/2026
 * File Name: CustomerLookupCli.java
 * Description: Handles terminal interactions through the new customer API.
 */

package edu.bu.met.cs665.cli;

import edu.bu.met.cs665.customer.CustomerDataViaHttps;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Accepts lookup and print requests through the new system's interface. */
public final class CustomerLookupCli {

  private final CustomerDataViaHttps customerData;

  /**
   * Creates a CLI that uses the supplied API.
   *
   * @param customerData the customer provider used for the session
   */
  public CustomerLookupCli(CustomerDataViaHttps customerData) {
    this.customerData = customerData;
  }

  /**
   * Runs the menu until the user exits.
   *
   * @param input the source of menu choices and customer IDs
   * @param output the destination for prompts and lookup results
   */
  public void run(Scanner input, PrintWriter output) {
    output.println("Customer data lookup");
    while (true) {
      output.println("1 Look up customer | 2 Print customer | 0 Exit");
      output.flush();
      if (!input.hasNextLine()) {
        break;
      }
      String choice = input.nextLine().strip();
      if (choice.equals("0")) {
        break;
      }
      if (!choice.equals("1") && !choice.equals("2")) {
        output.println("Choose 1, 2, or 0.");
        continue;
      }

      output.println("Enter customer ID:");
      output.flush();
      if (!input.hasNextLine()) {
        break;
      }
      showCustomer(choice, input.nextLine().strip(), output);
    }
    output.println("Goodbye.");
    output.flush();
  }

  /**
   * Runs the selected operation and reports invalid input or missing customers.
   *
   * @param choice the validated lookup or print menu choice
   * @param idText the customer ID typed by the user
   * @param output the destination for lookup results and errors
   */
  private void showCustomer(String choice, String idText, PrintWriter output) {
    try {
      int customerId = Integer.parseInt(idText);
      if (choice.equals("1")) {
        output.println(customerData.getCustomerViaHttps(customerId).displayText());
      } else {
        customerData.printCustomer(customerId);
      }
    } catch (IllegalArgumentException exception) {
      output.println("Enter a positive whole-number customer ID.");
    } catch (NoSuchElementException exception) {
      output.println(exception.getMessage());
    }
  }
}
