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

import ru.batorov.library.dto.CredentialsAdminDTO;
import ru.batorov.library.dto.PersonAdminDTO;
import ru.batorov.library.dto.RegistrationDTO;
import ru.batorov.library.dto.RoleDTO;
import ru.batorov.library.models.Person;
import ru.batorov.library.models.Role;
import ru.batorov.library.services.PeopleService;
import ru.batorov.library.services.RolesService;
import ru.batorov.library.util.PersonsCredentialsValidator;

import static ru.batorov.library.util.DTOConvert.*;

import java.util.List;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final ModelMapper modelMapper;
    private final PersonsCredentialsValidator credentialsValidator;
    private final RolesService rolesService;

    public PeopleController(PeopleService peopleService, ModelMapper modelMapper,
            PersonsCredentialsValidator credentialsValidator, RolesService rolesService) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
        this.credentialsValidator = credentialsValidator;
        this.rolesService = rolesService;
    }

    @GetMapping()
    public String all(Model model) {
        model.addAttribute("personUserDTOs", convertToPersonAdminDTOCollection(peopleService.allWithRoles(), modelMapper));
        return "people/all";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("registrationDTO") RegistrationDTO registrationDTO) {
        return "people/new";
    }

    @PostMapping("/new")
    public String performRegistration(@ModelAttribute("registrationDTO") @Valid RegistrationDTO registrationDTO,
            BindingResult bindingResult) {
        Person person = modelMapper.map(registrationDTO, Person.class);

        credentialsValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "/people/new";

        peopleService.register(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int personId, Model model) {
        model.addAttribute("personAdminDTO", convertToPersonAdminDTO(peopleService.showWithRoles(personId), modelMapper));
        model.addAttribute("bookAdminDTOs",
                convertToBookAdminDTOCollection(peopleService.getBooksByPersonId(personId), modelMapper));
        return "people/show";
    }

    @GetMapping("/{personId}/edit")
    public String edit(@PathVariable("personId") int person_id, Model model) {
        Person person = peopleService.showWithRoles(person_id);
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
    public String deleteRole(@ModelAttribute("roleDTO") RoleDTO roleDTO, @PathVariable("personId") int personId )
    {
        peopleService.deleteRole(personId, convertToRole(roleDTO, modelMapper));
        return "redirect:/people/" + personId + "/edit";
    }
    
    @PatchMapping("/{personId}/edit/addrole")
    public String addRole(@ModelAttribute("roleDTO") RoleDTO roleDTO, @PathVariable("personId") int personId )
    {
        peopleService.addRole(personId, convertToRole(roleDTO, modelMapper));
        return "redirect:/people/" + personId + "/edit";
    }


    @GetMapping("/{personId}/credentials/edit")
    public String editCredentials(@PathVariable("personId") int person_id, Model model) {
        model.addAttribute("credentialsAdminDTO",
                convertToCredentialsAdminDTO(peopleService.showWithRoles(person_id), modelMapper));
        return "people/credentials/edit";
    }

    @PatchMapping("/{personId}/credentials/edit")
    public String updateCredentials(
            @ModelAttribute("credentialsAdminDTO") @Valid CredentialsAdminDTO credentialsAdminDTO,
            BindingResult bindingResult, @PathVariable("personId") int personId) {
        Person person = converToPerson(credentialsAdminDTO, modelMapper);
        person.setId(personId);
        credentialsValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "people/credentials/edit";

        peopleService.update(personId, person);
        return "redirect:/people/" + personId;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }
}
