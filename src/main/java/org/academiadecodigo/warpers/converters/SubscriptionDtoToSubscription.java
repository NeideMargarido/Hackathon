package org.academiadecodigo.warpers.converters;

import org.academiadecodigo.warpers.command.SubsDto;
import org.academiadecodigo.warpers.command.SubscriptionDto;
import org.academiadecodigo.warpers.factories.SubscriptionFactory;
import org.academiadecodigo.warpers.persistence.model.subscription.SpotifySub;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;
import org.academiadecodigo.warpers.persistence.model.subscription.SubscriptionType;
import org.academiadecodigo.warpers.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.concurrent.Flow;


@Component
public class SubscriptionDtoToSubscription implements Converter<SubsDto, Subscription> {

    private SubscriptionFactory subscriptionFactory;
    private SubscriptionService subscriptionService;


    @Autowired
    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Autowired
    public void setSubscriptionFactory(SubscriptionFactory subscriptionFactory) {
        this.subscriptionFactory = subscriptionFactory;
    }


    @Override
    public Subscription convert(/*SubscriptionDto subscriptionDto*/SubsDto subsDto) {

        //Subscription subscription = null;

        Subscription subscription = (subsDto.getId() != null ? subscriptionService.get(subsDto.getId()) : new Subscription());

        subscription.setSubsType(subsDto.getSubscriptionType());
        subscription.setMaxMembers(subsDto.getMaxMembers());

        //subscription.setSubscriptionType(subscriptionDto.getSubscriptionType());
        //subscription.setMaxMembers(subscriptionDto.getMaxMembers());

        //subscription = subscriptionFactory.createSubscription(subsDto.getSubscriptionType());

        return subscription;
    }
}

