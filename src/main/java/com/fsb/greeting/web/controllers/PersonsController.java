package com.fsb.greeting.web.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsb.greeting.business.services.PersonService;
import com.fsb.greeting.dao.entities.Person;
import com.fsb.greeting.web.models.requests.PersonForm;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/* Carte d'opération CRUD
 * Read Get  /persons : Récuperer une liste de personne
 * 
 * Create - Get   /persons/create : Récupérer le formulaire d'ajout d'une nouvelle personne
 *        - Post  /persons/create : Ajouter une nouvelle personne à la liste persons
 * 
 * Update - Get   /persons/{id}/edit : Récupérer le formulaire de modificatin d'une personne
 *        - Post  /persons/{id}/edit : Permet de mettre à jour une  personne de la liste persons
 * 
 * Delete - Post /persons/{id}/delete : Supprimer une personne de la liste persons par son id
 */

@Controller
@RequestMapping("/persons")
public class PersonsController {
    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images";

    private static List<Person> persons = new ArrayList<Person>();
    private static Long idCount = 0L;

    static {
        persons.add(new Person(++idCount, "demo1", (short) 20, "men.png"));
        persons.add(new Person(++idCount, "demo2", (short) 30, "women.png"));
        persons.add(new Person(++idCount, "demo3", (short) 40, null));
        persons.add(new Person(++idCount, "demo4", (short) 50, "men.png"));
    }

    //injection par constructeur
    private final PersonService personService;
    public PersonsController(PersonService personService) {
        this.personService=personService;
    }

    @RequestMapping()
    public String getAllPerson(Model model) {
       // model.addAttribute("persons", persons);
       model.addAttribute("persons", this.personService.getAllPerson());
        return "person-list";
    }

    // Create - Get /persons/create : Récupérer le formulaire d'ajout d'une nouvelle
    // personne
    @RequestMapping("/create")
    public String showAddPersonForm(Model model) {
        model.addAttribute("personForm", new PersonForm());
        return "add-person";
    }

    // Create - Post /persons/create : Ajouter une nouvelle personne à la liste
    // persons
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public String addPerson(@Valid @ModelAttribute PersonForm personForm,
            BindingResult bindingResult,
            Model model,
            @RequestParam MultipartFile file) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Invalid input");
            return "add-person";
        }
        if (!file.isEmpty()) {
            // Création d'un objet StringBuilder pour stocker le nom du fichier
            StringBuilder fileName = new StringBuilder();
            // Ajout du nom de fichier original à l'objet StringBuilder
            fileName.append(file.getOriginalFilename());
            // Construction du chemin complet du fichier en combinant le répertoire de
            // destination et le nom du fichier
            Path newFilePath = Paths.get(uploadDirectory, fileName.toString());

            try {
                // Écriture du contenu du fichier dans le chemin spécifié
                Files.write(newFilePath, file.getBytes());
            } catch (IOException e) {
                // Capture et affichage des erreurs éventuelles lors de l'écriture du fichier
                e.printStackTrace();
            }
            // Ajouter la nouvelle personne à la liste persons
           // persons.add(new Person(idCount++, personForm.getName(), personForm.getAge(), fileName.toString()));
          this.personService.addPerson(new Person(null, personForm.getName(), personForm.getAge(), fileName.toString()));
            
        } else {
            // Ajouter une nouvelle personne sans image à la liste persons
            //persons.add(new Person(idCount++, personForm.getName(), personForm.getAge(), null));
            this.personService.addPerson(new Person(null, personForm.getName(), personForm.getAge(), null));
        }
        // persons.add(new Person(++idCount, personForm.getName(), personForm.getAge(),
        // null));
        return "redirect:/persons";
    }

    /*
     * Update - GET /persons/{id}/edit : Récupérer le formulaire de modificatin
     * d'une personne
     */
    @RequestMapping("/{id}/edit")
    public String showUpdatePersonForm(@PathVariable Long id, Model model) {
        // Chercher la personne par son id
        for (Person person : persons) {
            if (person.getId() == id) {
                model.addAttribute("personForm", new PersonForm(person.getName(), person.getAge(), person.getPhoto()));
                model.addAttribute("id", person.getId());
                return "update-person";
            }
        }
        return "redirect:/persons";
    }

    /*
     * Update - POST /persons/{id}/edit : Permet de mettre à jour une personne de la
     * liste persons
     */
    @RequestMapping(path = "{id}/edit", method = RequestMethod.POST)
    public String updatePerson(@Valid @ModelAttribute PersonForm personForm,
            BindingResult bindingResult,
            @PathVariable Long id,
            Model model,
            @RequestParam MultipartFile file) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Invalid input");
            return "update-person";
        }

        for (Person person : persons) {
            if (person.getId() == id) {
                person.setName(personForm.getName());
                person.setAge(personForm.getAge());
                if (!file.isEmpty()) {
                    StringBuilder fileName = new StringBuilder();
                    Path newFilePath = Paths.get(uploadDirectory, file.getOriginalFilename());
                    fileName.append(file.getOriginalFilename());
                    try {
                        Files.write(newFilePath, file.getBytes());
                        // Supprimer le fichier de photo si existe
                        if (person.getPhoto() != null) {
                            Path filePath = Paths.get(uploadDirectory, person.getPhoto());
                            try {
                                Files.deleteIfExists(filePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    person.setPhoto(fileName.toString());
                }
                break;
            }
        }

        return "redirect:/persons";
    }

    /* Exemple: Post /persons/1/delete */
    @RequestMapping(path = "/{id}/delete", method = RequestMethod.POST)
    public String deletePersonById(@PathVariable Long id) {

        for (Person person : persons) {
            if (person.getId() == id) {
                // Supprimer le fichier de photo si existe
                if (person.getPhoto() != null) {
                    Path filePath = Paths.get(uploadDirectory, person.getPhoto());
                    try {
                        Files.deleteIfExists(filePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // Supprimer la personne de la liste
                persons.remove(person);

                break;
            }
        }
        return "redirect:/persons";

    }
}