/*
 * Name: Nurdos Ramazan
 * Course: CS-665 Software Designs & Patterns
 * Date: 11/10/2026
 * File Name: LegacyUsbCustomerData.java
 * Description: Simulates the legacy USB customer API with in-memory records.
 */

package edu.bu.met.cs665.customer;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/** Implements the legacy USB API using in-memory collection of customers. */
public final class LegacyUsbCustomerData implements CustomerDataViaUsb {

  private final Map<Integer, Customer> customersById;
  private final PrintWriter output;

  /**
   * Creates a provider that looks up customers from an in-memory snapshot.
   *
   * @param customers the records to look up by ID
   * @param output the writer for legacy customer details
   */
  public LegacyUsbCustomerData(Collection<Customer> customers, PrintWriter output) {
    Map<Integer, Customer> records = new HashMap<>();
    for (Customer customer : customers) {
      records.put(customer.id(), customer);
    }
    this.customersById = Map.copyOf(records);
    this.output = new PrintWriter(output, true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void printCustomer(int customerId) {
    output.println(getCustomerViaUsb(customerId).displayText());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Customer getCustomerViaUsb(int customerId) {
    if (customerId <= 0) {
      throw new IllegalArgumentException("Customer ID must be positive.");
    }
    Customer customer = customersById.get(customerId);
    if (customer == null) {
      throw new NoSuchElementException("Customer #" + customerId + " was not found.");
    }
    return customer;
  }
}
