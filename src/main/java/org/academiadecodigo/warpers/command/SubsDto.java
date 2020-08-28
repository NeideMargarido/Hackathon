package org.academiadecodigo.warpers.command;

import javax.validation.constraints.NotNull;

public class SubsDto {

    private Integer id;
    @NotNull
    private String subscriptionType;
    @NotNull
    private String maxMembers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public String getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(String maxMembers) {
        this.maxMembers = maxMembers;
    }
}
