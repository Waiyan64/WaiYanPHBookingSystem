package com.example.RestService.service;

import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.RestService.common.exception.BadRequestException;
import com.example.RestService.common.exception.UnauthorizedException;
import com.example.RestService.constant.CustomerStatus;
import com.example.RestService.entity.Customer;
import com.example.RestService.entity.PointCollection;
import com.example.RestService.repository.CustomerRepository;
import com.example.RestService.utils.AppUtils;
import com.example.RestService.utils.Builder;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CustomerServiceImpl implements ICustomerService{
   private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

   private final CustomerRepository customerRepository;
   private final IActivationCodeService activationCodeService;

   private final MessageSource messageSource;

   private final PasswordEncoder passwordEncoder;

   @Transactional
   @Override
   public Customer create(String phoneNumber) {
       String password = this.passwordEncoder.encode(AppUtils.generatePinCode());
       Customer customer = Builder.of(Customer::new)
               .add(Customer::setPhoneNumber,  phoneNumber)
               .add(Customer::setPassword, password)
               .add(Customer::setConfirmPassword, password)
               .add(Customer::setConfirmPassword, password)
               .add(Customer::setStatus, CustomerStatus.NEW)
               .add(Customer::setEnable, true)
               .add(Customer::setIsWalletUser, false)
               .add(Customer::setPointCollection,  Builder.of(PointCollection::new)
                       .add(PointCollection::setTotalPoint, 0)
                       .build()
               )
               .build();
       this.customerRepository.save(customer);

       return customer;
   }

   @Override
   public Customer getNewCustomer(String phoneNumber) {
       return this.customerRepository.findByPhoneNumber(phoneNumber).orElseGet(() -> this.create(phoneNumber));
   }

   @Override
   public Customer getRegisteredCustomer(String phoneNumber) { 
    Customer customer = this.customerRepository.findByPhoneNumber(phoneNumber).orElseGet(() -> this.create(phoneNumber));

    if (customer.getStatus() != CustomerStatus.REGISTER) { 
        throw new BadRequestException(org.springframework.http.HttpStatus.NOT_FOUND.getReasonPhrase(), String.format("User Account is not registered with %s", phoneNumber));
    }
    return customer;
   }


   @Override
   public Customer checkValidRegisterConfirmCode(String phoneNumber, String code) {
       Customer customer = this.getNewCustomer(phoneNumber);
       if (this.activationCodeService.validRegisterCode(customer, code)) {
           customer.setStatus(CustomerStatus.REGISTER);
           return this.customerRepository.save(customer);
       }

       throw new UnauthorizedException(
           this.messageSource.getMessage("error.activation.title", null, LocaleContextHolder.getLocale()),
           this.messageSource.getMessage("error.activation.register.content", null, LocaleContextHolder.getLocale()));
   }

   @Override 
   public Customer checkValidRestPinConfirmCode(String phoneNumber, String code) { 
        Customer customer = this.getRegisteredCustomer(phoneNumber);
        if (this.activationCodeService.validResetPinCode(customer, code)) { 
            return customer;
        }

        throw new UnauthorizedException(
            this.messageSource.getMessage("error.activation.title", null, LocaleContextHolder.getLocale()),
            this.messageSource.getMessage("error.activation.resetPin.content", null, LocaleContextHolder.getLocale()));
   }
   

}
