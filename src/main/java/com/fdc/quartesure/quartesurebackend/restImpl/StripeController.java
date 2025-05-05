package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fdc.quartesure.quartesurebackend.POJO.CheckoutPayment;
import com.fdc.quartesure.quartesurebackend.service.UserService;
import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

import com.stripe.param.checkout.SessionCreateParams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class StripeController {
    // create a Gson object
    private static Gson gson = new Gson();
    @Autowired
    UserService userService;

    private long paymentchoice;

    @PostMapping("/payment")
    /**
     * Payment with Stripe checkout page
     *
     * @throws StripeException
     */
    public String paymentWithCheckoutPage(@RequestBody CheckoutPayment payment) throws StripeException {
        // We initilize stripe object with the api key
        this.paymentchoice=payment.getAmount();
        init();
        // We create a  stripe session parameters
        SessionCreateParams params = SessionCreateParams.builder()
                // We will use the credit card payment method
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT).setSuccessUrl(payment.getSuccessUrl())
                .setCancelUrl(
                        payment.getCancelUrl())
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setQuantity(payment.getQuantity())
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(payment.getCurrency()).setUnitAmount(payment.getAmount())
                                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData
                                                        .builder().setName(payment.getName()).build())
                                                .build())
                                .build())
                .build();
        // create a stripe session
        Session session = Session.create(params);
        Map<String, String> responseData = new HashMap<>();
        // We get the sessionId and we putted inside the response data you can get more info from the session object
        responseData.put("id", session.getId());
        // We can return only the sessionId as a String
        // we update credit in user entity

        return gson.toJson(responseData);
    }
    @PatchMapping("/successPayment/{id}")
    public String stripePaymentSuccess(@RequestBody CheckoutPayment payment) {
        System.out.println("797979797979999999999");
        System.out.println(this.paymentchoice);
        this.userService.creditAccount(this.paymentchoice/100);
        return gson.toJson("responseData");

    }

    private static void init() {
        Stripe.apiKey = "sk_test_51MaipqFzoKzk6dX5bqCUklrZxdyoqv9nxMYelFlS0QAN5u7GCphDmA7zJlzDmdwTtk1FacrT2LHJIZmA3i2zby0I00VHgJTqGt";
    }
}
