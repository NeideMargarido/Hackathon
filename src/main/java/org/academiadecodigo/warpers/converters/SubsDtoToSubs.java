package org.academiadecodigo.warpers.converters;

import org.academiadecodigo.warpers.command.SubsDto;
import org.academiadecodigo.warpers.persistence.model.Subs;
import org.academiadecodigo.warpers.persistence.model.User;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;
import org.academiadecodigo.warpers.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SubsDtoToSubs implements Converter<SubsDto, Subs> {

    SubscriptionService subscriptionService;

    @Autowired
    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }


    @Override
    public Subs convert(SubsDto subsDto) {

        Subs subs = (subsDto.getId() != null ? subscriptionService.get(subsDto.getId()) : new Subs());

        subs.setSubsType(subsDto.getSubsType());
        subs.setMaxMembers(subsDto.getMaxNumber());

        return subs;
    }
}
