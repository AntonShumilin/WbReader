package com.WbReader.Services;

import com.WbReader.Data.Role;
import com.WbReader.Data.User;
import com.WbReader.Repository.UserRepo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public Optional<User> findByUserName(String username) {
        return Optional.ofNullable(userRepo.findByUsername(username));
    }

    public void addNewUser(User user)  {
        userRepo.save(user);
        LOGGER.info("Добавален новый пользователь: {} id: {}", user.getUsername(), user.getId());
    }

    public User addNewUser(String username, String rawPassword, boolean isAdmin)  {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(bCryptPasswordEncoder.encode(rawPassword));
        Set<Role> userRoles = isAdmin ? Stream.of(Role.USER, Role.ADMIN).collect(Collectors.toSet()) : Collections.singleton(Role.USER);
        newUser.setRoles(userRoles);
        addNewUser(newUser);
        return newUser;
    }

    public boolean deleteUser(Long userId) {
        if (userRepo.findById(userId).isPresent()) {
            userRepo.deleteById(userId);
            return true;
        }
        return false;
    }
}
