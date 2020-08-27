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
    public String signup() {
        return "redirect:signup";
    }

    @RequestMapping(path = "/assets/css/{item}.{extension}")
    public String serveResourcesCss(@PathVariable String item, @PathVariable String extension) {
        return "assets/css/" + item + "." + extension;
    }

    @RequestMapping(path = "/assets/js/{item}.{extension}")
    public String serveResourcesJs(@PathVariable String item, @PathVariable String extension) {
        return "assets/js/" + item + "." + extension;
    }
}
