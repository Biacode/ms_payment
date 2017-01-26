package com.sfl.pms.services.payment.method.model.individual;

import com.sfl.pms.services.payment.method.model.PaymentMethodDefinition;
import com.sfl.pms.services.payment.method.model.PaymentMethodDefinitionType;
import com.sfl.pms.services.payment.method.model.PaymentMethodType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 7/25/15
 * Time: 1:25 PM
 */
@Entity
@DiscriminatorValue(value = "INDIVIDUAL")
public class IndividualPaymentMethodDefinition extends PaymentMethodDefinition {

    private static final long serialVersionUID = -4351590509644227864L;

    /* Properties */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method_type", nullable = true)
    private PaymentMethodType paymentMethodType;

    /* Constructors */
    public IndividualPaymentMethodDefinition() {
        setType(PaymentMethodDefinitionType.INDIVIDUAL);
    }

    /* Properties getters and setters */
    public PaymentMethodType getPaymentMethodType() {
        return paymentMethodType;
    }

    public void setPaymentMethodType(final PaymentMethodType paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndividualPaymentMethodDefinition)) {
            return false;
        }
        final IndividualPaymentMethodDefinition that = (IndividualPaymentMethodDefinition) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getPaymentMethodType(), that.getPaymentMethodType());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getPaymentMethodType());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("paymentMethodType", this.getPaymentMethodType());
        return builder.build();
    }
}
