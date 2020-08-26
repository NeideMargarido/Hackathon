package org.academiadecodigo.warpers.converters;

import org.academiadecodigo.warpers.command.SubscriptionDto;
import org.academiadecodigo.warpers.persistence.model.account.Account;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A {@link Converter} implementation, responsible for {@link Account} to {@link SubscriptionDto} type conversion
 */
@Component
public class AccountToAccountDto extends AbstractConverter<Account, SubscriptionDto> {

    /**
     * Converts the account model object into an account DTO
     * @param account the account
     * @return the account DTO
     */
    @Override
    public SubscriptionDto convert(Account account) {

        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setId(account.getId());
        subscriptionDto.setType(account.getAccountType());
        subscriptionDto.setBalance(String.valueOf(account.getBalance()));

        return subscriptionDto;
    }
}
