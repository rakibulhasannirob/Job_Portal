package edu.diu.cis.jobportal.service;

import edu.diu.cis.jobportal.model.Profile;
import edu.diu.cis.jobportal.model.User;
import edu.diu.cis.jobportal.repository.ProfileRepository;
import edu.diu.cis.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProfileRepository profileRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Transactional
    public User registerJobSeeker(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addRole("ROLE_USER");
        User savedUser = userRepository.save(user);
        
        Profile profile = new Profile();
        profile.setUser(savedUser);
        profileRepository.save(profile);
        savedUser.setProfile(profile);
        
        return savedUser;
    }
    
    @Transactional
    public User registerEmployer(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addRole("ROLE_EMPLOYER");
        User savedUser = userRepository.save(user);
        
        Profile profile = new Profile();
        profile.setUser(savedUser);
        profileRepository.save(profile);
        savedUser.setProfile(profile);
        
        return savedUser;
    }
    
    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}