package org.academiadecodigo.warpers.controller.web;

import org.academiadecodigo.warpers.command.SubscriptionDto;
import org.academiadecodigo.warpers.command.UserDto;
import org.academiadecodigo.warpers.converters.SubscriptionToSubscriptionDto;
import org.academiadecodigo.warpers.converters.UserDtoToUser;
import org.academiadecodigo.warpers.converters.UserToUserDto;
import org.academiadecodigo.warpers.exceptions.AssociationExistsException;
import org.academiadecodigo.warpers.exceptions.CustomerNotFoundException;
import org.academiadecodigo.warpers.persistence.model.User;
import org.academiadecodigo.warpers.persistence.model.subscription.SubscriptionType;
import org.academiadecodigo.warpers.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/")
public class UserController {

    private UserService userService;

    private UserToUserDto userToUserDto;
    private UserDtoToUser userDtoToUser;
    private SubscriptionToSubscriptionDto subscriptionToSubscriptionDto;


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Autowired
    public void setUserToUserDto(UserToUserDto userToUserDto) {
        this.userToUserDto = userToUserDto;
    }


    @Autowired
    public void setUserDtoToUser(UserDtoToUser userDtoToUser) {
        this.userDtoToUser = userDtoToUser;
    }


    @Autowired
    public void setSubscriptionToSubscriptionDto(SubscriptionToSubscriptionDto subscriptionToSubscriptionDto) {
        this.subscriptionToSubscriptionDto = subscriptionToSubscriptionDto;
    }


    @RequestMapping(method = RequestMethod.GET, path = {"/", ""})
    public String listCustomers(Model model) {
        model.addAttribute("customers", userToUserDto.convert(userService.list()));
        return "index";
    }


    @RequestMapping(method = RequestMethod.GET, path = "/add")
    public String addCustomer(Model model) {
        model.addAttribute("customer", new UserDto());
        return "customer/add-update";
    }


    @RequestMapping(method = RequestMethod.GET, path = "/{id}/edit")
    public String editCustomer(@PathVariable Integer id, Model model) {
        model.addAttribute("customer", userToUserDto.convert(userService.get(id)));
        return "customer/add-update";
    }


    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public String showCustomer(@PathVariable Integer id, Model model) throws Exception {

        User user = userService.get(id);

        model.addAttribute("user", userToUserDto.convert(user));
        model.addAttribute("accounts", subscriptionToSubscriptionDto.convert(user.getSubscriptions()));
        model.addAttribute("accountTypes", SubscriptionType.list());
        //model.addAttribute("customerBalance", userService.getBalance(id));

        SubscriptionDto subscriptionDto = new SubscriptionDto();

        model.addAttribute("account", subscriptionDto);

        return "user/show";
    }


    @RequestMapping(method = RequestMethod.POST, path = {"/", ""}, params = "action=save")
    public String saveCustomer(@Valid @ModelAttribute("customer") UserDto userDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "customer/add-update";
        }

        User savedUser = userService.save(userDtoToUser.convert(userDto));

        redirectAttributes.addFlashAttribute("lastAction", "Saved " + savedUser.getFirstName() + " " + savedUser.getLastName());
        return "redirect:/customer/" + savedUser.getId();
    }


    @RequestMapping(method = RequestMethod.POST, path = {"/", ""}, params = "action=cancel")
    public String cancelSaveCustomer() {
        return "redirect:/customer/";
    }


    @RequestMapping(method = RequestMethod.GET, path = "{id}/delete")
    public String deleteCustomer(@PathVariable Integer id, RedirectAttributes redirectAttributes) throws AssociationExistsException, CustomerNotFoundException {
        User user = userService.get(id);
        userService.delete(id);
        redirectAttributes.addFlashAttribute("lastAction", "Deleted " + user.getFirstName() + " " + user.getLastName());
        return "redirect:/user";
    }
}
