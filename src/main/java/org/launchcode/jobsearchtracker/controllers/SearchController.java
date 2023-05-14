package org.launchcode.jobsearchtracker.controllers;

import org.launchcode.jobsearchtracker.data.UserRepository;
import org.launchcode.jobsearchtracker.models.JobListing;
import org.launchcode.jobsearchtracker.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("search")
public class SearchController {

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public String displaySearchForm(Model model) {
        
        model.addAttribute("title", "Search My Jobs");

        return "search";
    }

    @PostMapping("results")
    public String processSearchForm(String searchCategory,
                                    String searchTerm,
                                    Model model,
                                    Principal principal) {

        User user = userRepository.findByUsername(principal.getName());

        Iterable<JobListing> jobListings = user.findJobListingsByValue(searchCategory, searchTerm, user.getJobListings());

        if (jobListings != null)
        {
            model.addAttribute("title", "Jobs with " + searchCategory + ": " + searchTerm);
            model.addAttribute("jobListings", jobListings);
        } else {
            model.addAttribute("title", "There are no jobs that match the search term \"" + searchTerm + "\".");
        }


        return "list-jobs";
    }
}
