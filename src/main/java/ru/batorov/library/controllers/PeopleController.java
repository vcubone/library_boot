package ru.batorov.library.controllers;


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
import ru.batorov.library.models.Credentials;
import ru.batorov.library.models.Person;
import ru.batorov.library.services.CredentialsService;
import ru.batorov.library.services.PeopleService;
import ru.batorov.library.util.CredentialsValidator;
import ru.batorov.library.util.PersonValidator;

import static ru.batorov.library.util.DTOConvert.*;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PersonValidator personValidator;
    private final PeopleService peopleService;
    private final ModelMapper modelMapper;
    private final CredentialsValidator credentialsValidator;
    private final CredentialsService credentialsService;

    public PeopleController(PersonValidator personValidator, PeopleService peopleService, ModelMapper modelMapper,
            CredentialsValidator credentialsValidator, CredentialsService credentialsService) {
        this.personValidator = personValidator;
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
        this.credentialsValidator = credentialsValidator;
        this.credentialsService = credentialsService;
    }

    @GetMapping()
    public String all(Model model){
        model.addAttribute("personUserDTOs", convertToPersonUserDTOList(peopleService.all(), modelMapper));
        return "people/all";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("registrationDTO") RegistrationDTO registrationDTO) {
        return "people/new";
    }
    
    @PostMapping("/new")
	public String performRegistration(@ModelAttribute("registrationDTO") @Valid RegistrationDTO registrationDTO, BindingResult bindingResult) {
		Person person = modelMapper.map(registrationDTO, Person.class);
		Credentials credentials = modelMapper.map(registrationDTO, Credentials.class);
		
		personValidator.validate(person, bindingResult);
		credentialsValidator.validate(credentials, bindingResult);
		
		if (bindingResult.hasErrors())
			return "/people/new";
		credentials.setPerson(person);
		person.setCredentials(credentials);
		
		peopleService.register(person);
		return "redirect:/people";
	}
    
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int personId, Model model) {
        model.addAttribute("personAdminDTO", convertToPersonAdminDTO(peopleService.show(personId), modelMapper));
        model.addAttribute("bookAdminDTOs", convertToBookAdminDTOList(peopleService.getBooksByPersonId(personId), modelMapper));
        return "people/show";
    }
    
    @GetMapping("/{personId}/edit")
    public String edit(@PathVariable("personId") int person_id, Model model) {
        model.addAttribute("personAdminDTO", convertToPersonAdminDTO(peopleService.show(person_id), modelMapper));
        return "people/edit";
    }
    
    @PatchMapping("/{personId}/edit")
    public String update(@ModelAttribute("personAdminDTO") @Valid PersonAdminDTO personAdminDTO, BindingResult bindingResult,@PathVariable("personId") int personId) {
        Person person = converToPerson(personAdminDTO, modelMapper);
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "people/edit";
        
        peopleService.update(personId, person);
        return "redirect:/people/" + personId;
    }
    
    @GetMapping("/{personId}/credentials/edit")
    public String editCredentials(@PathVariable("personId") int person_id, Model model) {
        model.addAttribute("credentialsAdminDTO", convertToCredentialsAdminDTO(credentialsService.show(person_id), modelMapper));
        return "people/credentials/edit";
    }
    
    @PatchMapping("/{personId}/credentials/edit")
    public String updateCredentials(@ModelAttribute("credentialsAdminDTO") @Valid CredentialsAdminDTO credentialsAdminDTO, BindingResult bindingResult,@PathVariable("personId") int personId) {
        Credentials credentials = converToCredentials(credentialsAdminDTO, modelMapper);
        credentialsValidator.validate(credentials, bindingResult);
        credentials.setPerson(null);//enttities id depends on Person, so it initializes
        if (bindingResult.hasErrors())
            return "people/credentials/edit";
            
        credentialsService.update(personId, credentials);
        return "redirect:/people/" + personId;
    }
    
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id)
    {
        peopleService.delete(id);
        return "redirect:/people";
    }
}
