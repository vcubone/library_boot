package ru.batorov.library.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.batorov.library.services.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private final AdminService adminService;
	
	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}

	@GetMapping("")
    public String adminPage(){
		adminService.onlyAdminCanPass();
		
        return "admin/adminHome";
    }
}
