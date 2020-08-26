package org.academiadecodigo.warpers.services.mock;

import org.academiadecodigo.warpers.exceptions.AssociationExistsException;
import org.academiadecodigo.warpers.exceptions.CustomerNotFoundException;
import org.academiadecodigo.warpers.exceptions.RecipientNotFoundException;
import org.academiadecodigo.warpers.persistence.model.AbstractModel;
import org.academiadecodigo.warpers.persistence.model.Customer;
import org.academiadecodigo.warpers.persistence.model.Recipient;
import org.academiadecodigo.warpers.persistence.model.account.Account;
import org.academiadecodigo.warpers.services.SubscriptionService;
import org.academiadecodigo.warpers.services.UserService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A mock {@link UserService} implementation
 */
public class MockUserService extends AbstractMockService<Customer> implements UserService {

    private SubscriptionService subscriptionService;

    /**
     * Sets the customer service
     *
     * @param subscriptionService the customer service to set
     */
    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    /**
     * @see UserService#get(Integer)
     */
    @Override
    public Customer get(Integer id) {
        return modelMap.get(id);
    }

    /**
     * @see UserService#getBalance(Integer)
     */
    @Override
    public double getBalance(Integer id) throws CustomerNotFoundException {

        Customer customer = Optional.ofNullable(modelMap.get(id))
                .orElseThrow(CustomerNotFoundException::new);

        return customer.getAccounts().stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }

    /**
     * @see UserService#save(Customer)
     */
    @Override
    public Customer save(Customer customer) {

        if (customer.getId() == null) {
            customer.setId(getNextId());
        }

        modelMap.put(customer.getId(), customer);
        return customer;
    }

    /**
     * @see UserService#delete(Integer)
     */
    @Override
    public void delete(Integer id) throws AssociationExistsException {

        Customer customer = get(id);

        if (!customer.getAccounts().isEmpty()) {
            throw new AssociationExistsException();
        }

        modelMap.remove(id);
    }

    /**
     * @see UserService#list()
     */
    @Override
    public List<Customer> list() {
        return new ArrayList<>(modelMap.values());
    }

    /**
     * @see UserService#listRecipients(Integer)
     */
    @Override
    public List<Recipient> listRecipients(Integer id) throws CustomerNotFoundException {

        return Optional.ofNullable(modelMap.get(id))
                .orElseThrow(CustomerNotFoundException::new)
                .getRecipients();
    }

    /**
     * @see UserService#addRecipient(Integer, Recipient)
     */
    @Override
    public Recipient addRecipient(Integer id, Recipient recipient) {

        Customer customer = modelMap.get(id);

        if (subscriptionService.get(recipient.getAccountNumber()) == null ||
                getAccountIds(customer).contains(recipient.getAccountNumber())) {
            return null;
        }

        if (recipient.getId() == null) {
            recipient.setId(getNextId());
        }

        customer.addRecipient(recipient);

        return recipient;
    }

    /**
     * @see UserService#removeRecipient(Integer, Integer)
     */
    @Override
    public void removeRecipient(Integer id, Integer recipientId)
            throws CustomerNotFoundException, RecipientNotFoundException {

        Customer customer = Optional.ofNullable(modelMap.get(id))
                .orElseThrow(CustomerNotFoundException::new);

        Recipient recipient = null;

        for (Recipient rcpt : customer.getRecipients()) {
            if (rcpt.getId().equals(recipientId)) {
                recipient = rcpt;
            }
        }

        if (recipient == null) {
            throw new RecipientNotFoundException();
        }

        customer.removeRecipient(recipient);
    }

    /**
     * @see UserService#addAccount(Integer, Account)
     */
    @Override
    public Account addAccount(Integer id, Account account) {
        Customer customer = get(id);
        customer.addAccount(account);
        return account;
    }

    /**
     * @see UserService#closeAccount(Integer, Integer)
     */
    @Override
    public void closeAccount(Integer cid, Integer accountId) {
        Customer customer = modelMap.get(cid);

        customer.getAccounts().stream()
                .filter(account -> account.getId().equals(accountId))
                .findFirst()
                .ifPresent(customer::removeAccount);
    }

    private Set<Integer> getAccountIds(Customer customer) {
        List<Account> accounts = customer.getAccounts();

        return accounts.stream()
                .map(AbstractModel::getId)
                .collect(Collectors.toSet());
    }
}
