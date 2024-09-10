package com.payment.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PaymentMethods {
    private String name;
    private String displayName;
    private String paymentType;
    private List<PaymentPlans> paymentPlans;
}
