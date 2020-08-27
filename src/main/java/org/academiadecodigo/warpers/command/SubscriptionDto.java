package org.academiadecodigo.warpers.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.academiadecodigo.warpers.persistence.model.subscription.SubscriptionType;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SubscriptionDto {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "Subscription Type is mandatory")
    private SubscriptionType subscriptionType;


    @NotNull(message = "Max number of members is mandatory")
    @NotBlank(message = "Max number of members is mandatory")
    private String maxMembers;


    public String getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(String maxMembers) {
        this.maxMembers = maxMembers;
    }

    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }


    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }


    /*@Override
    public String toString() {
        return "AccountDto{" +
                ", type=" + subscriptionType +
                ", maxMembers=" + maxMembers +
                '}';
    }*/
}
