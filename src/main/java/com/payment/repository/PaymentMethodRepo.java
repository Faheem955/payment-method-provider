package com.payment.repository;

import com.payment.bean.PaymentMethods;
import com.payment.bean.PaymentPlans;
import com.payment.util.PaymentMethodResultExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PaymentMethodRepo {

    @Autowired
    @Qualifier("h2local")
    private DataSource dataSource;



    public String addPaymentMethods(PaymentMethods paymentMethods) {
        String response = "Failure";
        Long paymentMethodId = 0L;
        List<Object[]> batchParams = new ArrayList<>();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        try{

            jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement("INSERT INTO payment_method (name, display_name, payment_type) VALUES (?, ?, ?)", new String[] {"id"});
                    ps.setString(1, paymentMethods.getName());
                    ps.setString(2, paymentMethods.getDisplayName());
                    ps.setString(3, paymentMethods.getPaymentType());
                    return ps;
                }
            }, keyHolder);

            if(null != keyHolder.getKey()){
                paymentMethodId = keyHolder.getKey().longValue();
            }

            if(paymentMethodId > 0 && null != paymentMethods.getPaymentPlans()){

                for (PaymentPlans pp : paymentMethods.getPaymentPlans()) {
                    batchParams.add(new Object[] {
                            pp.getNetAmount(), pp.getTaxAmount(), pp.getGrossAmount(), pp.getCurrency(), pp.getDuration(), paymentMethodId
                    });
                }

                jdbcTemplate.batchUpdate("INSERT INTO payment_plan (net_amount, tax_amount, gross_amount, currency, duration, payment_method_id) VALUES (?, ?, ?, ?, ?, ?)", batchParams);
            }
            response = "Success";
        }catch(Exception e){
            response = "Failure";
        }

        return response;
    }

    public String updatePaymentMethod(PaymentMethods paymentMethods, Long id) {
        String response = "Failure";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        PaymentPlans pPlan = null;
        try{
            if(null!= paymentMethods.getPaymentPlans() && !paymentMethods.getPaymentPlans().isEmpty()) {
                pPlan = paymentMethods.getPaymentPlans().get(0);

                jdbcTemplate.update("UPDATE payment_plan SET net_amount = ?, tax_amount = ?, gross_amount = ?, currency = ?, duration = ? WHERE id = ?;",
                        pPlan.getNetAmount(), pPlan.getTaxAmount(), pPlan.getGrossAmount(), pPlan.getCurrency(), pPlan.getDuration(), id);
            }
            Long paymentMethodId = jdbcTemplate.queryForObject("select payment_method_id from payment_plan where id = ?", new Object[]{id}, Long.class);

            jdbcTemplate.update("UPDATE payment_method SET name = ?, display_name = ?, payment_type = ? WHERE id = ?",
                    paymentMethods.getName(), paymentMethods.getDisplayName(), paymentMethods.getPaymentType(), paymentMethodId);

            response = "Success";
        }catch(Exception e){
            response = "Failure";
        }
        return response;
    }

    public List<PaymentMethods> getAllPaymentMethods(Long id, String name) {
        List<PaymentMethods> paymentMethods = new ArrayList<>();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        StringBuilder query = new StringBuilder();
        boolean isConditionAdded = false;
        try{
            query.append("SELECT pm.name, pm.display_name, pm.payment_type, pp.id, pp.net_amount, pp.tax_amount, pp.gross_amount, pp.currency, pp.duration FROM payment_method pm LEFT JOIN payment_plan pp ON pm.id = pp.payment_method_id ");
            if(null != id && id>0){
                query.append(" WHERE pp.id = ").append(id);
                isConditionAdded = true;
            }

            if(null != name && !name.isBlank() && !name.isEmpty()){
                if(isConditionAdded){
                    query.append(" AND pm.name LIKE '%").append(name).append("%'");
                }else{
                    query.append(" WHERE pm.name LIKE '%").append(name).append("%'");
                }
            }

            paymentMethods = jdbcTemplate.query(query.toString(), new PaymentMethodResultExtractor());

            return paymentMethods;
        }catch(Exception e){
            e.printStackTrace();
        }
        return paymentMethods;
    }
}

