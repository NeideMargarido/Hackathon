package org.academiadecodigo.warpers.controller.rest;

import org.academiadecodigo.warpers.command.SubscriptionDto;
import org.academiadecodigo.warpers.converters.SubscriptionDtoToSubscription;
import org.academiadecodigo.warpers.converters.SubscriptionToSubscriptionDto;
import org.academiadecodigo.warpers.exceptions.AccountNotFoundException;
import org.academiadecodigo.warpers.exceptions.UserNotFoundException;
import org.academiadecodigo.warpers.exceptions.TransactionInvalidException;
import org.academiadecodigo.warpers.persistence.model.User;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;
import org.academiadecodigo.warpers.services.SubscriptionService;
import org.academiadecodigo.warpers.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller responsible for {@link Subscription} related CRUD operations
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class RestSubscriptionController {

    private UserService userService;
    private SubscriptionService subscriptionService;
    private SubscriptionToSubscriptionDto subscriptionToSubscriptionDto;
    private SubscriptionDtoToSubscription subscriptionDtoToSubscription;

    /**
     * Sets the customer service
     *
     * @param userService the customer service to set
     */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
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
     * Sets the converter for converting between account model object and account DTO
     *
     * @param subscriptionToSubscriptionDto the account model object to account DTO converter to set
     */
    @Autowired
    public void setSubscriptionToSubscriptionDto(SubscriptionToSubscriptionDto subscriptionToSubscriptionDto) {
        this.subscriptionToSubscriptionDto = subscriptionToSubscriptionDto;
    }

    /**
     * Sets the converter for converting between account DTO and account model objects
     *
     * @param accountDtoToAccount the subscription DTO to subscription converter to set
     * @param subscriptionDtoToSubscription the account DTO to account converter to set
     */
    @Autowired
    public void setSubscriptionDtoToSubscription(SubscriptionDtoToSubscription subscriptionDtoToSubscription) {
        this.subscriptionDtoToSubscription = subscriptionDtoToSubscription;
    }

    /**
     * Retrieves a representation of the given customer accounts
     *
     * @param cid the customer id
     * @return the response entity
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/subscription")
    public ResponseEntity<List<SubscriptionDto>> listCustomerAccounts(@PathVariable Integer cid) {

        User user = userService.get(cid);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<SubscriptionDto> subscriptionDtos = user.getSubscriptions().stream().map(account -> subscriptionToSubscriptionDto.convert(account)).collect(Collectors.toList());

        return new ResponseEntity<>(subscriptionDtos, HttpStatus.OK);
    }

    /**
     * Retrieves a representation of the customer account
     *
     * @param cid the customer id
     * @param aid the account id
     * @return the response entity
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/subscription/{aid}")
    public ResponseEntity<SubscriptionDto> showCustomerAccount(@PathVariable Integer cid, @PathVariable Integer aid) {

        Subscription subscription = subscriptionService.get(aid);

        if (subscription == null || subscription.getUser() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!subscription.getUser().getId().equals(cid)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(subscriptionToSubscriptionDto.convert(subscription), HttpStatus.OK);
    }

    /**
     * Adds an account
     *
     * @param cid                  the customer id
     * @param subscriptionDto           the account DTO
     * @param bindingResult        the binding result object
     * @param uriComponentsBuilder the uri components builder object
     * @return the response entity
     */
    @RequestMapping(method = RequestMethod.POST, path = "/{cid}/subscription")
    public ResponseEntity<?> addAccount(@PathVariable Integer cid, @Valid @RequestBody SubscriptionDto subscriptionDto, BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors() || subscriptionDto.getId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {

            Subscription subscription = userService.addAccount(cid, subscriptionDtoToSubscription.convert(subscriptionDto));

            UriComponents uriComponents = uriComponentsBuilder.path("/api/user/" + cid + "/subscription/" + subscription.getId()).build();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uriComponents.toUri());

            return new ResponseEntity<>(headers, HttpStatus.CREATED);

        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (TransactionInvalidException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Closes an account
     *
     * @param cid the customer id
     * @param aid the accound id
     * @return the response entity
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/subscription/{aid}/close")
    public ResponseEntity<?> closeAccount(@PathVariable Integer cid, @PathVariable Integer aid) {

        try {

            userService.closeAccount(cid, aid);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (TransactionInvalidException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
    }
}


