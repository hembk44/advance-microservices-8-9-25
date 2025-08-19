package com.sapiustech.accounts.service.impl;

import com.sapiustech.accounts.dto.AccountsDto;
import com.sapiustech.accounts.dto.CardsDto;
import com.sapiustech.accounts.dto.CustomerDetailsDto;
import com.sapiustech.accounts.dto.LoansDto;
import com.sapiustech.accounts.entity.Accounts;
import com.sapiustech.accounts.entity.Customer;
import com.sapiustech.accounts.exception.ResourceNotFoundException;
import com.sapiustech.accounts.mapper.AccountsMapper;
import com.sapiustech.accounts.mapper.CustomerMapper;
import com.sapiustech.accounts.repository.AccountsRepository;
import com.sapiustech.accounts.repository.CustomerRepository;
import com.sapiustech.accounts.service.ICustomersService;
import com.sapiustech.accounts.service.client.CardsFeignClient;
import com.sapiustech.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {


    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationID) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(correlationID,mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationID,mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;

    }
}
