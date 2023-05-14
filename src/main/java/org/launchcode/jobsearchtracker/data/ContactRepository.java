package org.launchcode.jobsearchtracker.data;

import org.launchcode.jobsearchtracker.models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
}
