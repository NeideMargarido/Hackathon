package org.academiadecodigo.warpers.converters;

import org.academiadecodigo.warpers.command.SubscriptionDto;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class SubscriptionToSubscriptionDto extends AbstractConverter<Subscription, SubscriptionDto> {


    @Override
    public SubscriptionDto convert(Subscription subscription) {

        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setId(subscription.getId());
        subscriptionDto.setType(subscription.getAccountType());
        subscriptionDto.setBalance(String.valueOf(subscription.getBalance()));

        return subscriptionDto;
    }
}
