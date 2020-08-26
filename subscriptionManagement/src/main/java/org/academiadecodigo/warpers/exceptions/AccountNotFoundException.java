package org.academiadecodigo.warpers.exceptions;

import org.academiadecodigo.warpers.errors.ErrorMessage;

/**
 * Thrown to indicate that the account was not found
 */
public class AccountNotFoundException extends JavaBankException {

    /**
     * @see JavaBankException#JavaBankException(String)
     */
    public AccountNotFoundException() {
        super(ErrorMessage.ACCOUNT_NOT_FOUND);
    }
}
