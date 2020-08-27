package org.academiadecodigo.warpers.exceptions;

import org.academiadecodigo.warpers.errors.ErrorMessage;


public class AccountNotFoundException extends JavaBankException {


    public AccountNotFoundException() {
        super(ErrorMessage.ACCOUNT_NOT_FOUND);
    }
}
