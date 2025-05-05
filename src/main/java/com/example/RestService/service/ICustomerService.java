package com.example.RestService.service;

import com.example.RestService.entity.Customer;

public interface ICustomerService {
 
    
    Customer checkValidRegisterConfirmCode(String phoneNuumber, String code);

    Customer getNewCustomer(String phoneNumber);

    Customer create(String phoneNumber);

    Customer getRegisteredCustomer(String phoneNumber);

    Customer checkValidRestPinConfirmCode(String phoneNummber, String code);

}
