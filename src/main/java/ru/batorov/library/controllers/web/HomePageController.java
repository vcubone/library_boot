package ru.batorov.library.controllers.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {
    @GetMapping("/")
    public String home(Authentication authentif, Model model){
        System.out.println("\n\n\nhome");
        if(authentif != null && authentif.isAuthenticated())
        {
            System.out.println("authentif");
            System.out.println(authentif.getAuthorities());
            if (authentif.getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")))
                System.out.println("admin");
        }
        return "home";
    }
}
