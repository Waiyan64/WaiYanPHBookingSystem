package com.example.RestService.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.RestService.constant.ActivationKey;
import com.example.RestService.entity.ActivationCode;
import com.example.RestService.entity.Customer;
import com.example.RestService.repository.ActivationCodeRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ActivationCodeServiceImpl implements IActivationCodeService {

   private final ActivationCodeRepository activationCodeRepository;

   @Override
   public boolean validRegisterCode(Customer customer, String token) {
       return this.validCode(customer, token, ActivationKey.REGISTER);
   }

   @Override
   public boolean validCode(Customer customer, String token, ActivationKey activationKey) {
       if (customer != null && token != null && activationKey != null) {
           Optional<ActivationCode> activationCodeOpt = this.activationCodeRepository.findByCustomerIdAndActiveAndCodeType( customer.getId(), true, activationKey);

           return activationCodeOpt.isPresent() && !this.isCodeExpired(activationCodeOpt.get()) && Objects.equals(token, activationCodeOpt.get().getCode());
       }
       return false;
   }

   @Override
   public boolean validResetPinCode(Customer customer, String token) { 
         return this.validCode(customer, token, ActivationKey.RESET_PIN);
   }

}
