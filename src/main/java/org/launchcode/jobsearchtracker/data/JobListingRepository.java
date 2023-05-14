package org.launchcode.jobsearchtracker.data;

import org.launchcode.jobsearchtracker.models.JobListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobListingRepository extends JpaRepository<JobListing, Integer> {
}
