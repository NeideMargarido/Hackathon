package org.academiadecodigo.warpers.converters;

import org.academiadecodigo.warpers.command.SubscriptionDto;
import org.academiadecodigo.warpers.factories.SubscriptionFactory;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A {@link Converter} implementation, responsible for {@link SubscriptionDto} to {@link Subscription} type conversion
 */
@Component
public class AccountDtoToAccount implements Converter<SubscriptionDto, Subscription> {

    private SubscriptionFactory subscriptionFactory;

    /**
     * Sets the account factory
     *
     * @param subscriptionFactory the account factory to set
     */
    @Autowired
    public void setSubscriptionFactory(SubscriptionFactory subscriptionFactory) {
        this.subscriptionFactory = subscriptionFactory;
    }

    /**
     * Converts the account DTO into a account model object
     *
     * @param subscriptionDto the account DTO
     * @return the account
     */
    @Override
    public Subscription convert(SubscriptionDto subscriptionDto) {

        Subscription subscription = null;

        subscription = subscriptionFactory.createAccount(subscriptionDto.getType());
        subscription.credit(subscriptionDto.getBalance() != null ? Double.parseDouble(subscriptionDto.getBalance()) : 0);

        return subscription;
    }
}

