package org.launchcode.jobsearchtracker.models;

import org.launchcode.jobsearchtracker.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void processOAuthPostLogin(String username, String email) {
        User existingUser = userRepository.findByUsername(username);

        if (existingUser == null) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setProvider(Provider.GOOGLE);
            newUser.setEnabled(true);

            userRepository.save(newUser);
        }
    }

}
