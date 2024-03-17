package ru.batorov.library.controllers.web;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import ru.batorov.library.dto.RoleDTO;
import ru.batorov.library.dto.credentials.PasswordDTO;
import ru.batorov.library.dto.person.PersonAdminDTO;
import ru.batorov.library.dto.person.PersonRegistrationDTO;
import ru.batorov.library.models.Person;
import ru.batorov.library.models.Role;
import ru.batorov.library.services.PeopleService;
import ru.batorov.library.services.RolesService;
import ru.batorov.library.services.SessionService;
import ru.batorov.library.util.UsernameValidator;

import static ru.batorov.library.util.DTOConvert.*;

import java.util.List;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final ModelMapper modelMapper;
    private final UsernameValidator usernameValidator;
    private final RolesService rolesService;
    private final SessionService sessionService;

    public PeopleController(PeopleService peopleService, ModelMapper modelMapper,
            UsernameValidator usernameValidator, RolesService rolesService,
            SessionService sessionService) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
        this.usernameValidator = usernameValidator;
        this.rolesService = rolesService;
        this.sessionService = sessionService;
    }

    @GetMapping()
    public String all(Model model) {
        model.addAttribute("personUserDTOs",
                convertToPersonAdminDTOCollection(peopleService.allWithRoles(), modelMapper));
        return "people/all";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("registrationDTO") PersonRegistrationDTO registrationDTO) {
        return "people/new";
    }

    @PostMapping("/new")
    public String performRegistration(@ModelAttribute("registrationDTO") @Valid PersonRegistrationDTO registrationDTO,
            BindingResult bindingResult) {
        Person person = modelMapper.map(registrationDTO, Person.class);

        usernameValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "/people/new";

        peopleService.register(person);
        return "redirect:/people";
    }

    @GetMapping("/{personId}")
    public String show(@PathVariable("personId") int personId, Model model) {
        model.addAttribute("personAdminDTO",
                convertToPersonAdminDTO(peopleService.getPersonWithRoles(personId), modelMapper));
        model.addAttribute("bookAdminDTOs",
                convertToBookAdminDTOCollection(peopleService.findBooksByPersonId(personId), modelMapper));
        return "people/show";
    }

    @GetMapping("/{personId}/edit")
    public String edit(@PathVariable("personId") int person_id, Model model) {
        Person person = peopleService.getPersonWithRoles(person_id);
        model.addAttribute("personAdminDTO", convertToPersonAdminDTO(person, modelMapper));
        model.addAttribute("roleDTO", new RoleDTO());

        List<Role> remainingRoles = rolesService.all();
        for (Role role : person.getRoles()) {
            remainingRoles.removeIf(a -> a.getName().equals(role.getName()));
        }
        model.addAttribute("allRolesDTO", convertToRoleDTOCollection(remainingRoles, modelMapper));
        return "people/edit";
    }

    @PatchMapping("/{personId}/edit")
    public String update(@ModelAttribute("personAdminDTO") @Valid PersonAdminDTO personAdminDTO,
            BindingResult bindingResult, @PathVariable("personId") int personId) {
        Person person = converToPerson(personAdminDTO, modelMapper);
        if (bindingResult.hasErrors())
            return "people/edit";

        peopleService.update(personId, person);
        return "redirect:/people/" + personId;
    }

    @PatchMapping("/{personId}/edit/deleterole")
    public String deleteRole(@ModelAttribute("roleDTO") RoleDTO roleDTO, @PathVariable("personId") int personId) {
        peopleService.deleteRole(personId, convertToRole(roleDTO, modelMapper));
        // sessionService.expireUserSessions(personId);
        return "redirect:/people/" + personId + "/edit";
    }

    @PatchMapping("/{personId}/edit/addrole")
    public String addRole(@ModelAttribute("roleDTO") RoleDTO roleDTO, @PathVariable("personId") int personId) {
        peopleService.addRole(personId, convertToRole(roleDTO, modelMapper));
        // sessionService.expireUserSessions(personId);
        return "redirect:/people/" + personId + "/edit";
    }

    @GetMapping("/{personId}/credentials/edit")
    public String editCredentials(@PathVariable("personId") int person_id, Model model) {
        model.addAttribute("credentialsAdminDTO",
                convertToCredentialsAdminDTO(peopleService.getPersonWithRoles(person_id), modelMapper));
        return "people/credentials/edit";
    }

    @PatchMapping("/{personId}/credentials/edit")
    public String updateCredentials(
            @ModelAttribute("credentialsAdminDTO") @Valid PasswordDTO passwordDTODTO,
            BindingResult bindingResult, @PathVariable("personId") int personId) {
        if (bindingResult.hasErrors())
            return "people/credentials/edit";
        Person person = converToPerson(passwordDTODTO, modelMapper);
        
        peopleService.update(personId, person);
        sessionService.expireUserSessions(personId);
        return "redirect:/people/" + personId;
    }

    @DeleteMapping("/{personId}")
    public String delete(@PathVariable("personId") int personId) {
        peopleService.delete(personId);
        sessionService.expireUserSessions(personId);
        return "redirect:/people";
    }
}
