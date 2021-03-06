package com.sfl.pms.services.payment.common.impl.channel;

import com.sfl.pms.services.customer.model.Customer;
import com.sfl.pms.services.payment.common.dto.channel.PaymentProcessingChannelDto;
import com.sfl.pms.services.payment.common.dto.channel.ProvidedPaymentMethodProcessingChannelDto;
import com.sfl.pms.services.payment.common.model.channel.PaymentProcessingChannel;
import com.sfl.pms.services.payment.common.model.channel.ProvidedPaymentMethodProcessingChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 7/7/15
 * Time: 12:22 PM
 */
@Component
public class ProvidedPaymentMethodProcessingChannelHandlerImpl implements ProvidedPaymentMethodProcessingChannelHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProvidedPaymentMethodProcessingChannelHandlerImpl.class);

    /* Constructors */
    public ProvidedPaymentMethodProcessingChannelHandlerImpl() {
        LOGGER.debug("Initializing provided payment method processing channel handler");
    }

    @Override
    public void assertPaymentProcessingChannelDto(@Nonnull final PaymentProcessingChannelDto<? extends PaymentProcessingChannel> paymentProcessingChannelDto, @Nonnull final Customer customer) {
        LOGGER.debug("Asserting payment processing channel DTO - {}", paymentProcessingChannelDto);
        Assert.notNull(paymentProcessingChannelDto, "Payment processing DTO should not be null");
        Assert.notNull(paymentProcessingChannelDto.getPaymentProviderIntegrationType(), "Payment provider integration type in payment method type in payment processing DTO should not be null");
        Assert.notNull(customer, "Customer should not be null");
        Assert.isInstanceOf(ProvidedPaymentMethodProcessingChannelDto.class, paymentProcessingChannelDto);
        final ProvidedPaymentMethodProcessingChannelDto providedPaymentMethodProcessingChannelDto = (ProvidedPaymentMethodProcessingChannelDto) paymentProcessingChannelDto;
        Assert.notNull(providedPaymentMethodProcessingChannelDto.getPaymentMethodType(), "Payment method type in payment processing DTO should not be null");
    }

    @Nonnull
    @Override
    public PaymentProcessingChannel convertPaymentProcessingChannelDto(@Nonnull final PaymentProcessingChannelDto<? extends PaymentProcessingChannel> paymentProcessingChannelDto, @Nonnull final Customer customer) {
        assertPaymentProcessingChannelDto(paymentProcessingChannelDto, customer);
        LOGGER.debug("Converting payment processing channel DTO - {}", paymentProcessingChannelDto);
        final ProvidedPaymentMethodProcessingChannelDto providedPaymentMethodProcessingChannelDto = (ProvidedPaymentMethodProcessingChannelDto) paymentProcessingChannelDto;
        // Create domain model object
        final ProvidedPaymentMethodProcessingChannel paymentMethodProcessingChannel = new ProvidedPaymentMethodProcessingChannel();
        providedPaymentMethodProcessingChannelDto.updateDomainEntityProperties(paymentMethodProcessingChannel);
        return paymentMethodProcessingChannel;
    }
}
