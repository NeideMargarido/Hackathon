package org.academiadecodigo.warpers.exceptions;

import org.academiadecodigo.warpers.errors.ErrorMessage;

/**
 * Thrown to indicate that the customer was not found
 */
public class UserNotFoundException extends JavaBankException {

    /**
     * @see JavaBankException#JavaBankException(String)
     */
    public UserNotFoundException() {
        super(ErrorMessage.CUSTOMER_NOT_FOUND);
    }
}
