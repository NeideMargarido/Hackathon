package org.academiadecodigo.warpers.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;
import org.academiadecodigo.warpers.persistence.model.subscription.SubscriptionType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * The {@link Subscription} data transfer object
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SubscriptionDto {

    //public static final String moneyRegex = "^\\$?0*[1-9]\\d*(\\.\\d{0,2})?|\\d*(\\.0[1-9])|\\d*(\\.[1-9]\\d?)?$?";

    private Integer id;

    @NotNull(message = "SubscriptionType is mandatory")
    private SubscriptionType type;

    //@Pattern(regexp = moneyRegex, message = "Amount is not valid")
    @NotNull(message = "Initial amount is mandatory")
    @NotBlank(message = "Initial amount is mandatory")
    private String balance;

    /**
     * Gets the id of the account DTO
     *
     * @return the account DTO id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the account DTO
     *
     * @param id the account DTO id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the account DTO balance
     *
     * @return the account DTO balance
     */
    public String getBalance() {
        return balance;
    }

    /**
     * Sets the account DTO balance
     *
     * @param balance the account DTO balance to set
     */
    public void setBalance(String balance) {
        this.balance = balance;
    }

    /**
     * Gets the type of the account DTO
     *
     * @return the account type
     */
    public SubscriptionType getType() {
        return type;
    }

    /**
     * Sets the account DTO type
     *
     * @param type the account type to set
     */
    public void setType(SubscriptionType type) {
        this.type = type;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "AccountDto{" +
                ", type=" + type +
                ", balance=" + balance +
                '}';
    }
}
