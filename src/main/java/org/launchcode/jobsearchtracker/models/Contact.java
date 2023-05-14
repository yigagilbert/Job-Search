package org.launchcode.jobsearchtracker.models;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Contact extends AbstractEntity {

    @Size(min=2, max=35, message="Contact first name must bet between 2 and 35 characters")
    private String firstName;

    private String lastName;

    private String jobTitle;

    private String phoneNumber;

    private String email;

    private String notes;

    @ManyToMany(mappedBy="contacts")
    private final List<JobListing> jobListings = new ArrayList<>();

    public Contact() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<JobListing> getJobListings() {
        return jobListings;
    }

    public void addJobListing(JobListing job) {
        jobListings.add(job);
    }

    public void removeJobListing(JobListing job) {
        jobListings.remove(job);
    }

    public void editContact(String firstName, String lastName, String jobTitle,
                            String phoneNumber, String email, String notes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.notes = notes;
    }
}
