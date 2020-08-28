package org.academiadecodigo.warpers.persistence.model;

import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "subs")
public class Subs extends AbstractModel{


    private String subsType;
    private String maxMembers;

    public String getSubsType() {
        return subsType;
    }

    public void setSubsType(String subsType) {
        this.subsType = subsType;
    }

    public String getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(String maxMembers) {
        this.maxMembers = maxMembers;
    }
}
