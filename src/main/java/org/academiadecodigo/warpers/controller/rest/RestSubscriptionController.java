package org.academiadecodigo.warpers.controller.rest;

import org.academiadecodigo.warpers.command.SubscriptionDto;
import org.academiadecodigo.warpers.command.UserDto;
import org.academiadecodigo.warpers.converters.SubscriptionDtoToSubscription;
import org.academiadecodigo.warpers.converters.SubscriptionToSubscriptionDto;
import org.academiadecodigo.warpers.converters.UserToUserDto;
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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class RestSubscriptionController {

    private UserService userService;
    private SubscriptionService subscriptionService;
    private SubscriptionToSubscriptionDto subscriptionToSubscriptiionDto;
    private SubscriptionDtoToSubscription subscriptionDtoToSubscription;
    private UserToUserDto userToUserDto;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

   /* @Autowired
    public void setSubscriptionToSubscriptionDto(SubscriptionToSubscriptionDto subscriptionToSubscriptionDto) {
        this.subscriptionToSubscriptionDto = subscriptionToSubscriptionDto;
    }*/

    @Autowired
    public void setSubscriptionDtoToSubscription(SubscriptionDtoToSubscription subscriptionDtoToSubscription) {
        this.subscriptionDtoToSubscription = subscriptionDtoToSubscription;
    }

    /*@RequestMapping(method = RequestMethod.GET, path = "/{cid}/subscription")
    public ResponseEntity<List<SubscriptionDto>> listCustomerAccounts(@PathVariable Integer cid) {

        User user = userService.get(cid);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<SubscriptionDto> subscriptionDtos = user.getSubscriptions().stream().map(account -> subscriptionToSubscriptionDto.convert(account)).collect(Collectors.toList());

        return new ResponseEntity<>(subscriptionDtos, HttpStatus.OK);
    }*/

    /*@RequestMapping(method = RequestMethod.GET, path = "/{cid}/subscription/{aid}")
    public ResponseEntity<SubscriptionDto> showCustomerAccount(@PathVariable Integer cid, @PathVariable Integer aid) {

        Subscription subscription = subscriptionService.get(aid);

        /*if (subscription == null || subscription.getUser() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!subscription.getUser().getId().equals(cid)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }*/

    //return new ResponseEntity<>(subscriptionToSubscriptionDto.convert(subscription), HttpStatus.OK);
    //}

    /*@RequestMapping(method = RequestMethod.POST, path = "/subscription")
    public ResponseEntity<?> addAccount(@Valid @RequestBody SubscriptionDto subscriptionDto, BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors() || subscriptionDto.getId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        Subscription savedSubscription = subscriptionService.save(subscriptionDtoToSubscription.convert(subscriptionDto));

        UriComponents uriComponents = uriComponentsBuilder.path("/api/user/" + savedSubscription.getId()).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);



    }*/

   /* @RequestMapping(method = RequestMethod.GET, path = "/{cid}/subscription/{aid}/close")
    public ResponseEntity<?> closeAccount(@PathVariable Integer cid, @PathVariable Integer aid) {

        try {

            userService.closeSubscription(cid, aid);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (TransactionInvalidException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
    }*/

    /*@RequestMapping(method = RequestMethod.GET, path = {"/subscription"})
    public ResponseEntity<List<SubscriptionDto>> listSubscription() {

        List<SubscriptionDto> subscriptionDto = subscriptionService.list().stream()
                .map(subscription -> subscriptionToSubscriptionDto.convert(subscription))
                .collect(Collectors.toList());

        return new ResponseEntity<>(subscriptionDto, HttpStatus.OK);
    }*/
}



