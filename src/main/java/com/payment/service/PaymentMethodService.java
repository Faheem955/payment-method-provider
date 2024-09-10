package com.payment.service;

import com.payment.bean.PaymentMethods;
import com.payment.repository.PaymentMethodRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodService {

    @Autowired
    PaymentMethodRepo paymentRepo;

    public String addPaymentMethods(List<PaymentMethods> paymentMethods) {
        String response = null;

        if(null!= paymentMethods) {
            for (PaymentMethods pm : paymentMethods) {
                response = paymentRepo.addPaymentMethods(pm);
            }
        }
        return response;
    }

    public String updatePaymentMethod(PaymentMethods paymentMethods, Long id) {
        String response = null;
        if(null != paymentMethods && null != id && id>0) {
            response = paymentRepo.updatePaymentMethod(paymentMethods, id);
        }
        return response;
    }

    public List<PaymentMethods> getPaymentMethods(Long id, String name) {
        List<PaymentMethods> response = null;
        response = paymentRepo.getAllPaymentMethods(id, name);
        return response;
    }

}

