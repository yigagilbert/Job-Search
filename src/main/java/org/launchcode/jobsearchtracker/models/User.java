package org.launchcode.jobsearchtracker.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends AbstractEntity {

    @NotBlank(message="Username is required")
    @Size(min=3, max=20, message="Username must be between 3 and 20 characters")
    private String username;

//    @NotBlank
    private String password;

    // Added for OAuth2 authentication
    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String role;

    @Email
    private String email;

    @Min(0)
    private int dailyGoal = 0;

    private boolean enabled;

    @OneToMany(mappedBy = "user")
    private final List<JobListing> jobListings = new ArrayList<>();

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDailyGoal() {
        return dailyGoal;
    }

    public void setDailyGoal(int dailyGoal) {
        this.dailyGoal = dailyGoal;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<JobListing> getJobListings() {
        return jobListings;
    }

    public void addJobListing(JobListing jobListing) {
        this.jobListings.add(jobListing);
    }

    public boolean deleteJobListing(JobListing jobListing) {
        return this.jobListings.remove(jobListing);
    }

    public static ArrayList<JobListing> findJobListingsByValue(String fieldName, String value,
                                                      Iterable<JobListing> jobListings) {
        String lower_val = value.toLowerCase();

        ArrayList<JobListing> results = new ArrayList<>();

        if (fieldName.equals("company")) {
            for (JobListing job : jobListings) {
                JobListingDetails jobListingDetails = job.getJobListingDetails();

                String company = jobListingDetails.getCompany();
                if (company != null && company.toLowerCase().contains(lower_val)) {
                    results.add(job);
                }
            }
        } else if (fieldName.equals("jobType")) {
            for (JobListing job : jobListings) {
                JobListingDetails jobListingDetails = job.getJobListingDetails();

                String jobType = jobListingDetails.getJobType();
                if (jobType != null && jobType.toLowerCase().contains(lower_val)) {
                    results.add(job);
                }
            }
        } else if (fieldName.equals("location")) {
            for (JobListing job : jobListings) {
                JobListingDetails jobListingDetails = job.getJobListingDetails();

                String location = jobListingDetails.getJobLocation();
                if (location != null && location.toLowerCase().contains(lower_val)) {
                    results.add(job);
                }
            }
        } else if (fieldName.equals("jobTitle")) {
            for (JobListing job : jobListings) {
                String jobTitle = job.getJobTitle();

                if (jobTitle != null && jobTitle.toLowerCase().contains(lower_val)) {
                    results.add(job);
                }
            }
        }

        return results;
    }

}
