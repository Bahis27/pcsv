package com.testtask.csvp.services;

import com.testtask.csvp.domains.User;
import com.testtask.csvp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public void saveUsers(Collection<User> users) {
        for (User user : users) {
            log.debug(String.format("%sNumber %s. Status - STORE.%s", "\u001b[0;34m", user.getMsisdn(), "\u001b[m"));
        }

        List<User> savedUsers = userRepository.saveAll(users);

        for (User user : savedUsers) {
            log.debug(String.format("%sNumber - %s. Status - STORE_SUCCESSFUL!%s", "\u001b[0;32m", user.getMsisdn(), "\u001b[m"));
        }
    }

    public List<User> getUsersByMsisds(Set<String> keySet) {
        return userRepository.findAllByMsisdnIn(keySet);
    }
}
