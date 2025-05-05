package com.example.RestService.service;

import java.time.LocalDateTime;

import com.example.RestService.constant.ActivationKey;
import com.example.RestService.entity.ActivationCode;
import com.example.RestService.entity.Customer;


public interface IActivationCodeService {

   boolean validRegisterCode(Customer customer, String token);

   boolean validCode(Customer customer, String token, ActivationKey activationKey);

   boolean validResetPinCode(Customer customer, String token);

   default boolean isCodeExpired(ActivationCode activationCode) {
       if (activationCode != null && activationCode.getExpirationDate() != null) {
           return LocalDateTime.now().isAfter(activationCode.getExpirationDate());
       }
       return true;
   }
}
