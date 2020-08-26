package org.academiadecodigo.javabank.services;

import org.academiadecodigo.warpers.exceptions.AccountNotFoundException;
import org.academiadecodigo.warpers.exceptions.CustomerNotFoundException;
import org.academiadecodigo.warpers.exceptions.JavaBankException;
import org.academiadecodigo.warpers.exceptions.TransactionInvalidException;
import org.academiadecodigo.warpers.persistence.dao.SubscriptionDao;
import org.academiadecodigo.warpers.persistence.dao.UserDao;
import org.academiadecodigo.warpers.persistence.model.Customer;
import org.academiadecodigo.warpers.persistence.model.account.Account;
import org.academiadecodigo.warpers.persistence.model.account.CheckingAccount;
import org.academiadecodigo.warpers.persistence.model.account.SavingsAccount;
import org.academiadecodigo.warpers.services.SubscriptionServiceImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SubscriptionServiceImplTest {

    private static final double DOUBLE_PRECISION = 0.1;
    private SubscriptionDao subscriptionDao;
    private UserDao userDao;
    private SubscriptionServiceImpl accountService;

    @Before
    public void setup() {

        subscriptionDao = mock(SubscriptionDao.class);
        userDao = mock(UserDao.class);

        accountService = new SubscriptionServiceImpl();
        accountService.setSubscriptionDao(subscriptionDao);
        accountService.setUserDao(userDao);
    }

    @Test
    public void testGet() {

        //setup
        int fakeId = 9999;
        Account fakeAccount = mock(Account.class);
        when(subscriptionDao.findById(fakeId)).thenReturn(fakeAccount);
        when(fakeAccount.getId()).thenReturn(fakeId);

        //exercise
        Account returnAcc = accountService.get(fakeId);

        //verify
        verify(subscriptionDao, times(1)).findById(fakeId);
        assertTrue(returnAcc.getId() == fakeId);
    }

    @Test
    public void testDeposit() throws JavaBankException {

        //setup
        int fakeCustomerId = 9999;
        int fakeAccountId = 8888;
        double fakeAmount = 1000.00;

        Account fakeAccount = spy(CheckingAccount.class);
        Customer fakeCustomer = new Customer();
        fakeCustomer.setId(fakeCustomerId);
        fakeCustomer.addAccount(fakeAccount);
        fakeAccount.setId(fakeAccountId);

        when(userDao.findById(fakeCustomerId)).thenReturn(fakeCustomer);
        when(subscriptionDao.findById(fakeAccountId)).thenReturn(fakeAccount);
        when(fakeAccount.getCustomer()).thenReturn(fakeCustomer);

        //exercise
        accountService.deposit(fakeAccountId, fakeCustomerId, fakeAmount);

        //verify
        assertTrue(fakeAccount.getBalance() == fakeAmount);
        verify(userDao, times(1)).findById(fakeCustomerId);
        verify(subscriptionDao, times(1)).findById(fakeAccountId);
        verify(userDao, times(1)).saveOrUpdate(fakeCustomer);
        verify(fakeAccount, times(1)).credit(fakeAmount);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testDepositInvalidCustomer() throws JavaBankException {

        //setup
        int fakeCustomerId = 9999;
        int fakeAccountId = 8888;
        double fakeAmount = 1000.00;
        Account fakeAccount = mock(Account.class);

        when(userDao.findById(anyInt())).thenReturn(null);
        when(subscriptionDao.findById(anyInt())).thenReturn(fakeAccount);

        //exercise
        accountService.deposit(fakeCustomerId, fakeAccountId, fakeAmount);
    }

    @Test(expected = AccountNotFoundException.class)
    public void testDepositInvalidAccount() throws JavaBankException {

        //setup
        int fakeCustomerId = 9999;
        int fakeAccountId = 8888;
        double fakeAmount = 1000.00;
        Customer fakeCustomer = mock(Customer.class);

        when(userDao.findById(anyInt())).thenReturn(fakeCustomer);
        when(subscriptionDao.findById(anyInt())).thenReturn(null);

        //exercise
        accountService.deposit(fakeCustomerId, fakeAccountId, fakeAmount);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testDepositInvalidAccountOwner() throws JavaBankException {

        // setup
        double fakeAmount = 100;
        int fakeCustomerThatIsDepositing = 9898;

        int fakeCustomerId = 9999;
        int fakeAccountId = 9999;
        Customer fakeCustomer = new Customer();
        Account fakeAccount = new SavingsAccount();
        fakeCustomer.setId(fakeCustomerId);

        fakeAccount.setCustomer(fakeCustomer);
        fakeCustomer.addAccount(fakeAccount);

        when(userDao.findById(fakeCustomerThatIsDepositing)).thenReturn(fakeCustomer);
        when(subscriptionDao.findById(fakeAccountId)).thenReturn(fakeAccount);

        // exercise
        accountService.deposit(fakeCustomerThatIsDepositing, fakeAccountId, fakeAmount);
    }

    @Test(expected = TransactionInvalidException.class)
    public void testDepositInvalidAmount() throws JavaBankException {

        // setup
        double fakeAmount = -10;

        int fakeCustomerId = 9999;
        int fakeAccountId = 9999;
        Customer fakeCustomer = new Customer();
        fakeCustomer.setId(fakeCustomerId);
        Account fakeAccount = new SavingsAccount();
        fakeAccount.setId(fakeAccountId);

        fakeAccount.setCustomer(fakeCustomer);
        fakeCustomer.addAccount(fakeAccount);

        when(userDao.findById(fakeCustomerId)).thenReturn(fakeCustomer);
        when(subscriptionDao.findById(fakeAccountId)).thenReturn(fakeAccount);

        // exercise
        accountService.deposit(fakeCustomerId, fakeAccountId, fakeAmount);
    }


    @Test
    public void testWithdraw() throws JavaBankException {

        // setup
        double fakeAmountToWithdraw = 100;
        double fakeAccountBalance = 9999;

        int fakeCustomerId = 9999;
        int fakeAccountId = 9999;
        Customer fakeCustomer = new Customer();
        fakeCustomer.setId(fakeCustomerId);
        Account fakeAccount = new CheckingAccount();

        fakeAccount.setId(fakeAccountId);
        fakeAccount.credit(fakeAccountBalance);
        fakeAccount.setCustomer(fakeCustomer);
        fakeCustomer.addAccount(fakeAccount);

        when(userDao.findById(fakeCustomerId)).thenReturn(fakeCustomer);
        when(subscriptionDao.findById(fakeAccountId)).thenReturn(fakeAccount);

        // exercise
        accountService.withdraw(fakeCustomerId, fakeAccountId, fakeAmountToWithdraw);

        //verify
        assertEquals(accountService.get(fakeAccountId).getBalance(), fakeAccountBalance - fakeAmountToWithdraw, DOUBLE_PRECISION);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testWithdrawInvalidCustomer() throws JavaBankException {

        // setup
        double fakeAmount = 100;
        int fakeCustomerId = 9999;
        int fakeAccountId = 9999;

        when(userDao.findById(fakeCustomerId)).thenReturn(null);

        // exercise
        accountService.withdraw(fakeCustomerId, fakeAccountId, fakeAmount);
    }

    @Test(expected = AccountNotFoundException.class)
    public void testWithdrawInvalidAccount() throws JavaBankException {

        // setup
        double fakeAmount = 100;
        int fakeCustomerId = 9999;
        int fakeAccountId = 9999;
        Customer fakeCustomer = new Customer();

        when(userDao.findById(fakeCustomerId)).thenReturn(fakeCustomer);
        when(subscriptionDao.findById(fakeAccountId)).thenReturn(null);

        // exercise
        accountService.withdraw(fakeCustomerId, fakeAccountId, fakeAmount);
    }

    @Test(expected = AccountNotFoundException.class)
    public void testWithdrawInvalidAccountOwner() throws JavaBankException {

        // setup
        double fakeAmount = 100;
        int fakeCustomerThatIsDepositing = 9898;

        int fakeCustomerId = 9999;
        int fakeAccountId = 9999;
        Customer fakeCustomer = new Customer();
        Account fakeAccount = new CheckingAccount();
        fakeCustomer.setId(fakeCustomerId);

        fakeAccount.setCustomer(fakeCustomer);
        fakeCustomer.addAccount(fakeAccount);

        when(userDao.findById(fakeCustomerThatIsDepositing)).thenReturn(fakeCustomer);
        when(subscriptionDao.findById(fakeAccountId)).thenReturn(fakeAccount);

        // exercise
        accountService.withdraw(fakeAccountId, fakeCustomerThatIsDepositing, fakeAmount);
    }

    @Test(expected = TransactionInvalidException.class)
    public void testWithdrawInvalidAmount() throws JavaBankException {

        // setup
        double fakeAmount = -10;

        int fakeCustomerId = 9999;
        int fakeAccountId = 9999;
        Customer fakeCustomer = new Customer();
        fakeCustomer.setId(fakeCustomerId);
        Account fakeAccount = new CheckingAccount();
        fakeAccount.setId(fakeAccountId);

        fakeAccount.setCustomer(fakeCustomer);
        fakeCustomer.addAccount(fakeAccount);

        when(userDao.findById(fakeCustomerId)).thenReturn(fakeCustomer);
        when(subscriptionDao.findById(fakeAccountId)).thenReturn(fakeAccount);

        // exercise
        accountService.withdraw(fakeCustomerId, fakeAccountId, fakeAmount);
    }

    @Test(expected = TransactionInvalidException.class)
    public void testWithdrawInvalidAccountType() throws JavaBankException {

        // setup
        double fakeAmount = 100;

        int fakeCustomerId = 9999;
        int fakeAccountId = 9999;
        Customer fakeCustomer = new Customer();
        Account fakeAccount = new SavingsAccount();

        when(userDao.findById(fakeCustomerId)).thenReturn(fakeCustomer);
        when(subscriptionDao.findById(fakeAccountId)).thenReturn(fakeAccount);

        // exercise
        accountService.withdraw(fakeCustomerId, fakeAccountId, fakeAmount);
    }

}
