/*
 * Name: Nurdos Ramazan
 * Course: CS-665 Software Designs & Patterns
 * Date: 11/10/2026
 * File Name: UsbToHttpsAdapter.java
 * Description: Adapts the new system's API to the legacy USB API.
 */

package edu.bu.met.cs665.customer;

/** Presents a legacy USB customer provider through the new system's interface. */
public final class UsbToHttpsAdapter implements CustomerDataViaHttps {

  private final CustomerDataViaUsb legacyData;

  /**
   * Wraps the legacy API that will handle both customer operations.
   *
   * @param legacyData the legacy customer data provider
   */
  public UsbToHttpsAdapter(CustomerDataViaUsb legacyData) {
    this.legacyData = legacyData;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void printCustomer(int customerId) {
    legacyData.printCustomer(customerId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Customer getCustomerViaHttps(int customerId) {
    return legacyData.getCustomerViaUsb(customerId);
  }
}
