/*
 * Name: Nurdos Ramazan
 * Course: CS-665 Software Designs & Patterns
 * Date: 11/10/2026
 * File Name: CustomerDataViaUsb.java
 * Description: Defines the legacy customer data interface.
 */

package edu.bu.met.cs665.customer;

/** Defines the customer operations of the legacy USB system. */
public interface CustomerDataViaUsb {

  /**
   * Prints the requested customer to the legacy system's output.
   *
   * @param customerId customer identifier
   * @throws IllegalArgumentException if the ID is nonpositive
   * @throws java.util.NoSuchElementException if the customer is absent
   */
  void printCustomer(int customerId);

  /**
   * Retrieves a customer from the legacy system.
   *
   * @param customerId customer identifier
   * @return the matching customer
   * @throws IllegalArgumentException if the ID is nonpositive
   * @throws java.util.NoSuchElementException if the customer is absent
   */
  Customer getCustomerViaUsb(int customerId);
}
