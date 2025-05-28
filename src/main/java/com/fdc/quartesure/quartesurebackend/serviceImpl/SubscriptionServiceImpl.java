package com.fdc.quartesure.quartesurebackend.serviceImpl;

import com.fdc.quartesure.quartesurebackend.POJO.Payment;
import com.fdc.quartesure.quartesurebackend.POJO.Subscription;
import com.fdc.quartesure.quartesurebackend.POJO.User;
import com.fdc.quartesure.quartesurebackend.constants.QuarteSureConstants;
import com.fdc.quartesure.quartesurebackend.dao.PaymentDao;
import com.fdc.quartesure.quartesurebackend.dao.SubscriptionDao;
import com.fdc.quartesure.quartesurebackend.dao.UserDao;
import com.fdc.quartesure.quartesurebackend.service.SubscriptionService;
import com.fdc.quartesure.quartesurebackend.utils.QuarteSureUtils;
import com.fdc.quartesure.quartesurebackend.wrapper.SubscriptionResponseWrapper;
import com.fdc.quartesure.quartesurebackend.wrapper.SubscriptionWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final UserDao userDao;
    private final SubscriptionDao subscriptionDao;
    private final PaymentDao paymentDao;
    @Override
    public ResponseEntity<String> subscribe(SubscriptionWrapper subscriptionWrapper) {
        User user = userDao.findByEmail(subscriptionWrapper.userEmail);
        if (user == null) {
            return QuarteSureUtils.getResponseEntity(QuarteSureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
//                    User newUser = new User();
//                    newUser.setEmail(subscriptionWrapper.userEmail);
//                    newUser.setLastName(subscriptionWrapper.fullName);
//                    newUser.setRole("user");
//                    return userDao.save(newUser);
                };

        Subscription subscription = subscriptionDao.findByUser_id(user.getUser_id());
        if(subscription == null) {
            return QuarteSureUtils.getResponseEntity(QuarteSureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        subscription.setUser(user);
       // subscription.setPlan(subscriptionWrapper.plan);
        subscription.setAmountPaid(subscription.getAmountPaid()+subscriptionWrapper.amountPaid);
        subscription.setCurrency(subscriptionWrapper.currency);
        subscription.setPaymentMethod(subscriptionWrapper.paymentMethod);
       // subscription.setTransactionId(subscriptionWrapper.transactionId);
       // subscription.setStartDate(subscriptionWrapper.startDate);
       // subscription.setEndDate(subscriptionWrapper.endDate);
        subscription.setStatus("active");

        subscription = subscriptionDao.save(subscription);

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setSubscription(subscription);
        payment.setAmount(subscriptionWrapper.amountPaid);
        payment.setCurrency(subscriptionWrapper.currency);
       // payment.setTransactionId(subscriptionWrapper.transactionId);
        payment.setStatus("succeeded");
        payment.setPaidAt(LocalDateTime.now());

        payment = paymentDao.save(payment);

//        SubscriptionResponseWrapper response = new SubscriptionResponseWrapper();
//        response.userId = user.getUser_id();
//        response.subscriptionId = subscription.getId();
//        response.paymentId = payment.getId();
//        response.message = "Abonnement enregistré avec succès.";
        return QuarteSureUtils.getResponseEntity("Subscription updated successfully.",HttpStatus.OK);

    }

}
