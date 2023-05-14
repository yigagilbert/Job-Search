package org.launchcode.jobsearchtracker.data;

import org.launchcode.jobsearchtracker.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{

    User findByUsername(String username);



//    @Query("SELECT u FROM User u WHERE u.username = :username")
//    public User getUserByUsername(@Param("username") String username);
}
