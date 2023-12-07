package com.example.springsocial.config;

import com.example.springsocial.dto.LoginDto;
import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.Post;
import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.Role;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.AuthProvider;
import com.example.springsocial.enums.Country;
import com.example.springsocial.enums.Tag;
import com.example.springsocial.repository.PostRepo;
import com.example.springsocial.repository.ProfileRepo;
import com.example.springsocial.repository.RoleRepo;
import com.example.springsocial.repository.UserRepo;
import com.example.springsocial.service.AuthService;
import com.nimbusds.openid.connect.sdk.assurance.claims.CountryCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class StartApp implements CommandLineRunner {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private ProfileRepo profileRepo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private RoleRepo roleRepo;

  @Autowired
  private PostRepo postRepo;

  @Autowired
  private AuthService authService;



  @Override
  public void run(String... args) throws Exception {
    Role admins = new Role("ADMIN");
    Role users = new Role("USER");
    roleRepo.save(admins);
    roleRepo.save(users);

    Set<Role> admin = new HashSet<>();
    admin.add(roleRepo.findByName("ROLE_ADMIN"));

    Set<Role> user = new HashSet<>();
    user.add(roleRepo.findByName("ROLE_USER"));

    Set<Role> allRoles = new HashSet<>();
    allRoles.add(roleRepo.findByName("ROLE_ADMIN"));
    allRoles.add(roleRepo.findByName("ROLE_USER"));


    User user1 = new User();
    user1.setEmail("as");
    user1.setPassword(passwordEncoder.encode("as"));
    user1.setActive(true);
    user1.setRoles(admin);
    user1.setProvider(AuthProvider.local);

    User user2 = new User();
    user2.setEmail("sakh@gmail.com");
    user2.setPassword(passwordEncoder.encode("aa"));
    user2.setRoles(user);
    user2.setProvider(AuthProvider.local);


    User user3 = new User();
    user3.setEmail("aa");
    user3.setPassword(passwordEncoder.encode("aa"));  
    user3.setActive(true);
    user3.setRoles(allRoles);
    user3.setProvider(AuthProvider.local);
    

    Profile profile1 = new Profile();
    profile1.setBirthDate(LocalDate.of(1995, 12, 21));
    profile1.setCity("annaba");
    profile1.setFirstName("khaled");
    profile1.setLastName("sakhai");
    profile1.setSummary(
      "Welcome, Hi to my profile page, hope this works one day"
    );


   profile1.setProfileCountry("Algeria");
    profile1.setUser(user1);
    user1.setUserProfile(profile1);
    profileRepo.save(profile1);
    userRepo.save(user1);


    Profile profile2 = new Profile();
    profile2.setBirthDate(LocalDate.of(1995, 12, 21));
    profile2.setCity("annaba");
    profile2.setFirstName("khal");
    profile2.setLastName("sak");
    profile2.setSummary(
      "Welcome, Hi to my profile pagr"
    );

    profile2.setUser(user2);
    user2.setUserProfile(profile2);
    profileRepo.save(profile2);
    userRepo.save(user2);


    Profile profile3 = new Profile();
    profile3.setBirthDate(LocalDate.of(2000, 12, 21));
    profile3.setCity("annaba");
    profile3.setFirstName("ahmed");
    profile3.setLastName("meftah");
    profile3.setSummary(
      "Welcome, Hi to my profile page, hope this works one day"
    );
    profile3.setUser(user3);
    user3.setUserProfile(profile3);
    profileRepo.save(profile3);
    userRepo.save(user3);


    Post post = new Post();
    post.setContent("This is how i made 50$ a day using spring bott");
    post.setTitle("Test post only");
    post.setTag(Tag.CAREER);
    postRepo.save(post);


    Image img = new Image();
    
  }
}
