/*
 * Name: Nurdos Ramazan
 * Course: CS-665 Software Designs & Patterns
 * Date: 11/10/2026
 * File Name: CustomerDataViaHttps.java
 * Description: Defines the customer data interface used by the new system.
 */

package edu.bu.met.cs665.customer;

/** Defines the customer operations expected by new-system clients. */
public interface CustomerDataViaHttps {

  /**
   * Prints the requested customer to the provider's output.
   *
   * @param customerId customer identifier
   * @throws IllegalArgumentException if the ID is nonpositive
   * @throws java.util.NoSuchElementException if the customer is absent
   */
  void printCustomer(int customerId);

  /**
   * Retrieves a customer through the new system's API.
   *
   * @param customerId customer identifier
   * @return the matching customer
   * @throws IllegalArgumentException if the ID is nonpositive
   * @throws java.util.NoSuchElementException if the customer is absent
   */
  Customer getCustomerViaHttps(int customerId);
}
