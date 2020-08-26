package org.academiadecodigo.javabank.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.academiadecodigo.warpers.command.SubscriptionDto;
import org.academiadecodigo.warpers.controller.rest.RestSubscriptionController;
import org.academiadecodigo.warpers.converters.AccountDtoToAccount;
import org.academiadecodigo.warpers.converters.AccountToAccountDto;
import org.academiadecodigo.warpers.exceptions.AccountNotFoundException;
import org.academiadecodigo.warpers.exceptions.CustomerNotFoundException;
import org.academiadecodigo.warpers.exceptions.TransactionInvalidException;
import org.academiadecodigo.warpers.persistence.model.Customer;
import org.academiadecodigo.warpers.persistence.model.account.Account;
import org.academiadecodigo.warpers.persistence.model.account.AccountType;
import org.academiadecodigo.warpers.persistence.model.account.CheckingAccount;
import org.academiadecodigo.warpers.persistence.model.account.SavingsAccount;
import org.academiadecodigo.warpers.services.SubscriptionService;
import org.academiadecodigo.warpers.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RestSubscriptionControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AccountDtoToAccount accountDtoToAccount;

    @Mock
    private AccountToAccountDto accountToAccountDto;

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private RestSubscriptionController restAccountController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(restAccountController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testListCustomerAccounts() throws Exception {

        int fakeCustomerId = 999;
        Customer customer = new Customer();
        customer.setId(fakeCustomerId);

        int fakeAccountId = 888;
        int balance = 8888;
        AccountType accountType = AccountType.CHECKING;

        Account checkingAccount = new CheckingAccount();
        checkingAccount.setCustomer(customer);
        checkingAccount.setBalance(balance);
        checkingAccount.setId(fakeAccountId);
        customer.addAccount(checkingAccount);

        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setId(fakeAccountId);
        subscriptionDto.setBalance(String.valueOf(balance));
        subscriptionDto.setType(accountType);


        when(userService.get(fakeCustomerId)).thenReturn(customer);
        when(accountToAccountDto.convert(ArgumentMatchers.any(Account.class))).thenReturn(subscriptionDto);

        mockMvc.perform(get("/api/customer/{cid}/account", fakeCustomerId))
                .andExpect(jsonPath("$[0].id").value(fakeAccountId))
                .andExpect(jsonPath("$[0].type").value(accountType.toString()))
                .andExpect(jsonPath("$[0].balance").value(balance))
                .andExpect(status().isOk());

        verify(userService, times(1)).get(fakeCustomerId);
        verify(accountToAccountDto, times(1)).convert(checkingAccount);

    }

    @Test
    public void testListAccountsInvalidCustomer() throws Exception {

        int invalidId = 888;

        when(userService.get(invalidId)).thenReturn(null);

        mockMvc.perform(get("/api/customer/{cid}/account", invalidId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).get(invalidId);

    }

    @Test
    public void testShowCustomerAccount() throws Exception {

        int fakeCustomerId = 999;
        Customer customer = new Customer();
        customer.setId(fakeCustomerId);

        int fakeAccountId = 888;
        int balance = 8888;
        AccountType accountType = AccountType.CHECKING;

        Account checkingAccount = new CheckingAccount();
        checkingAccount.setCustomer(customer);
        checkingAccount.setBalance(balance);
        checkingAccount.setId(fakeAccountId);
        customer.addAccount(checkingAccount);

        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setId(fakeAccountId);
        subscriptionDto.setBalance(String.valueOf(balance));
        subscriptionDto.setType(accountType);


        when(subscriptionService.get(fakeAccountId)).thenReturn(checkingAccount);
        when(accountToAccountDto.convert(checkingAccount)).thenReturn(subscriptionDto);

        mockMvc.perform(get("/api/customer/{cid}/account/{aid}", fakeCustomerId, fakeAccountId))
                .andExpect(jsonPath("$.id").value(fakeAccountId))
                .andExpect(jsonPath("$.type").value(accountType.toString()))
                .andExpect(jsonPath("$.balance").value(balance))
                .andExpect(status().isOk());

        verify(subscriptionService, times(1)).get(fakeAccountId);
        verify(accountToAccountDto, times(1)).convert(checkingAccount);
    }

    @Test
    public void testShowInvalidAccount() throws Exception {

        int fakeCustomerId = 999;
        int invalidAccountId = 777;

        when(subscriptionService.get(invalidAccountId)).thenReturn(null);

        mockMvc.perform(get("/api/customer/{cid}/account/{aid}", fakeCustomerId, invalidAccountId))
                .andExpect(status().isNotFound());

        verify(subscriptionService, times(1)).get(invalidAccountId);
    }

    @Test
    public void testShowAccountWithInvalidCustomer() throws Exception {

        int fakeAccountId = 888;
        int invalidCustomerId = 777;

        Account checkingAccount = new CheckingAccount();
        checkingAccount.setId(fakeAccountId);


        when(subscriptionService.get(fakeAccountId)).thenReturn(checkingAccount);

        mockMvc.perform(get("/api/customer/{cid}/account/{aid}", invalidCustomerId, fakeAccountId))
                .andExpect(status().isNotFound());

        verify(subscriptionService, times(1)).get(fakeAccountId);
    }

    @Test
    public void testShowAccountWithMismatchingCustomerId() throws Exception {

        int fakeCustomerId = 999;
        int fakeAccountId = 888;
        int invalidCustomerId = 777;

        Customer customer = new Customer();
        customer.setId(fakeCustomerId);

        Account checkingAccount = new CheckingAccount();
        checkingAccount.setId(fakeAccountId);
        checkingAccount.setCustomer(customer);


        when(subscriptionService.get(fakeAccountId)).thenReturn(checkingAccount);

        mockMvc.perform(get("/api/customer/{cid}/account/{aid}", invalidCustomerId, fakeAccountId))
                .andExpect(status().isNotFound());

        verify(subscriptionService, times(1)).get(fakeAccountId);
    }

    @Test
    public void testAddAccount() throws Exception {

        int fakeCustomerId = 999;
        Customer customer = new Customer();
        customer.setId(fakeCustomerId);

        int fakeAccountId = 888;
        int balance = 8888;
        AccountType accountType = AccountType.CHECKING;

        Account account = new CheckingAccount();
        account.setCustomer(customer);
        account.setBalance(balance);
        account.setId(fakeAccountId);
        customer.addAccount(account);

        String initialAmount = "1111";
        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setId(null);
        subscriptionDto.setBalance(initialAmount);
        subscriptionDto.setType(accountType);


        when(accountDtoToAccount.convert(ArgumentMatchers.any(SubscriptionDto.class))).thenReturn(account);
        when(userService.addAccount(fakeCustomerId, account)).thenReturn(account);

        mockMvc.perform(post("/api/customer/{cid}/account", fakeCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(subscriptionDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("http://localhost/api/customer/" + fakeCustomerId + "/account/" + fakeAccountId)));

        //verify properties of bound command object
        ArgumentCaptor<SubscriptionDto> boundAccount = ArgumentCaptor.forClass(SubscriptionDto.class);

        verify(accountDtoToAccount, times(1)).convert(boundAccount.capture());
        verify(userService, times(1)).addAccount(fakeCustomerId, account);

        assertEquals(null, boundAccount.getValue().getId());
        assertEquals(initialAmount, boundAccount.getValue().getBalance());
        assertEquals(accountType, boundAccount.getValue().getType());

    }

    @Test
    public void testAddAccountWithBadRequest() throws Exception {

        int fakeCustomerId = 999;

        mockMvc.perform(post("/api/customer/{cid}/account", fakeCustomerId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddAccountWithInvalidCustomer() throws Exception {

        int invalidCustomerId = 888;
        AccountType accountType = AccountType.CHECKING;
        String initialAmount = "1111";

        Account account = new CheckingAccount();

        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setId(null);
        subscriptionDto.setBalance(initialAmount);
        subscriptionDto.setType(accountType);


        when(accountDtoToAccount.convert(ArgumentMatchers.any(SubscriptionDto.class))).thenReturn(account);
        doThrow(new CustomerNotFoundException()).when(userService).addAccount(ArgumentMatchers.any(Integer.class), ArgumentMatchers.any(Account.class));

        mockMvc.perform(post("/api/customer/{cid}/account", invalidCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(subscriptionDto)))
                .andExpect(status().isNotFound());

        verify(accountDtoToAccount, times(1)).convert(ArgumentMatchers.any(SubscriptionDto.class));
    }

    @Test
    public void testAddSavingAccountWithoutMinimumBalance() throws Exception {

        int fakeCustomerId = 999;
        Customer customer = new Customer();
        customer.setId(fakeCustomerId);

        AccountType accountType = AccountType.SAVINGS;
        Account account = new SavingsAccount();

        String invalidInitialAmount = "1";
        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setId(null);
        subscriptionDto.setBalance(invalidInitialAmount);
        subscriptionDto.setType(accountType);


        when(accountDtoToAccount.convert(ArgumentMatchers.any(SubscriptionDto.class))).thenReturn(account);
        when(userService.addAccount(fakeCustomerId, account)).thenReturn(account);
        doThrow(new TransactionInvalidException()).when(userService).addAccount(ArgumentMatchers.any(Integer.class), ArgumentMatchers.any(Account.class));

        mockMvc.perform(post("/api/customer/{cid}/account", fakeCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(subscriptionDto)))
                .andExpect(status().isBadRequest());

        verify(accountDtoToAccount, times(1)).convert(ArgumentMatchers.any(SubscriptionDto.class));
        verify(userService, times(1)).addAccount(ArgumentMatchers.any(Integer.class), ArgumentMatchers.any(Account.class));
    }

    @Test
    public void testCloseAccount() throws Exception {

        int fakeCustomerId = 999;
        int fakeAccountId = 888;

        mockMvc.perform(get("/api/customer/{cid}/account/{aid}/close", fakeCustomerId, fakeAccountId))
                .andExpect(status().isOk());

        verify(userService, times(1)).closeAccount(fakeCustomerId, fakeAccountId);
    }

    @Test
    public void testCloseAccountWithInvalidCustomer() throws Exception {

        int fakeAccountId = 888;
        int invalidCustomerId = 777;

        doThrow(new CustomerNotFoundException()).when(userService).closeAccount(ArgumentMatchers.any(Integer.class), ArgumentMatchers.any(Integer.class));

        mockMvc.perform(get("/api/customer/{cid}/account/{aid}/close", invalidCustomerId, fakeAccountId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).closeAccount(invalidCustomerId, fakeAccountId);
    }

    @Test
    public void testCloseInvalidAccount() throws Exception {

        int fakeCustomerId = 999;
        int invalidAccountId = 777;

        doThrow(new AccountNotFoundException()).when(userService).closeAccount(ArgumentMatchers.any(Integer.class), ArgumentMatchers.any(Integer.class));

        mockMvc.perform(get("/api/customer/{cid}/account/{aid}/close", fakeCustomerId, invalidAccountId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).closeAccount(fakeCustomerId, invalidAccountId);
    }

    @Test
    public void testCloseAccountWithNonZeroBalance() throws Exception {

        int fakeCustomerId = 999;
        int fakeAccountId = 888;

        doThrow(new TransactionInvalidException()).when(userService).closeAccount(ArgumentMatchers.any(Integer.class), ArgumentMatchers.any(Integer.class));

        mockMvc.perform(get("/api/customer/{cid}/account/{aid}/close", fakeCustomerId, fakeAccountId))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).closeAccount(fakeCustomerId, fakeAccountId);
    }

}
