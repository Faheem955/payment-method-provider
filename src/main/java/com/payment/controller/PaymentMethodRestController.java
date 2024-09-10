package com.payment.controller;

import com.payment.bean.PaymentMethods;
import com.payment.bean.PaymentMethodsRequest;
import com.payment.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PaymentMethodRestController {

    @Autowired
    PaymentMethodService paymentService;

    @PostMapping("/v1.0/configuration/payment-methods")
    public ResponseEntity<String> addPaymentMethod(@RequestBody PaymentMethodsRequest paymentMethodsRequest) {
        List<PaymentMethods> paymentMethodsList = paymentMethodsRequest.getPaymentMethods();
        return ResponseEntity.ok(paymentService.addPaymentMethods(paymentMethodsList));
    }

    /*
    In update, Its being considered that the id provided in request parameter is of payment plan
    and not of payment method as there is no mention of any id of payment method in the task.
     */
    @PutMapping("/v1.0/configuration/payment-methods")
    public ResponseEntity<String> updatePaymentMethod(@RequestBody PaymentMethodsRequest paymentMethodsRequest, @RequestParam(name = "payment-methods") Long id) {
        List<PaymentMethods> paymentMethodsList = paymentMethodsRequest.getPaymentMethods();
        return ResponseEntity.ok(paymentService.updatePaymentMethod(paymentMethodsList.get(0), id));
    }

    @GetMapping("/v1.0/configuration/payment-methods")
    public ResponseEntity<PaymentMethodsRequest> getPaymentMethod(@RequestParam(required = false) Long id, @RequestParam(required = false) String name) {
        PaymentMethodsRequest response = new PaymentMethodsRequest();
        List<PaymentMethods> res = paymentService.getPaymentMethods(id, name);
        response.setPaymentMethods(res);
        return ResponseEntity.ok(response);
    }
}
