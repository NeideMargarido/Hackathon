package org.academiadecodigo.warpers.converters;

import org.academiadecodigo.warpers.command.SubsDto;
import org.academiadecodigo.warpers.command.SubscriptionDto;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;
import org.springframework.stereotype.Component;


@Component
public class SubscriptionToSubscriptionDto extends AbstractConverter<Subscription, SubsDto> {


    @Override
    public /*SubscriptionDto*/SubsDto convert(Subscription subscription) {

        /*SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setId(subscription.getId());
        subscriptionDto.setSubscriptionType(subscription.getSubscriptionType());
        subscriptionDto.setMaxMembers(subscription.getMaxMembers());

        return subscriptionDto;*/

        SubsDto subsDto = new SubsDto();
        subsDto.setId(subscription.getId());
        subsDto.setSubscriptionType(subscription.getSubsType());
        //subsDto.setSubscriptionType(subscription.getSubscriptionType());
        subsDto.setMaxMembers(subscription.getMaxMembers());

        return subsDto;
    }
}
