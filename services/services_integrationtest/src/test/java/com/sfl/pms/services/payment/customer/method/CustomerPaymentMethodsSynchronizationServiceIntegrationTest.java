package com.sfl.pms.services.payment.customer.method;

import com.sfl.pms.persistence.utility.PersistenceUtilityService;
import com.sfl.pms.services.customer.model.Customer;
import com.sfl.pms.services.payment.customer.method.adyen.CustomerPaymentMethodAdyenInformationService;
import com.sfl.pms.services.payment.customer.method.model.CustomerCardPaymentMethod;
import com.sfl.pms.services.payment.customer.method.model.CustomerPaymentMethod;
import com.sfl.pms.services.payment.customer.method.model.CustomerPaymentMethodProviderInformation;
import com.sfl.pms.services.payment.customer.method.model.adyen.CustomerPaymentMethodAdyenInformation;
import com.sfl.pms.services.payment.provider.model.PaymentProviderType;
import com.sfl.pms.services.test.AbstractServiceIntegrationTest;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/7/15
 * Time: 11:13 AM
 */
public class CustomerPaymentMethodsSynchronizationServiceIntegrationTest extends AbstractServiceIntegrationTest {

    /* Dependencies */
    @Autowired
    private CustomerPaymentMethodsSynchronizationService customerPaymentMethodsSynchronizationService;

    @Autowired
    private PersistenceUtilityService persistenceUtilityService;

    @Autowired
    private CustomerPaymentMethodAdyenInformationService customerPaymentMethodAdyenInformationService;

    @Autowired
    private CustomerPaymentMethodService customerPaymentMethodService;

    /* Constructors */
    public CustomerPaymentMethodsSynchronizationServiceIntegrationTest() {
    }

    /* Test methods */
    @Test
    public void testSynchronizeAdyenCustomerPaymentMethods() {
        if (!isEnablePaymentIntegrationTests()) {
            return;
        }
        // Prepare data
        final String customerEmail = "dummy+" + UUID.randomUUID().toString() + "@dummy.com";
        final Customer customer = getServicesTestHelper().createCustomer(customerEmail);
        final String masterCardEncryptedData = getServicesTestHelper().createMasterCardEncryptedInformation();
        final Pair<CustomerCardPaymentMethod, Long> paymentMethodAddingResult = getServicesTestHelper().createCustomerEncryptedCardPaymentMethodWithPaymentProvider(customer, masterCardEncryptedData);
        final CustomerCardPaymentMethod customerCardPaymentMethod = persistenceUtilityService.initializeAndUnProxy(paymentMethodAddingResult.getKey());
        final CustomerPaymentMethodProviderInformation customerPaymentMethodProviderInformation = persistenceUtilityService.initializeAndUnProxy(customerCardPaymentMethod.getProviderInformation());
        assertTrue(customerPaymentMethodProviderInformation instanceof CustomerPaymentMethodAdyenInformation);
        CustomerPaymentMethodAdyenInformation adyenInformation = (CustomerPaymentMethodAdyenInformation) customerPaymentMethodProviderInformation;
        final String originalRecurringDetailsReference = adyenInformation.getRecurringDetailReference();
        final String updatedRecurringDetailsReference = originalRecurringDetailsReference + "_updated";
        final Set<String> expectedReferencesSet = new HashSet<>(Arrays.asList(originalRecurringDetailsReference, updatedRecurringDetailsReference));
        // Flush and clear
        flushAndClear();
        // Load customer payment methods, there should be only one
        List<CustomerPaymentMethod> customerPaymentMethods = customerPaymentMethodService.getPaymentMethodsForCustomer(customer.getId());
        assertEquals(1, customerPaymentMethods.size());
        // Update recurring details reference for Adyen provider information, which means that synchronization process should recreate it
        adyenInformation = customerPaymentMethodAdyenInformationService.updatePaymentMethodInformationRecurringDetailReference(adyenInformation.getId(), updatedRecurringDetailsReference);
        flushAndClear();
        // Trigger synchronization process
        customerPaymentMethodsSynchronizationService.synchronizeCustomerPaymentMethods(customer.getId(), PaymentProviderType.ADYEN);
        flushAndClear();
        // Reload payment methods
        customerPaymentMethods = customerPaymentMethodService.getPaymentMethodsForCustomer(customer.getId());
        assertEquals(2, customerPaymentMethods.size());
        // Check that both recurring details references are found
        customerPaymentMethods.forEach(customerPaymentMethod -> {
            final CustomerPaymentMethodProviderInformation currentPaymentMethodProviderInformation = persistenceUtilityService.initializeAndUnProxy(customerPaymentMethod.getProviderInformation());
            assertTrue(currentPaymentMethodProviderInformation instanceof CustomerPaymentMethodAdyenInformation);
            final CustomerPaymentMethodAdyenInformation currentInformation = (CustomerPaymentMethodAdyenInformation) currentPaymentMethodProviderInformation;
            assertTrue(expectedReferencesSet.contains(currentInformation.getRecurringDetailReference()));
        });

    }
}
