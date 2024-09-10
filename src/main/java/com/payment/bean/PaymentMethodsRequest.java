package com.payment.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PaymentMethodsRequest {
    private List<PaymentMethods> paymentMethods;
}
