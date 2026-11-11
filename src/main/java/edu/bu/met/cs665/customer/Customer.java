/*
 * Name: Nurdos Ramazan
 * Course: CS-665 Software Designs & Patterns
 * Date: 11/10/2026
 * File Name: Customer.java
 * Description: Represents a customer record
 */

package edu.bu.met.cs665.customer;

/**
 * Holds the customer data shared by the legacy and new systems.
 *
 * @param id the customer identifier
 * @param name the customer's full name
 * @param email the customer's email address
 */
public record Customer(int id, String name, String email) {

  /**
   * Returns a one-line description for the terminal.
   *
   * @return the customer ID, name, and email
   */
  public String displayText() {
    return "Customer #" + id + " | " + name + " | " + email;
  }
}
