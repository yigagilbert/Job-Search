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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("jobs")
public class JobController {

    @Autowired
    private JobListingRepository jobListingRepository;

    @Autowired
    private JobListingDetailsRepository jobListingDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("add")
    public String displayAddJobListingForm(
            Principal principal,
            Model model) {

        String username = principal.getName();

        model.addAttribute("title", "Add a New Job Listing");
        model.addAttribute("username", username);

        return "jobs/add";
    }

    @PostMapping("add")
    public String processAddJobListing(
            String username,
            String jobTitle,
            String company,
            String jobListingUrl,
            String jobListingNumber,
            String jobLocation,
            String jobType,
            String salary,
            String jobQualifications,
            String jobDescription
            ) {

        User user = userRepository.findByUsername(username);

        JobListingDetails newJobListingDetails =
                new JobListingDetails(company, jobListingUrl, jobListingNumber,
                        jobLocation, jobType, salary, jobQualifications,
                        jobDescription);

        jobListingDetailsRepository.save(newJobListingDetails);

        JobListing newJobListing = new JobListing(
                jobTitle,
                newJobListingDetails,
                user);

        jobListingRepository.save(newJobListing);

        user.addJobListing(newJobListing);

        return "redirect:../dashboard";
    }

    @GetMapping("{id}")
    public String displayJobListing(@PathVariable String id, Model model) {
        int jobListingId = Integer.parseInt(id);

        Optional<JobListing> result = jobListingRepository.findById(jobListingId);

        if (result.isEmpty()) {
            model.addAttribute("title", "Invalid Job Listing ID: " + jobListingId);
        } else {
            JobListing jobListing = result.get();
            JobListingDetails jobListingDetails = jobListing.getJobListingDetails();
            model.addAttribute("title", jobListingDetails.getCompany() + ": "
                    + jobListing.getJobTitle());
            model.addAttribute("listing", jobListing);
            model.addAttribute("details", jobListingDetails);
            model.addAttribute("id", jobListingId);

            model.addAttribute("contacts", jobListing.getContacts());
        }

        return "jobs/jobListing";
    }

    @GetMapping("edit/{id}")
    public String displayEditJobListingForm(Principal principal, Model model,
                                            @PathVariable String id) {
        int jobListingId = Integer.parseInt(id);
        String username = principal.getName();

        Optional<JobListing> result = jobListingRepository.findById(jobListingId);

        if (result.isEmpty()) {
            model.addAttribute("title", "Invalid Job Listing");
        } else {
            JobListing jobListing = result.get();
            model.addAttribute("title", "Edit Job Listing");
            model.addAttribute("jobListing", jobListing);
        }

        return "jobs/edit";
    }

    @PostMapping("edit/{id}")
    public String processEditJobListing(@PathVariable String id, Model model,
                                        String username,
                                        String jobTitle,
                                        String company,
                                        String jobListingUrl,
                                        String jobListingNumber,
                                        String jobLocation,
                                        String jobType,
                                        String salary,
                                        String jobQualifications,
                                        String jobDescription) {

        Integer jobListingId = Integer.parseInt(id);

        Optional<JobListing> result = jobListingRepository.findById(jobListingId);

        if (result.isEmpty()) {
            model.addAttribute("Title",
                    "Job editing unsuccessful. Please try again.");
        } else {
            JobListing jobListing = result.get();
            jobListing.setJobTitle(jobTitle);
            jobListingRepository.save(jobListing);

            JobListingDetails listingDetails = jobListing.getJobListingDetails();
            Optional<JobListingDetails> resultDetails =
                    jobListingDetailsRepository.findById(listingDetails.getId());

            if (resultDetails.isEmpty()) {
                model.addAttribute("Title",
                        "Job editing unsuccessful. Please try again.");
            } else {
                JobListingDetails jobListingDetails = resultDetails.get();
                jobListingDetails.editJobListingDetails(
                        company,
                        jobListingUrl,
                        jobListingNumber,
                        jobLocation,
                        jobType,
                        salary,
                        jobQualifications,
                        jobDescription);

                jobListingDetailsRepository.save(jobListingDetails);
            }

        }

        return "redirect:/dashboard";
    }

    @PostMapping("{id}")
    public String deleteJobListing(@PathVariable String id, Model model, Principal principal) {

        User user = userRepository.findByUsername(principal.getName());

        int jobListingId = Integer.parseInt(id);

        Optional<JobListing> result = jobListingRepository.findById(jobListingId);

        if (result.isEmpty()) {
            model.addAttribute("title", "Job listing deletion unsuccessful");
        } else {
            JobListing jobListing = result.get();

            JobListingDetails jobListingDetails = jobListing.getJobListingDetails();

            user.deleteJobListing(jobListing);

            jobListingRepository.deleteById(jobListingId);
        }

        return "redirect:/dashboard";
    }
}