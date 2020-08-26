package org.academiadecodigo.warpers.services;

import org.academiadecodigo.warpers.domain.Transfer;
import org.academiadecodigo.warpers.exceptions.AccountNotFoundException;
import org.academiadecodigo.warpers.exceptions.CustomerNotFoundException;
import org.academiadecodigo.warpers.exceptions.TransactionInvalidException;
import org.academiadecodigo.warpers.persistence.dao.SubscriptionDao;
import org.academiadecodigo.warpers.persistence.dao.UserDao;
import org.academiadecodigo.warpers.persistence.model.Customer;
import org.academiadecodigo.warpers.persistence.model.Recipient;
import org.academiadecodigo.warpers.persistence.model.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A {@link TransferService} implementation
 */
@Service
public class TransferServiceImpl implements TransferService {

    private UserDao userDao;
    private SubscriptionDao subscriptionDao;

    /**
     * Sets the customer data access object
     *
     * @param userDao the customer dao to set
     */
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Sets the account data access object
     *
     * @param subscriptionDao the account dao to set
     */
    @Autowired
    public void setSubscriptionDao(SubscriptionDao subscriptionDao) {
        this.subscriptionDao = subscriptionDao;
    }

    /**
     * @see TransferService#transfer(Transfer)
     */
    @Transactional
    @Override
    public void transfer(Transfer transfer) throws AccountNotFoundException, TransactionInvalidException {

        Account srcAccount = subscriptionDao.findById(transfer.getSrcId());
        Account dstAccount = subscriptionDao.findById(transfer.getDstId());

        accountTransfer(srcAccount, dstAccount, transfer.getAmount());
    }

    /**
     * @see TransferService#transfer(Transfer, Integer)
     */
    @Transactional
    @Override
    public void transfer(Transfer transfer, Integer customerId)
            throws CustomerNotFoundException, AccountNotFoundException, TransactionInvalidException {

        Customer customer = Optional.ofNullable(userDao.findById(customerId))
                .orElseThrow(CustomerNotFoundException::new);

        Account srcAccount = Optional.ofNullable(subscriptionDao.findById(transfer.getSrcId()))
                .orElseThrow(AccountNotFoundException::new);
        Account dstAccount = Optional.ofNullable(subscriptionDao.findById(transfer.getDstId()))
                .orElseThrow(AccountNotFoundException::new);

        if (!customer.getAccounts().contains(srcAccount)) {
            throw new AccountNotFoundException();
        }

        // make sure destination account is a part of the recipient list
        verifyRecipientId(customer, dstAccount);

        accountTransfer(srcAccount, dstAccount, transfer.getAmount());
    }

    private void accountTransfer(Account srcAccount, Account dstAccount, Double amount)
            throws AccountNotFoundException, TransactionInvalidException {

        // make sure transaction can be performed
        verifyTransferAccountInformation(srcAccount, dstAccount, amount);

        srcAccount.debit(amount);
        dstAccount.credit(amount);

        subscriptionDao.saveOrUpdate(srcAccount);
        subscriptionDao.saveOrUpdate(dstAccount);
    }

    private void verifyTransferAccountInformation(Account srcAccount, Account dstAccount, double amount)
            throws AccountNotFoundException, TransactionInvalidException {

        Optional.ofNullable(srcAccount)
                .orElseThrow(AccountNotFoundException::new);

        Optional.ofNullable(dstAccount)
                .orElseThrow(AccountNotFoundException::new);

        if (!srcAccount.canDebit(amount) || !dstAccount.canCredit(amount)) {
            throw new TransactionInvalidException();
        }

    }

    private void verifyRecipientId(Customer customer, Account dstAccount) throws AccountNotFoundException {

        List<Integer> recipientAccountIds = listRecipientAccountIds(customer);

        if (!customer.getAccounts().contains(dstAccount) &&
                !recipientAccountIds.contains(dstAccount.getId())) {
            throw new AccountNotFoundException();
        }
    }

    private List<Integer> listRecipientAccountIds(Customer customer) {

        return customer.getRecipients().stream()
                .map(Recipient::getAccountNumber)
                .collect(Collectors.toList());
    }
}
