package org.academiadecodigo.warpers.converters;

import org.academiadecodigo.warpers.command.SubscriptionDto;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A {@link Converter} implementation, responsible for {@link Subscription} to {@link SubscriptionDto} type conversion
 */
@Component
public class AccountToAccountDto extends AbstractConverter<Subscription, SubscriptionDto> {

    /**
     * Converts the subscription model object into an subscription DTO
     * @param subscription the subscription
     * @return the subscription DTO
     */
    @Override
    public SubscriptionDto convert(Subscription subscription) {

        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setId(subscription.getId());
        subscriptionDto.setType(subscription.getAccountType());
        subscriptionDto.setBalance(String.valueOf(subscription.getBalance()));

        return subscriptionDto;
    }
}
