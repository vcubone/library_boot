package ru.batorov.library.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@PreAuthorize("hasRole('ROLE_ADMIN')")//or hasRole()// and hasRole
public class AdminService {
    public void onlyAdminCanPass(){
        System.out.println("admin is here");
    }

}
