package org.academiadecodigo.warpers.controller.rest;

import org.academiadecodigo.warpers.command.AccountTransactionDto;
import org.academiadecodigo.warpers.command.TransferDto;
import org.academiadecodigo.warpers.converters.TransferDtoToTransfer;
import org.academiadecodigo.warpers.exceptions.AccountNotFoundException;
import org.academiadecodigo.warpers.exceptions.CustomerNotFoundException;
import org.academiadecodigo.warpers.exceptions.TransactionInvalidException;
import org.academiadecodigo.warpers.services.SubscriptionService;
import org.academiadecodigo.warpers.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST controller responsible for transaction related CRUD operations
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customer")
public class RestTransactionController {

    private TransferService transferService;
    private SubscriptionService subscriptionService;
    private TransferDtoToTransfer transferDtoToTransfer;

    /**
     * Sets the transfer service
     *
     * @param transferService the transfer service to set
     */
    @Autowired
    public void setTransferService(TransferService transferService) {
        this.transferService = transferService;
    }

    /**
     * Sets the account service
     *
     * @param subscriptionService the account service to set
     */
    @Autowired
    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    /**
     * Sets the converter for converting between the transfer DTO and transfer objects
     *
     * @param transferDtoToTransfer the transfer DTO to transfer converter
     */
    @Autowired
    public void setTransferDtoToTransfer(TransferDtoToTransfer transferDtoToTransfer) {
        this.transferDtoToTransfer = transferDtoToTransfer;
    }

    /**
     * Transfers money between accounts
     *
     * @param cid           the customer id
     * @param transferDto   the transfer DTO
     * @param bindingResult the binding result
     * @return the response entity
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/{cid}/transfer")
    public ResponseEntity<TransferDto> transfer(@PathVariable Integer cid, @Valid @RequestBody TransferDto transferDto, BindingResult bindingResult) {

        try {

            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            transferService.transfer(transferDtoToTransfer.convert(transferDto), cid);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (TransactionInvalidException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deposits money in an account
     *
     * @param cid                   the customer id
     * @param accountTransactionDto the account transaction DTO
     * @param bindingResult         the binding result object
     * @return the response entity
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/{cid}/deposit")
    public ResponseEntity<AccountTransactionDto> deposit(@PathVariable Integer cid, @Valid @RequestBody AccountTransactionDto accountTransactionDto, BindingResult bindingResult) {

        try {

            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            subscriptionService.deposit(accountTransactionDto.getId(), cid, Double.parseDouble(accountTransactionDto.getAmount()));

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (TransactionInvalidException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Withdraws money from an account
     *
     * @param cid                   the customer id
     * @param accountTransactionDto the account transaction DTO
     * @param bindingResult         the binding result object
     * @return the response entity
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/{cid}/withdraw")
    public ResponseEntity<AccountTransactionDto> withdraw(@PathVariable Integer cid, @Valid @RequestBody AccountTransactionDto accountTransactionDto, BindingResult bindingResult) {

        try {

            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            subscriptionService.withdraw(accountTransactionDto.getId(), cid, Double.parseDouble(accountTransactionDto.getAmount()));

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (TransactionInvalidException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
