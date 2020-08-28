package org.academiadecodigo.warpers.controller.web;


import org.academiadecodigo.warpers.command.SubsDto;
import org.academiadecodigo.warpers.command.UserDto;
import org.academiadecodigo.warpers.converters.SubsDtoToSubs;
import org.academiadecodigo.warpers.converters.SubsToSubsDto;
import org.academiadecodigo.warpers.persistence.model.Subs;
import org.academiadecodigo.warpers.persistence.model.User;
import org.academiadecodigo.warpers.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.Access;
import javax.validation.Valid;

@Controller
@RequestMapping("/subs")
public class SubsController {

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


    @RequestMapping(method = RequestMethod.GET, path = {"/subs"})
    public String listSubs(Model model) {
        model.addAttribute("customers", subsToSubsDto.convert(subscriptionService.list()));
        return "index";
    }

    @RequestMapping(method = RequestMethod.POST, path = {"/subs"}, params = "action=save")
    public String saveCustomer(@Valid @ModelAttribute("customer") SubsDto subsDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "subs/add-update";
        }

        Subs savedSub = subscriptionService.save(subsDtoToSubs.convert(subsDto));

        //redirectAttributes.addFlashAttribute("lastAction", "Saved " + savedUser.getFirstName() + " " + savedUser.getLastName());
        return "redirect:/subs/" + savedSub.getId();
    }


}
