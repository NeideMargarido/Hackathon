package org.academiadecodigo.warpers.converters;

import org.academiadecodigo.warpers.command.SubscriptionDto;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;
import org.springframework.stereotype.Component;


@Component
public class SubscriptionToSubscriptionDto extends AbstractConverter<Subscription, SubscriptionDto> {


    @Override
    public SubscriptionDto convert(Subscription subscription) {

        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setId(subscription.getId());
        subscriptionDto.setType(subscription.getSubscriptionType());
        subscriptionDto.setMaxMembers(subscription.getMaxMembers());

        return subscriptionDto;
    }
}
