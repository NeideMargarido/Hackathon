package org.academiadecodigo.warpers.converters;

import org.academiadecodigo.warpers.command.SubscriptionDto;
import org.academiadecodigo.warpers.factories.AccountFactory;
import org.academiadecodigo.warpers.persistence.model.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A {@link Converter} implementation, responsible for {@link SubscriptionDto} to {@link Account} type conversion
 */
@Component
public class AccountDtoToAccount implements Converter<SubscriptionDto, Account> {

    private AccountFactory accountFactory;

    /**
     * Sets the account factory
     *
     * @param accountFactory the account factory to set
     */
    @Autowired
    public void setAccountFactory(AccountFactory accountFactory) {
        this.accountFactory = accountFactory;
    }

    /**
     * Converts the account DTO into a account model object
     *
     * @param subscriptionDto the account DTO
     * @return the account
     */
    @Override
    public Account convert(SubscriptionDto subscriptionDto) {

        Account account = null;

        account = accountFactory.createAccount(subscriptionDto.getType());
        account.credit(subscriptionDto.getBalance() != null ? Double.parseDouble(subscriptionDto.getBalance()) : 0);

        return account;
    }
}

