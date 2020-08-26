package org.academiadecodigo.javabank.controller.web;

import org.academiadecodigo.warpers.command.SubscriptionDto;
import org.academiadecodigo.warpers.command.TransferDto;
import org.academiadecodigo.warpers.controller.web.SubscriptionController;
import org.academiadecodigo.warpers.converters.AccountDtoToAccount;
import org.academiadecodigo.warpers.converters.TransferDtoToTransfer;
import org.academiadecodigo.warpers.domain.Transfer;
import org.academiadecodigo.warpers.persistence.model.Customer;
import org.academiadecodigo.warpers.persistence.model.account.AccountType;
import org.academiadecodigo.warpers.persistence.model.account.CheckingAccount;
import org.academiadecodigo.warpers.services.SubscriptionService;
import org.academiadecodigo.warpers.services.UserService;
import org.academiadecodigo.warpers.services.TransferService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SubscriptionControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private SubscriptionController subscriptionController;

    private MockMvc mockMvc;

    @Mock
    private AccountDtoToAccount accountDtoToAccount;

    @Mock
    private TransferDtoToTransfer transferDtoToTransfer;

    @Mock
    private TransferService transferService;

    @Mock
    private SubscriptionService subscriptionService;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(subscriptionController).build();
    }

    @Test
    public void testCloseAccount() throws Exception {

        //setup
        int fakeCustomerId = 9999;
        int fakeAccountId = 8888;


        //exercise
        mockMvc.perform(get("/customer/" + fakeCustomerId + "/account/" + fakeAccountId + "/close"))

                //verify
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/" + fakeCustomerId))
                .andExpect(flash().attribute("lastAction", "Closed account " + fakeAccountId));

        verify(userService, times(1)).closeAccount(fakeCustomerId, fakeAccountId);
    }

    @Test
    public void testTransferToAccount() throws Exception {

        //setup
        Integer fakeCustomerId = 9999;
        Double fakeAmount = 1000.00;
        Integer fakeSrcId = 8888;
        Integer fakeDestId = 7777;

        Transfer fakeTransfer = new Transfer();
        fakeTransfer.setSrcId(fakeSrcId);
        fakeTransfer.setDstId(fakeDestId);
        fakeTransfer.setAmount(fakeAmount);

        when(transferDtoToTransfer.convert(any(TransferDto.class))).thenReturn(fakeTransfer);

        //exercise
        mockMvc.perform(post("/customer/" + fakeCustomerId + "/transfer")
                .param("srcId", fakeSrcId.toString())
                .param("dstId", fakeDestId.toString())
                .param("amount", fakeAmount.toString()))

                //verify
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/" + fakeCustomerId))
                .andExpect(flash().attribute("lastAction", "Account # " + fakeSrcId + " transfered " + fakeAmount + " to account #" + fakeDestId));

        verify(transferService, times(1)).transfer(fakeTransfer, fakeCustomerId);

        assertEquals(fakeTransfer.getAmount(), fakeAmount);
        assertEquals(fakeTransfer.getSrcId(), fakeSrcId);
        assertEquals(fakeTransfer.getDstId(), fakeDestId);
    }

    @Test
    public void testDeposit() throws Exception {

        //setup
        int fakeCustomerId = 9998;
        Integer fakeDestId = 8888;
        Double fakeAmount = 1000.00;


        //exercise
        mockMvc.perform(post("/customer/" + fakeCustomerId + "/deposit")
                .param("id", fakeDestId.toString())
                .param("amount", fakeAmount.toString()))

                //verify
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/" + fakeCustomerId))
                .andExpect(flash().attribute("lastAction", equalTo("Deposited " + fakeAmount + " into account # " + fakeDestId)));

        verify(subscriptionService, times(1)).deposit(fakeDestId, fakeCustomerId, fakeAmount);
    }

    @Test
    public void testWithdraw() throws Exception {

        //setup
        int fakeCustomerId = 9998;
        Integer fakeDestId = 8888;
        Double fakeAmount = 1000.00;


        //exercise
        mockMvc.perform(post("/customer/" + fakeCustomerId + "/withdraw")
                .param("id", fakeDestId.toString())
                .param("amount", fakeAmount.toString()))

                //verify
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/" + fakeCustomerId))
                .andExpect(flash().attribute("lastAction", equalTo("Withdrew " + fakeAmount + " from account # " + fakeDestId)));

        verify(subscriptionService, times(1)).withdraw(fakeDestId, fakeCustomerId, fakeAmount);
    }

    @Test
    public void testAddAccount() throws Exception {

        //setup
        Integer fakeCustomerId = 9999;
        Double fakeInitialAmount = 1000.0;
        AccountType fakeAccountType = AccountType.CHECKING;

        Customer fakeCustomer = new Customer();

        fakeCustomer.setId(fakeCustomerId);

        CheckingAccount fakeAccount = new CheckingAccount();

        fakeAccount.setCustomer(fakeCustomer);
        fakeAccount.credit(fakeInitialAmount);

        when(userService.get(fakeCustomerId)).thenReturn(fakeCustomer);
        when(accountDtoToAccount.convert(ArgumentMatchers.any(SubscriptionDto.class))).thenReturn(fakeAccount);

        //exercise
        mockMvc.perform(post("/customer/" + fakeCustomerId + "/account")
                .param("customerId", fakeCustomerId.toString())
                .param("balance", fakeInitialAmount.toString())
                .param("type", fakeAccountType.toString()))

                //verify
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/" + fakeCustomerId))
                .andExpect(flash().attribute("lastAction", equalTo("Created " + fakeAccount.getAccountType() + " account.")));

        ArgumentCaptor<SubscriptionDto> boundAccount = ArgumentCaptor.forClass(SubscriptionDto.class);
        verify(accountDtoToAccount, times(1)).convert(boundAccount.capture());
        verify(userService, times(1)).addAccount(fakeCustomerId, fakeAccount);

        assertEquals(fakeInitialAmount.toString(), boundAccount.getValue().getBalance());
        assertEquals(fakeAccountType, boundAccount.getValue().getType());
    }

}
