package org.launchcode.jobsearchtracker.data;

import org.launchcode.jobsearchtracker.models.JobListingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobListingDetailsRepository extends JpaRepository<JobListingDetails, Integer> {

}
