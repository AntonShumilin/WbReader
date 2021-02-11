package com.WbReader.Services;

import com.WbReader.CustomExeptions.UserAlreadyExistsException;
import com.WbReader.Data.Role;
import com.WbReader.Data.User;
import com.WbReader.Data.UserRepo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    Logger LOGGER;

    @Autowired
    UserRepo userRepo;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public User findByUserName(String username) {
        return (User)loadUserByUsername(username);

    }

    public boolean addNewUser(User user) throws UserAlreadyExistsException {
        User userFromDB = userRepo.findByUsername(user.getUsername());
        if (userFromDB != null) {
            throw new UserAlreadyExistsException("В базе уже есть пользователь с именем " +  user.getUsername());
        }
        userRepo.save(user);
        LOGGER.info("Добавален новый пользователь: {} id: {}", user.getUsername(), user.getId());
        return true;
    }

    public boolean deleteUser(Long userId) {
        if (userRepo.findById(userId).isPresent()) {
            userRepo.deleteById(userId);
            return true;
        }
        return false;
    }
}
