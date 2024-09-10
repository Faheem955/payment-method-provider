package com.payment.util;

import com.payment.bean.PaymentMethods;
import com.payment.bean.PaymentPlans;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentMethodResultExtractor implements ResultSetExtractor<List<PaymentMethods>> {
    @Override
    public List<PaymentMethods> extractData(ResultSet rs) throws SQLException {
        List<PaymentMethods> paymentMethods = new ArrayList<>();
        PaymentMethods paymentMethod = null;
        List<PaymentPlans> paymentPlans = null;

        while (rs.next()) {
            String name = rs.getString("name");
            if (paymentMethod == null || !paymentMethod.getName().equals(name)) {
                if (paymentMethod != null) {
                    paymentMethod.setPaymentPlans(paymentPlans);
                    paymentMethods.add(paymentMethod);
                }
                paymentMethod = new PaymentMethods();
                paymentMethod.setName(name);
                paymentMethod.setDisplayName(rs.getString("display_name"));
                paymentMethod.setPaymentType(rs.getString("payment_type"));
                paymentPlans = new ArrayList<>();
            }

            if(rs.getLong("id") >0) {
                PaymentPlans paymentPlan = new PaymentPlans();
                paymentPlan.setId(rs.getLong("id"));
                paymentPlan.setNetAmount(rs.getDouble("net_amount"));
                paymentPlan.setTaxAmount(rs.getDouble("tax_amount"));
                paymentPlan.setGrossAmount(rs.getDouble("gross_amount"));
                paymentPlan.setCurrency(rs.getString("currency"));
                paymentPlan.setDuration(rs.getString("duration"));

                paymentPlans.add(paymentPlan);
            }
        }

        if (paymentMethod != null) {
            paymentMethod.setPaymentPlans(paymentPlans);
            paymentMethods.add(paymentMethod);
        }

        return paymentMethods;
    }
}
