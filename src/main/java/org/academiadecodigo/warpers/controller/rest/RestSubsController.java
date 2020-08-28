package org.academiadecodigo.warpers.controller.rest;


import org.academiadecodigo.warpers.command.SubsDto;
import org.academiadecodigo.warpers.converters.SubscriptionDtoToSubscription;
import org.academiadecodigo.warpers.converters.SubscriptionToSubscriptionDto;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;
import org.academiadecodigo.warpers.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/subs")
public class RestSubsController {

    private SubscriptionService subscriptionService;
    private SubscriptionDtoToSubscription subscriptionDtoToSubscription;
    private SubscriptionToSubscriptionDto subscriptionToSubscriptionDto;

    @Autowired
    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }
    @Autowired
    public void setSubscriptionDtoToSubscription(SubscriptionDtoToSubscription subscriptionDtoToSubscription) {
        this.subscriptionDtoToSubscription = subscriptionDtoToSubscription;
    }
    @Autowired
    public void setSubscriptionToSubscriptionDto(SubscriptionToSubscriptionDto subscriptionToSubscriptionDto) {
        this.subscriptionToSubscriptionDto = subscriptionToSubscriptionDto;
    }

    @RequestMapping(method = RequestMethod.GET, path ="/")
    public ResponseEntity<List<SubsDto>> listSubs(){

        List<SubsDto> subsDtos = subscriptionService.list().stream()
                .map(subs -> subscriptionToSubscriptionDto.convert(subs))
                .collect(Collectors.toList());

        return new ResponseEntity<>(subsDtos, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> addSubs(@Valid @RequestBody SubsDto subsDto, BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder){

        if(bindingResult.hasErrors() || subsDto.getId() != null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Subscription savedSub = subscriptionService.save(subscriptionDtoToSubscription.convert(subsDto));

        UriComponents uriComponents = uriComponentsBuilder.path("/api/subs" + savedSub.getId()).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
