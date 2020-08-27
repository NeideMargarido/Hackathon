package org.academiadecodigo.warpers.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller responsible for rendering the initial page of the application
 */
@Controller
public class MainController {

    /**
     * Renders the home page view
     *
     * @return the view
     */
    @RequestMapping("/")
    public String home() {
        return "redirect:index";
    }

    @RequestMapping(path = "/signup")
    public String signUp() {
        return "signup";
    }

    @RequestMapping(path = "/userpage/")
    public String userPage() {
        return "userpage/index";
    }

    @RequestMapping(path = "/userpage/profile")
    public String userProfile() {
        return "userpage/profile";
    }

    @RequestMapping(path = "/userpage/settings")
    public String userSettings() {
        return "userpage/settings";
    }

    @RequestMapping(path = "/userpage/subscriptions")
    public String userSubscriptions() {
        return "userpage/subscriptions";
    }

    @RequestMapping(path = "/assets/css/{item}.{extension}")
    public String serveResourcesCss(@PathVariable String item, @PathVariable String extension) {
        return "assets/css/" + item + "." + extension;
    }

    @RequestMapping(path = "/assets/js/{item}.{extension}")
    public String serveResourcesJs(@PathVariable String item, @PathVariable String extension) {
        return "assets/js/" + item + "." + extension;
    }

    @RequestMapping(path = "userpage/assets/css/{item}.{extension}")
    public String serveResourcesCssUser(@PathVariable String item, @PathVariable String extension) {
        return "userpage/assets/css/" + item + "." + extension;
    }

    @RequestMapping(path = "userpage/assets/js/{item}.{extension}")
    public String serveResourcesJsUser(@PathVariable String item, @PathVariable String extension) {
        return "userpage/assetsjs/" + item + "." + extension;
    }




}
