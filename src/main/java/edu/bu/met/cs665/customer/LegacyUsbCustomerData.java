/*
 * Name: Nurdos Ramazan
 * Course: CS-665 Software Designs & Patterns
 * Date: 11/10/2026
 * File Name: LegacyUsbCustomerData.java
 * Description: Simulates the legacy USB customer API with in-memory records.
 */

package edu.bu.met.cs665.customer;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Implements the legacy USB API using in-memory collection of customers. */
public final class LegacyUsbCustomerData implements CustomerDataViaUsb {

  private final Map<Integer, Customer> customersById;
  private final Consumer<String> printLine;

  /**
   * Creates a provider that looks up customers from an in-memory snapshot.
   *
   * @param customers the records to look up by ID
   * @param printLine the operation that writes one line to the legacy output
   * @throws IllegalStateException if two records have the same ID
   */
  public LegacyUsbCustomerData(Collection<Customer> customers, Consumer<String> printLine) {
    this.customersById = customers.stream()
        .collect(Collectors.toUnmodifiableMap(Customer::id, Function.identity()));
    this.printLine = printLine;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void printCustomer(int customerId) {
    printLine.accept(getCustomerViaUsb(customerId).displayText());
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
