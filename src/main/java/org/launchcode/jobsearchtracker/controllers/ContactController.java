package org.launchcode.jobsearchtracker.controllers;

import org.launchcode.jobsearchtracker.data.ContactRepository;
import org.launchcode.jobsearchtracker.data.JobListingRepository;
import org.launchcode.jobsearchtracker.data.UserRepository;
import org.launchcode.jobsearchtracker.models.Contact;
import org.launchcode.jobsearchtracker.models.JobListing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("contacts")
public class ContactController {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    JobListingRepository jobListingRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("add/{id}")
    public String displayAddContactForm(@PathVariable String id, Model model) {

        Integer jobId = Integer.parseInt(id);

        model.addAttribute("title", "Add Contact");
        model.addAttribute("subtitle", jobListingRepository.getById(jobId).getJobTitle());
        model.addAttribute("company", jobListingRepository.getById(jobId).getJobListingDetails().getCompany());
        model.addAttribute("contact", new Contact());
        return "contacts/add-contact";
    }

    @PostMapping("add/{id}")
    public String processAddContactForm(@ModelAttribute @Valid Contact newContact,
                                        @PathVariable String id,
                                        Model model) {

        Optional<JobListing> result  = jobListingRepository.findById(Integer.parseInt(id));

        if (result.isPresent()) {
            JobListing jobListing = result.get();

            if (!jobListing.getContacts().contains(newContact)) {
                jobListing.addContact(newContact);
            }

            model.addAttribute("jobId", id);
        }

        contactRepository.save(newContact);

        return "redirect:../../jobs/" + id;
    }

    @GetMapping("edit/{contactId}")
    public String displayEditContactForm(@PathVariable String contactId,
                                         Model model) {

        Optional<Contact> result = contactRepository.findById(Integer.parseInt(contactId));

        if (result.isPresent()) {
            Contact theContact = result.get();

            int jobId = theContact.getJobListings().get(0).getId();

            model.addAttribute("title", "Edit Contact");
            model.addAttribute("subtitle", jobListingRepository.getById(jobId).getJobTitle());
            model.addAttribute("company", jobListingRepository.getById(jobId).getJobListingDetails().getCompany());
            model.addAttribute("contact", theContact);
            model.addAttribute("id", jobId);
        }

        return "contacts/edit-contact";
    }

    @PostMapping("edit/{contactId}")
    public String processEditContactForm(@PathVariable String contactId,
                                         Model model, String firstName,
                                         String jobTitle, String lastName,
                                         String phoneNumber, String email,
                                         String notes) {

        System.out.println("***** Inside ContactController: processEditContactForm() *****");

        Optional<Contact> result = contactRepository.findById(Integer.parseInt(contactId));

        System.out.println("***** Artificial Breakpoint *****");

        int jobListingId = 0;

        if (result.isPresent()) {
            Contact contact = result.get();

            jobListingId = contact.getJobListings().get(0).getId();

            contact.editContact(firstName, lastName, jobTitle, phoneNumber, email, notes);

            contactRepository.save(contact);
        }

        return "redirect:../../jobs/" + String.valueOf(jobListingId);
    }

    @PostMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable String contactId, Model model) {

        System.out.println("***** Inside ContactController: deleteContact() *****");

        int jobListingId = 0;

        Optional<Contact> result = contactRepository.findById(Integer.parseInt(contactId));

        if (result.isPresent()) {
            Contact contact = result.get();

            jobListingId = contact.getJobListings().get(0).getId();

            JobListing jobListing = jobListingRepository.getById(jobListingId);
            jobListing.removeContact(contact);
            model.addAttribute("id", jobListingId);

            contactRepository.deleteById(Integer.parseInt(contactId));
        }

        return "redirect:../../jobs/" + String.valueOf(jobListingId);
    }
}
