package com.sapius.accounts.service.impl;

import com.sapius.accounts.dto.AccountsDto;
import com.sapius.accounts.dto.CardsDto;
import com.sapius.accounts.dto.CustomerDetailsDto;
import com.sapius.accounts.dto.LoansDto;
import com.sapius.accounts.entity.Accounts;
import com.sapius.accounts.entity.Customer;
import com.sapius.accounts.exception.ResourceNotFoundException;
import com.sapius.accounts.mapper.AccountsMapper;
import com.sapius.accounts.mapper.CustomerMapper;
import com.sapius.accounts.repository.AccountsRepository;
import com.sapius.accounts.repository.CustomerRepository;
import com.sapius.accounts.service.ICustomersService;
import com.sapius.accounts.service.client.CardsFeignClient;
import com.sapius.accounts.service.client.LoansFeignClient;
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
        if (null!=loansDtoResponseEntity){
            customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
        }
        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationID,mobileNumber);
       if (null!= cardsDtoResponseEntity){
           customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
       }
        return customerDetailsDto;

    }
}
