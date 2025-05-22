package edu.diu.cis.jobportal.service;

import edu.diu.cis.jobportal.model.Profile;
import edu.diu.cis.jobportal.model.User;
import edu.diu.cis.jobportal.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;
    
    public Profile getProfileByUser(User user) {
        return profileRepository.findByUser(user);
    }
    
    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }
    
    public Profile updateProfile(Profile profile) {
        return profileRepository.save(profile);
    }
}