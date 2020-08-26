package org.academiadecodigo.warpers.exceptions;

import org.academiadecodigo.warpers.errors.ErrorMessage;

/**
 * Thrown to indicate that the recipient was not found
 */
public class RecipientNotFoundException extends JavaBankException {

    /**
     * @see JavaBankException#JavaBankException(String)
     */
    public RecipientNotFoundException() {
        super(ErrorMessage.RECIPIENT_NOT_FOUND);
    }
}
