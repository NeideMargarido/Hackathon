package org.academiadecodigo.warpers.services.mock;

import org.academiadecodigo.warpers.exceptions.AccountNotFoundException;
import org.academiadecodigo.warpers.exceptions.CustomerNotFoundException;
import org.academiadecodigo.warpers.exceptions.TransactionInvalidException;
import org.academiadecodigo.warpers.persistence.model.Customer;
import org.academiadecodigo.warpers.persistence.model.account.Account;
import org.academiadecodigo.warpers.persistence.model.account.SavingsAccount;
import org.academiadecodigo.warpers.services.SubscriptionService;
import org.academiadecodigo.warpers.services.UserService;

import java.util.Optional;

/**
 * A mock {@link SubscriptionService} implementation
 */
public class MockSubscriptionService extends AbstractMockService<Account> implements SubscriptionService {

    private UserService userService;

    /**
     * Sets the customer service
     *
     * @param userService the customer service to set
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * @see SubscriptionService#get(Integer)
     */
    @Override
    public Account get(Integer id) {
        return modelMap.get(id);
    }

    /**
     * @see SubscriptionService#deposit(Integer, Integer, double)
     */
    @Override
    public void deposit(Integer id, Integer customerId, double amount)
            throws CustomerNotFoundException, AccountNotFoundException, TransactionInvalidException {

        Customer customer = Optional.ofNullable(userService.get(customerId))
                .orElseThrow(CustomerNotFoundException::new);

        Account account = Optional.ofNullable(modelMap.get(id))
                .orElseThrow(AccountNotFoundException::new);

        if (!account.getCustomer().getId().equals(customerId)) {
            throw new AccountNotFoundException();
        }

        if (!account.canCredit(amount)) {
            throw new TransactionInvalidException();
        }

        for (Account a : customer.getAccounts()) {
            if (a.getId().equals(id)) {
                a.credit(amount);
            }
        }

        account.credit(amount);
    }

    /**
     * @see SubscriptionService#withdraw(Integer, Integer, double)
     */
    @Override
    public void withdraw(Integer id, Integer customerId, double amount)
            throws CustomerNotFoundException, AccountNotFoundException, TransactionInvalidException {

        Customer customer = Optional.ofNullable(userService.get(customerId))
                .orElseThrow(CustomerNotFoundException::new);

        Account account = Optional.ofNullable(get(id))
                .orElseThrow(AccountNotFoundException::new);

        if (!account.getCustomer().getId().equals(customerId)) {
            throw new AccountNotFoundException();
        }

        // in UI the user cannot click on Withdraw so this is here just for safety
        if (account instanceof SavingsAccount) {
            throw new TransactionInvalidException();
        }

        // make sure transaction can be performed
        if (!account.canDebit(amount)) {
            throw new TransactionInvalidException();
        }

        for (Account a : customer.getAccounts()) {
            if (a.getId().equals(id)) {
                a.debit(amount);
            }
        }

        account.debit(amount);
    }
}
