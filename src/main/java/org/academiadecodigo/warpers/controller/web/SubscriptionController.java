package org.academiadecodigo.warpers.controller.web;

import org.academiadecodigo.warpers.command.SubscriptionDto;
import org.academiadecodigo.warpers.converters.SubscriptionDtoToSubscription;
import org.academiadecodigo.warpers.converters.UserToUserDto;
import org.academiadecodigo.warpers.exceptions.TransactionInvalidException;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;
import org.academiadecodigo.warpers.services.SubscriptionService;
import org.academiadecodigo.warpers.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/user")
public class SubscriptionController {

    private UserService userService;
    private SubscriptionService subscriptionService;

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


    @Autowired
    public void setSubscriptionDtoToSubscription(SubscriptionDtoToSubscription subscriptionDtoToSubscription) {
        this.subscriptionDtoToSubscription = subscriptionDtoToSubscription;
    }


    @Autowired
    public void setUserToUserDto(UserToUserDto userToUserDto) {
        this.userToUserDto = userToUserDto;
    }


    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/subscription"})
    public String addSubscription(@PathVariable Integer cid, @Valid @ModelAttribute("account") SubscriptionDto subscriptionDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws Exception {

        if (bindingResult.hasErrors()) {
            return "redirect:/user/" + cid;
        }

        try {
            Subscription subscription = subscriptionDtoToSubscription.convert(subscriptionDto);
            userService.addSubscription(cid, subscription);
            redirectAttributes.addFlashAttribute("lastAction", "Started " + subscription.getSubscriptionType() + " subscription.");
            return "redirect:/user/" + cid;

        } catch (TransactionInvalidException ex) {
            redirectAttributes.addFlashAttribute("failure", "Something unexpected happened.");
            return "redirect:/user/" + cid;
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/subscription/{aid}/close")
    public String closeSubscription(@PathVariable Integer cid, @PathVariable Integer aid, RedirectAttributes redirectAttributes) throws Exception {

        try {
            userService.closeSubscription(cid, aid);
            redirectAttributes.addFlashAttribute("lastAction", "Canceled subscription " + aid);
            return "redirect:/user/" + cid;

        } catch (TransactionInvalidException ex) {
            redirectAttributes.addFlashAttribute("failure", "Unable to perform closing operation. Subscription # " + aid);
            return "redirect:/user/" + cid;
        }
    }

}
