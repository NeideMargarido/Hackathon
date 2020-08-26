package org.academiadecodigo.javabank.converters;

import org.academiadecodigo.warpers.command.SubscriptionDto;
import org.academiadecodigo.warpers.converters.AccountDtoToAccount;
import org.academiadecodigo.warpers.factories.AccountFactory;
import org.academiadecodigo.warpers.persistence.model.account.Account;
import org.academiadecodigo.warpers.persistence.model.account.AccountType;
import org.academiadecodigo.warpers.persistence.model.account.CheckingAccount;
import org.academiadecodigo.warpers.services.UserService;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

public class AccountDtoToSubscriptionTest {

    private AccountDtoToAccount accountDtoToAccount;
    private UserService userService;
    private AccountFactory accountFactory;

    @Before
    public void setup() {
        accountDtoToAccount = new AccountDtoToAccount();
        userService = mock(UserService.class);
        accountFactory = mock(AccountFactory.class);

        accountDtoToAccount.setAccountFactory(accountFactory);
    }

    @Test
    public void testConvert() {

        //setup
        Double fakeInitialAmount = 1000.00;
        AccountType accountType = AccountType.CHECKING;
        Account fakeAccount = spy(CheckingAccount.class);
        SubscriptionDto fakeSubscriptionDto = mock(SubscriptionDto.class);

        when(accountFactory.createAccount(accountType)).thenReturn(fakeAccount);
        when(fakeSubscriptionDto.getBalance()).thenReturn(fakeInitialAmount.toString());
        when(fakeSubscriptionDto.getType()).thenReturn(accountType);

        //exercise
        Account account = accountDtoToAccount.convert(fakeSubscriptionDto);

        //verify
        verify(accountFactory, times(1)).createAccount(accountType);
        verify(fakeSubscriptionDto, times(1)).getType();
        verify(fakeSubscriptionDto, times(2)).getBalance();
        verify(fakeAccount, times(1)).credit(fakeInitialAmount);

        assertTrue(account.getAccountType() == fakeSubscriptionDto.getType());
        assertTrue(account.getBalance() == fakeInitialAmount);
    }
}
