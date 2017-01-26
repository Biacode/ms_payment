package com.sfl.pms.services.payment.notification.exception;

import com.sfl.pms.services.common.exception.EntityNotFoundForIdException;
import com.sfl.pms.services.payment.notification.model.PaymentProviderNotificationRequest;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 4/25/15
 * Time: 11:47 PM
 */
public class PaymentProviderNotificationRequestNotFoundForIdException extends EntityNotFoundForIdException {

    private static final long serialVersionUID = -823661773752499883L;

    /* Constructors */
    public PaymentProviderNotificationRequestNotFoundForIdException(final Long id) {
        super(id, PaymentProviderNotificationRequest.class);
    }
}
