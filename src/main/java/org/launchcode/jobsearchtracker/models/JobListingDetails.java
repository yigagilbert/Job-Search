package org.launchcode.jobsearchtracker.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
public class JobListingDetails extends AbstractEntity {

    @NotEmpty
    @Size(min=2, max=50, message="Company must be between 2 and 50 characters")
    private String company;
//    @NotEmpty
    private String jobListingUrl;

    private String jobListingNumber;

    private String jobLocation;

    private String jobType;

    private String jobSalary;

    private String jobQualifications;

    private String jobDescription;

    // https://vladmihalcea.com/the-best-way-to-map-a-onetoone-relationship-with-jpa-and-hibernate/
    @OneToOne(//fetch = FetchType.LAZY,
            mappedBy = "jobListingDetails")
//    @MapsId
    private JobListing jobListing;

    public JobListingDetails() {
    }

    public JobListingDetails(String company) {
        this.company = company;
    }

    public JobListingDetails(
            String company,
//            JobListing jobListing,
                             String jobListingUrl,
                             String jobListingNumber,
                             String jobLocation,
                             String jobType,
                             String jobSalary,
                             String jobQualifications,
                             String jobDescription) {
//        this.jobListing = jobListing;
        this.company = company;

        if (jobListingUrl == null) {
            this.jobListingUrl = "";
        } else {
            this.jobListingUrl = jobListingUrl;
        }

        if (jobListingNumber == null) {
            this.jobListingNumber = "";
        } else {
            this.jobListingNumber = jobListingNumber;
        }

        if (jobLocation == null) {
            this.jobLocation = "";
        } else {
            this.jobLocation = jobLocation;
        }

        if (jobType == null) {
            this.jobType = "";
        } else {
            this.jobType = jobType;
        }

        if (jobSalary == null) {
            this.jobSalary = "";
        } else {
            this.jobSalary = jobSalary;
        }

        if (jobQualifications == null) {
            this.jobQualifications = "";
        } else {
            this.jobQualifications = jobQualifications;
        }

        if (jobDescription == null) {
            this.jobDescription = "";
        } else {
            this.jobDescription = jobDescription;
        }
    }

    public void editJobListingDetails(String company, String jobListingUrl,
                                     String jobListingNumber, String jobLocation,
                                     String jobType, String jobSalary,
                                     String jobQualifications, String jobDescription) {
        setCompany(company);
        setJobListingUrl(jobListingUrl);
        setJobListingNumber(jobListingNumber);
        setJobLocation(jobLocation);
        setJobType(jobType);
        setJobSalary(jobSalary);
        setJobQualifications(jobQualifications);
        setJobDescription(jobDescription);
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public JobListing getJobListing() {
        return jobListing;
    }

    public void setJobListing(JobListing jobListing) {
        this.jobListing = jobListing;
    }

    public String getJobListingUrl() {
        return jobListingUrl;
    }

    public void setJobListingUrl(String jobListingUrl) {
        this.jobListingUrl = jobListingUrl;
    }

    public String getJobListingNumber() {
        return jobListingNumber;
    }

    public void setJobListingNumber(String jobListingNumber) {
        this.jobListingNumber = jobListingNumber;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobSalary() {
        return jobSalary;
    }

    public void setJobSalary(String jobSalary) {
        this.jobSalary = jobSalary;
    }

    public String getJobQualifications() {
        return jobQualifications;
    }

    public void setJobQualifications(String jobQualifications) {
        this.jobQualifications = jobQualifications;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }
}
