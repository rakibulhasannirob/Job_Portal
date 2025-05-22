package edu.diu.cis.jobportal.repository;

import edu.diu.cis.jobportal.model.Profile;
import edu.diu.cis.jobportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByUser(User user);
}