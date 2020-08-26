package org.academiadecodigo.javabank.services;

import org.academiadecodigo.warpers.domain.Transfer;
import org.academiadecodigo.warpers.exceptions.AccountNotFoundException;
import org.academiadecodigo.warpers.exceptions.CustomerNotFoundException;
import org.academiadecodigo.warpers.exceptions.JavaBankException;
import org.academiadecodigo.warpers.exceptions.TransactionInvalidException;
import org.academiadecodigo.warpers.persistence.dao.SubscriptionDao;
import org.academiadecodigo.warpers.persistence.dao.UserDao;
import org.academiadecodigo.warpers.persistence.model.Customer;
import org.academiadecodigo.warpers.persistence.model.Recipient;
import org.academiadecodigo.warpers.persistence.model.account.Account;
import org.academiadecodigo.warpers.services.TransferServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class TransferServiceImplTest {

    private TransferServiceImpl transferService;
    private SubscriptionDao subscriptionDao;
    private UserDao userDao;

    @Before
    public void setup() {
        subscriptionDao = mock(SubscriptionDao.class);
        userDao = mock(UserDao.class);

        transferService = new TransferServiceImpl();
        transferService.setSubscriptionDao(subscriptionDao);
        transferService.setUserDao(userDao);
    }

    @Test
    public void testTransfer() throws JavaBankException {

        //setup
        int fakeSrcId = 9999;
        int fakeDstId = 8888;
        double fakeAmount = 1000.00;

        Account fakeSrcAccount = mock(Account.class);
        Account fakeDstAccount = mock(Account.class);

        Transfer fakeTransfer = new Transfer();
        fakeTransfer.setSrcId(fakeSrcId);
        fakeTransfer.setDstId(fakeDstId);
        fakeTransfer.setAmount(fakeAmount);

        when(subscriptionDao.findById(fakeSrcId)).thenReturn(fakeSrcAccount);
        when(subscriptionDao.findById(fakeDstId)).thenReturn(fakeDstAccount);
        when(fakeSrcAccount.canDebit(fakeAmount)).thenReturn(true);
        when(fakeDstAccount.canCredit(fakeAmount)).thenReturn(true);

        //exercise
        transferService.transfer(fakeTransfer);

        //verify
        verify(subscriptionDao, times(1)).findById(fakeSrcId);
        verify(subscriptionDao, times(1)).findById(fakeDstId);
        verify(fakeSrcAccount, times(1)).canDebit(fakeAmount);
        verify(fakeDstAccount, times(1)).canCredit(fakeAmount);
        verify(fakeSrcAccount, times(1)).debit(fakeAmount);
        verify(fakeDstAccount, times(1)).credit(fakeAmount);
        verify(subscriptionDao, times(1)).saveOrUpdate(fakeSrcAccount);
        verify(subscriptionDao, times(1)).saveOrUpdate(fakeDstAccount);
    }

    @Test(expected = AccountNotFoundException.class)
    public void testTransferInvalidSrcAccount() throws JavaBankException {

        //setup
        double fakeAmount = 1000.00;
        int fakeDstAccountId = 9999;
        int invalidSrcAccountId = 8888;

        Account fakeDstAccount = mock(Account.class);

        Transfer fakeTransfer = mock(Transfer.class);

        when(subscriptionDao.findById(invalidSrcAccountId)).thenReturn(null);
        when(subscriptionDao.findById(fakeDstAccountId)).thenReturn(fakeDstAccount);
        when(fakeTransfer.getSrcId()).thenReturn(invalidSrcAccountId);
        when(fakeTransfer.getDstId()).thenReturn(fakeDstAccountId);
        when(fakeTransfer.getAmount()).thenReturn(fakeAmount);

        //exercise
        transferService.transfer(fakeTransfer);
    }

    @Test(expected = AccountNotFoundException.class)
    public void testTransferInvalidDstAccount() throws JavaBankException {

        //setup
        double fakeAmount = 1000.00;
        int invalidDstAccountId = 8888;
        int fakeSrcAccountId = 9999;

        Account fakeDstAccount = mock(Account.class);

        Transfer fakeTransfer = mock(Transfer.class);

        when(subscriptionDao.findById(fakeSrcAccountId)).thenReturn(fakeDstAccount);
        when(subscriptionDao.findById(invalidDstAccountId)).thenReturn(null);
        when(fakeTransfer.getDstId()).thenReturn(fakeSrcAccountId);
        when(fakeTransfer.getSrcId()).thenReturn(invalidDstAccountId);
        when(fakeTransfer.getAmount()).thenReturn(fakeAmount);

        //exercise
        transferService.transfer(fakeTransfer);
    }

    @Test(expected = TransactionInvalidException.class)
    public void testTransferInvalidDebitAmount() throws JavaBankException {

        //setup
        int fakeSrcId = 9999;
        int fakeDstId = 8888;
        double invalidAmount = 1000.00;

        Account fakeSrcAccount = mock(Account.class);
        Account fakeDstAccount = mock(Account.class);

        Transfer fakeTransfer = new Transfer();
        fakeTransfer.setSrcId(fakeSrcId);
        fakeTransfer.setDstId(fakeDstId);
        fakeTransfer.setAmount(invalidAmount);

        when(subscriptionDao.findById(fakeSrcId)).thenReturn(fakeSrcAccount);
        when(subscriptionDao.findById(fakeDstId)).thenReturn(fakeDstAccount);
        when(fakeSrcAccount.canDebit(invalidAmount)).thenReturn(false);
        when(fakeDstAccount.canCredit(invalidAmount)).thenReturn(true);

        //exercise
        transferService.transfer(fakeTransfer);
    }

    @Test(expected = TransactionInvalidException.class)
    public void testTransferInvalidCreditAmount() throws JavaBankException {

        //setup
        int fakeSrcId = 9999;
        int fakeDstId = 8888;
        double invalidAmount = 1000.00;

        Account fakeSrcAccount = mock(Account.class);
        Account fakeDstAccount = mock(Account.class);

        Transfer fakeTransfer = new Transfer();
        fakeTransfer.setSrcId(fakeSrcId);
        fakeTransfer.setDstId(fakeDstId);
        fakeTransfer.setAmount(invalidAmount);

        when(subscriptionDao.findById(fakeSrcId)).thenReturn(fakeSrcAccount);
        when(subscriptionDao.findById(fakeDstId)).thenReturn(fakeDstAccount);
        when(fakeSrcAccount.canDebit(invalidAmount)).thenReturn(true);
        when(fakeDstAccount.canCredit(invalidAmount)).thenReturn(false);

        //exercise
        transferService.transfer(fakeTransfer);
    }

    @Test
    public void testTransferCustomer() throws JavaBankException {

        //setup
        int fakeSrcId = 9999;
        int fakeDstId = 8888;
        int fakeCustomerId = 7777;
        double fakeAmount = 1000.00;

        Account fakeSrcAccount = mock(Account.class);
        Account fakeDstAccount = mock(Account.class);
        Recipient fakeRecipient = mock(Recipient.class);
        Customer fakeCustomer = mock(Customer.class);
        List fakeRecipientAccountIds = mock(List.class);
        List fakeAccountsList = mock(List.class);

        List<Recipient> fakeRecipientList = new ArrayList<>();
        fakeRecipientList.add(fakeRecipient);

        Transfer fakeTransfer = new Transfer();
        fakeTransfer.setSrcId(fakeSrcId);
        fakeTransfer.setDstId(fakeDstId);
        fakeTransfer.setAmount(fakeAmount);

        when(subscriptionDao.findById(fakeSrcId)).thenReturn(fakeSrcAccount);
        when(subscriptionDao.findById(fakeDstId)).thenReturn(fakeDstAccount);
        when(userDao.findById(anyInt())).thenReturn(fakeCustomer);
        when(fakeRecipient.getAccountNumber()).thenReturn(fakeDstId);
        when(fakeSrcAccount.canDebit(fakeAmount)).thenReturn(true);
        when(fakeDstAccount.canCredit(fakeAmount)).thenReturn(true);
        when(fakeDstAccount.getId()).thenReturn(fakeDstId);
        when(fakeCustomer.getAccounts()).thenReturn(fakeAccountsList);
        when(fakeCustomer.getRecipients()).thenReturn(fakeRecipientList);
        when(fakeAccountsList.contains(fakeSrcAccount)).thenReturn(true);
        when(fakeAccountsList.contains(fakeDstAccount)).thenReturn(false);
        when(fakeRecipientAccountIds.contains(fakeDstId)).thenReturn(true);

        //exercise
        transferService.transfer(fakeTransfer, fakeCustomerId);

        //verify
        verify(userDao, times(1)).findById(fakeCustomerId);
        verify(subscriptionDao, times(1)).findById(fakeSrcId);
        verify(subscriptionDao, times(1)).findById(fakeDstId);
        verify(fakeAccountsList, times(1)).contains(fakeSrcAccount);
        verify(fakeAccountsList, times(1)).contains(fakeDstAccount);
        verify(fakeSrcAccount, times(1)).canDebit(fakeAmount);
        verify(fakeDstAccount, times(1)).canCredit(fakeAmount);
        verify(fakeSrcAccount, times(1)).debit(fakeAmount);
        verify(fakeDstAccount, times(1)).credit(fakeAmount);
        verify(subscriptionDao, times(1)).saveOrUpdate(fakeSrcAccount);
        verify(subscriptionDao, times(1)).saveOrUpdate(fakeDstAccount);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testTransferCustomerInvalidCustomer() throws JavaBankException {

        //setup
        int invalidCustomerId = 9999;
        int fakeSrcId = 8888;
        int fakeDstId = 7777;
        double fakeAmount = 1000.00;

        Transfer fakeTransfer = new Transfer();
        fakeTransfer.setSrcId(fakeSrcId);
        fakeTransfer.setDstId(fakeDstId);
        fakeTransfer.setAmount(fakeAmount);

        when(userDao.findById(anyInt())).thenReturn(null);

        //exercise
        transferService.transfer(fakeTransfer, invalidCustomerId);
    }

    @Test(expected = AccountNotFoundException.class)
    public void testTransferCustomerInvalidSrcAccount() throws JavaBankException {

        //setup
        int fakeCustomerId = 9999;
        int fakeSrcId = 8888;
        int fakeDstId = 7777;
        double fakeAmount = 1000.00;

        Transfer fakeTransfer = new Transfer();
        fakeTransfer.setSrcId(fakeSrcId);
        fakeTransfer.setDstId(fakeDstId);
        fakeTransfer.setAmount(fakeAmount);

        Account fakeSrcAccount = mock(Account.class);
        Account fakeDstAccount = mock(Account.class);
        Customer fakeCustomer = mock(Customer.class);
        List fakeAccountsList = mock(List.class);

        when(userDao.findById(fakeCustomerId)).thenReturn(fakeCustomer);
        when(subscriptionDao.findById(fakeSrcId)).thenReturn(fakeSrcAccount);
        when(subscriptionDao.findById(fakeDstId)).thenReturn(fakeDstAccount);
        when(fakeCustomer.getAccounts()).thenReturn(fakeAccountsList);
        when(fakeAccountsList.contains(fakeSrcAccount)).thenReturn(false);

        //exercise
        transferService.transfer(fakeTransfer, fakeCustomerId);
    }

    @Test(expected = AccountNotFoundException.class)
    public void testTransferCustomerInvalidDstAccount() throws JavaBankException {

        //setup
        int fakeCustomerId = 7777;
        int fakeSrcId = 9999;
        int fakeDstId = 8888;
        double fakeAmount = 1000.00;
        int invalidAccountNumber = 6666;

        Account fakeSrcAccount = mock(Account.class);
        Account fakeDstAccount = mock(Account.class);
        Recipient fakeRecipient = mock(Recipient.class);
        Customer fakeCustomer = mock(Customer.class);
        List fakeRecipientAccountIds = mock(List.class);
        List fakeAccountsList = mock(List.class);

        List<Recipient> fakeRecipientList = new ArrayList<>();
        fakeRecipientList.add(fakeRecipient);

        Transfer fakeTransfer = new Transfer();
        fakeTransfer.setSrcId(fakeSrcId);
        fakeTransfer.setDstId(fakeDstId);
        fakeTransfer.setAmount(fakeAmount);

        when(subscriptionDao.findById(fakeSrcId)).thenReturn(fakeSrcAccount);
        when(subscriptionDao.findById(fakeDstId)).thenReturn(fakeDstAccount);
        when(userDao.findById(anyInt())).thenReturn(fakeCustomer);
        when(fakeRecipient.getAccountNumber()).thenReturn(invalidAccountNumber);
        when(fakeSrcAccount.canDebit(fakeAmount)).thenReturn(true);
        when(fakeDstAccount.canCredit(fakeAmount)).thenReturn(true);
        when(fakeDstAccount.getId()).thenReturn(fakeDstId);
        when(fakeCustomer.getAccounts()).thenReturn(fakeAccountsList);
        when(fakeCustomer.getRecipients()).thenReturn(fakeRecipientList);
        when(fakeAccountsList.contains(fakeSrcAccount)).thenReturn(true);
        when(fakeAccountsList.contains(fakeDstAccount)).thenReturn(false);
        when(fakeRecipientAccountIds.contains(fakeDstId)).thenReturn(false);

        //exercise
        transferService.transfer(fakeTransfer, fakeCustomerId);
    }

}
