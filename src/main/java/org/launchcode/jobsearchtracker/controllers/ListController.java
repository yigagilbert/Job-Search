package org.launchcode.jobsearchtracker.controllers;

import org.launchcode.jobsearchtracker.data.JobListingDetailsRepository;
import org.launchcode.jobsearchtracker.data.JobListingRepository;
import org.launchcode.jobsearchtracker.data.UserRepository;
import org.launchcode.jobsearchtracker.models.JobListing;
import org.launchcode.jobsearchtracker.models.JobListingDetails;
import org.launchcode.jobsearchtracker.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("list")
public class ListController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JobListingRepository jobListingRepository;

    @Autowired
    JobListingDetailsRepository jobListingDetailsRepository;

    @GetMapping
    public String listJobs(Model model, Principal principal) {

        User user = userRepository.findByUsername(principal.getName());

        List<String> companies = new ArrayList<>();
        List<String> jobTypes = new ArrayList<>();
        List<String> locations = new ArrayList<>();

        for (JobListing jobListing : user.getJobListings()) {
            JobListingDetails jobListingDetails = jobListing.getJobListingDetails();

            String company = jobListingDetails.getCompany();

            if (!companies.stream().anyMatch(company::equalsIgnoreCase)) {
                companies.add(company);
            }

            String jobType = jobListingDetails.getJobType();

            if (jobType != null && !jobType.equals("") && !jobTypes.stream().anyMatch(jobType::equalsIgnoreCase)) {
                jobTypes.add(jobType);
            }

            String location = jobListingDetails.getJobLocation();

            if (location != null && !location.equals("") && !locations.stream().anyMatch(location::equalsIgnoreCase)) {
                locations.add(location);
            }
        }

        model.addAttribute("companies", companies);
        model.addAttribute("jobTypes", jobTypes);
        model.addAttribute("locations", locations);

        return "list";
    }

    @RequestMapping(value="jobs")
    public String listJobsByValue(Model model, @RequestParam String fieldName, @RequestParam String value,
                                  Principal principal) {
        User user = userRepository.findByUsername(principal.getName());

        Iterable<JobListing> jobListings = user.findJobListingsByValue(fieldName, value, user.getJobListings());

        model.addAttribute("title", "Jobs with " + fieldName + ": " + value);

        model.addAttribute("jobListings", jobListings);

        return "list-jobs";
    }
}
