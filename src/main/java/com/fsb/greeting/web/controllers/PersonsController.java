package com.fsb.greeting.web.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsb.greeting.web.models.Person;
import com.fsb.greeting.web.models.requests.PersonForm;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;


/* Carte d'opération CRUD
 * Read Get  /persons : Récuperer une liste de personne
 * 
 * Create - Get   /persons/create : Récupérer le formulaire d'ajout d'une nouvelle personne
 *        - Post  /persons/create : Ajouter une nouvelle personne à la liste persons
 * 
 * Update - Get   /persons/{id}/edit : Récupérer le formulaire de modificatin d'une personne
 *        - Post  /persons/{id}/edit : Mettre à jour une  personne de la liste persons
 * 
 * Delete - Post /persons/{id}/delete : Supprimer une personne de la liste persons par son id
 */

@Controller
@RequestMapping("/persons")
public class PersonsController {
    private static List<Person> persons = new ArrayList<Person>();
    private static Long idCount = 0L;

    static {
        persons.add(new Person(++idCount, "demo1", (short) 20, "men.png"));
        persons.add(new Person(++idCount, "demo2", (short) 30, "women.png"));
        persons.add(new Person(++idCount, "demo3", (short) 40, null));
        persons.add(new Person(++idCount, "demo4", (short) 50, "men.png"));
    }
    @RequestMapping()
    public String getAllPerson(Model model) {
        model.addAttribute("persons", persons);
        return "person-list";
    }
    //Create - Get   /persons/create : Récupérer le formulaire d'ajout d'une nouvelle personne
    @RequestMapping("/create")
    public String showAddPersonForm(Model model) {
        model.addAttribute("personForm", new PersonForm());
        return "add-person";
    }
    //Create - Post  /persons/create : Ajouter une nouvelle personne à la liste persons
    @RequestMapping(path="/create", method=RequestMethod.POST)
    public String addPerson(@ModelAttribute @Valid PersonForm personForm, BindingResult bindingResult ) {
        if(bindingResult.hasErrors()){
            return "add-person";
        }
        Person person=new Person(++idCount, personForm.getName(), personForm.getAge(), null);
        persons.add(person);
        return "redirect:/persons";
    }
    
    /*Exemple: Post /persons/1/delete */
    @RequestMapping(path="/{id}/delete", method=RequestMethod.POST)
    public String deletePersonById(@PathVariable Long id ) {
        for (Person person : persons) {
            if (person.getId() == id) {
                persons.remove(person);
                break;
            }
        }
        return "redirect:/persons";
    }
    
}