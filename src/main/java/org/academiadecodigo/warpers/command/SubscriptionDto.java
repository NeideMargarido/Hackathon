package org.academiadecodigo.warpers.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;
import org.academiadecodigo.warpers.persistence.model.subscription.SubscriptionType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SubscriptionDto {


    private Integer id;

    @NotNull(message = "Subscription Type is mandatory")
    private SubscriptionType type;


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


    public SubscriptionType getType() {
        return type;
    }


    public void setType(SubscriptionType type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "AccountDto{" +
                ", type=" + type +
                ", maxMembers=" + maxMembers +
                '}';
    }
}
