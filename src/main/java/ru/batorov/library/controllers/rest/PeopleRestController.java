package ru.batorov.library.controllers.rest;

import java.util.Collection;
import javax.validation.Valid;

import static ru.batorov.library.util.DTOConvert.*;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.batorov.library.dto.RoleDTO;
import ru.batorov.library.dto.credentials.PasswordDTO;
import ru.batorov.library.dto.person.PersonAdminDTO;
import ru.batorov.library.dto.person.PersonWithBooksAdminDTO;
import ru.batorov.library.dto.person.PersonRegistrationDTO;
import ru.batorov.library.models.Person;
import ru.batorov.library.services.PeopleService;
import ru.batorov.library.util.UsernameValidator;
import ru.batorov.library.util.exceptions.ErrorsGetter;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/people")
@Tag(name = "People", description = "The People API. Admin only. Contains all the operations that can be performed with a user.")
public class PeopleRestController {
    private final PeopleService peopleService;
    private final ModelMapper modelMapper;
    private final UsernameValidator usernameValidator;

    public PeopleRestController(PeopleService peopleService, ModelMapper modelMapper,
            UsernameValidator usernameValidator) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
        this.usernameValidator = usernameValidator;
    }

    @Operation(summary = "Gets all people", tags = "People", security = @SecurityRequirement(name = "Bearer Authentication"))
    @GetMapping()
    public Collection<PersonAdminDTO> all(Model model) {
        return convertToPersonAdminDTOCollection(peopleService.allWithRoles(), modelMapper);
    }

    @PostMapping("/new")
    @Operation(summary = "Creates new person", tags = "People", security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponse(responseCode = "400", description = "Bad input values")
    public ResponseEntity<HttpStatus> performRegistration(@RequestBody @Valid PersonRegistrationDTO registrationDTO,
            BindingResult bindingResult) {
        Person person = modelMapper.map(registrationDTO, Person.class);

        usernameValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            throw new IllegalArgumentException(ErrorsGetter.getErrors(bindingResult));

        peopleService.register(person);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Shows user info", tags = "People", security = @SecurityRequirement(name = "Bearer Authentication"))
    @GetMapping("/{id}")
    public PersonWithBooksAdminDTO show(@PathVariable("id") int personId, Model model) {
        Person person = peopleService.getPersonWithRolesAndBooks(personId);
        PersonWithBooksAdminDTO personWithBooksAdminDTO = convertToPersonWithBooksAdminDTO(person, modelMapper);
        return personWithBooksAdminDTO;
    }

    // TODO do i need to change dto?(remove unused role field)
    @PatchMapping("/{personId}/edit")
    @Operation(summary = "Updates users information", description = "Igrore role fiels", tags = "People", security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponse(responseCode = "400", description = "Bad input values")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid PersonAdminDTO personAdminDTO,
            @ApiIgnore BindingResult bindingResult, @PathVariable("personId") int personId) {
        Person person = converToPerson(personAdminDTO, modelMapper);
        person.setRoles(null);
        if (bindingResult.hasErrors())
            throw new IllegalArgumentException(ErrorsGetter.getErrors(bindingResult));

        peopleService.update(personId, person);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{personId}/credentials/edit")
    @Operation(summary = "Updates users credentials", tags = "People", security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponse(responseCode = "400", description = "Bad input values")
    public ResponseEntity<HttpStatus> updateCredentials(
            @RequestBody @Valid PasswordDTO passwordDTO,
            @ApiIgnore BindingResult bindingResult, @PathVariable("personId") int personId) {
        if (bindingResult.hasErrors())
            throw new IllegalArgumentException(ErrorsGetter.getErrors(bindingResult));
        Person person = converToPerson(passwordDTO, modelMapper);

        peopleService.update(personId, person);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Delets users role", tags = "People", security = @SecurityRequirement(name = "Bearer Authentication"))
    @PatchMapping("/{personId}/edit/deleterole")
    public ResponseEntity<HttpStatus> deleteRole(@RequestBody RoleDTO roleDTO, @PathVariable("personId") int personId) {
        peopleService.deleteRole(personId, convertToRole(roleDTO, modelMapper));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Adds role to user", tags = "People", security = @SecurityRequirement(name = "Bearer Authentication"))
    @PatchMapping("/{personId}/edit/addrole")
    public ResponseEntity<HttpStatus> addRole(@RequestBody RoleDTO roleDTO, @PathVariable("personId") int personId) {
        peopleService.addRole(personId, convertToRole(roleDTO, modelMapper));
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
