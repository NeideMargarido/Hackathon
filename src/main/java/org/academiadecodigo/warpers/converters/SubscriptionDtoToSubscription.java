package org.academiadecodigo.warpers.converters;

import org.academiadecodigo.warpers.command.SubscriptionDto;
import org.academiadecodigo.warpers.factories.SubscriptionFactory;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class SubscriptionDtoToSubscription implements Converter<SubscriptionDto, Subscription> {

    private SubscriptionFactory subscriptionFactory;


    @Autowired
    public void setSubscriptionFactory(SubscriptionFactory subscriptionFactory) {
        this.subscriptionFactory = subscriptionFactory;
    }


    @Override
    public Subscription convert(SubscriptionDto subscriptionDto) {

        Subscription subscription = null;

        subscription = subscriptionFactory.createSubscription(subscriptionDto.getType());

        return subscription;
    }
}

