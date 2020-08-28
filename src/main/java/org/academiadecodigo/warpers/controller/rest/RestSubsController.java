package org.academiadecodigo.warpers.controller.rest;


import org.academiadecodigo.warpers.command.SubsDto;
import org.academiadecodigo.warpers.command.SubscriptionDto;
import org.academiadecodigo.warpers.command.UserDto;
import org.academiadecodigo.warpers.converters.*;
import org.academiadecodigo.warpers.persistence.model.Subs;
import org.academiadecodigo.warpers.persistence.model.User;
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
@RequestMapping("/api/subs")
public class RestSubsController {

    private SubscriptionService subscriptionService;
    private SubsDtoToSubs subsDtoToSubs;
    private SubsToSubsDto subsToSubsDto;

    @Autowired
    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Autowired
    public void setSubsDtoToSubs(SubsDtoToSubs subsDtoToSubs) {
        this.subsDtoToSubs = subsDtoToSubs;
    }

    @Autowired
    public void setSubsToSubsDto(SubsToSubsDto subsToSubsDto) {
        this.subsToSubsDto = subsToSubsDto;
    }

    @RequestMapping(method = RequestMethod.GET, path = {"/api/subs"})
    public ResponseEntity<List<SubsDto>> listSubscription() {

        List<SubsDto> subscriptionDto = subscriptionService.list().stream()
                .map(subscription -> subsToSubsDto.convert(subscription))
                .collect(Collectors.toList());

        return new ResponseEntity<>(subscriptionDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = {"/api/subs"})
    public ResponseEntity<?> addSub(@Valid @RequestBody SubsDto subsDto, BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors() || subsDto.getId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Subs savedSub = subscriptionService.save(subsDtoToSubs.convert(subsDto));

        UriComponents uriComponents = uriComponentsBuilder.path("/api/subs/" + savedSub.getId()).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }


}

