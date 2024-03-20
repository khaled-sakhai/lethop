package com.example.springsocial.config;

import com.example.springsocial.dto.user.LoginDto;
import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.postRelated.Category;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Tag;
import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.Role;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.APPRole;
import com.example.springsocial.enums.AuthProvider;
import com.example.springsocial.enums.Country;
import com.example.springsocial.repository.CategoryRepo;
import com.example.springsocial.repository.PostRepo;
import com.example.springsocial.repository.ProfileRepo;
import com.example.springsocial.repository.RoleRepo;
import com.example.springsocial.repository.TagRepo;
import com.example.springsocial.repository.UserRepo;
import com.example.springsocial.service.AuthService;
import com.nimbusds.openid.connect.sdk.assurance.claims.CountryCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/// stuff to run when the app run (usally we test things here)
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

  @Autowired
  private CategoryRepo categoryRepo;
  @Autowired
  private TagRepo tagRepo;
 

@Transactional
  @Override
  public void run(String... args) throws Exception {
    Role admin = new Role(APPRole.ROLE_ADMIN);
    Role user = new Role(APPRole.ROLE_USER);

        Role roleAdmin=roleRepo.save(admin);
        Role roleUser = roleRepo.save(user);

        User user1 = new User();
        user1.setEmail("as@as.com");
        user1.setPassword(passwordEncoder.encode("123456789"));
        user1.setActive(true);
    user1.setProvider(AuthProvider.local);
    userRepo.save(user1);
    user1.addRoles(roleAdmin);

    Profile profile1 = new Profile();
    profile1.setBirthDate(LocalDate.of(1995, 12, 21));
    profile1.setCity("annaba");
    profile1.setFirstName("khaled");
    profile1.setLastName("sakhai");
    profile1.setSummary(
      "Welcome, Hi to my profile page, hope this works one day"
    );
    profile1.setProfileCountry("Algeria");
    profileRepo.save(profile1);
    profile1.setUser(user1);
    user1.setUserProfile(profile1);




  User user2 = new User();
  user2.setEmail("as@as.net");
  user2.setPassword(passwordEncoder.encode("as"));
  user2.setActive(true);
  user2.setProvider(AuthProvider.local);
  userRepo.save(user2);
  user2.addRoles(user);
  Profile profile2 = new Profile();

  user2.setUserProfile(profile2);
  profileRepo.save(profile2);
  profile1.setUser(user2);
//////////

Category good = categoryRepo.save(new Category("good"));
Category question = categoryRepo.save(new Category("question"));
Category learn = categoryRepo.save(new Category("learn"));



Tag tag = tagRepo.save(new Tag("motivation"));
Tag tag2 = tagRepo.save(new Tag("addiction"));
Tag tag3 = tagRepo.save(new Tag("success"));


Set<Tag> tags1 = new HashSet<>();
tags1.add(tag);tags1.add(tag2);

Set<Tag> tags2 = new HashSet<>();
tags2.add(tag);tags2.add(tag3);

Set<Tag> tags3 = new HashSet<>();
tags3.add(tag);


Post post1 = new Post();Post post2 = new Post();Post post3 = new Post();Post post4 = new Post();

post1.setAnonymous(true);
post1.setTitle("how did i lose 10kg");
post1.setContent("This is my way to lose 10 kg");

postRepo.save(post1);;
good.addPostToCategory(post1);
post1.setUser(user2);
post1.setCategory(good);
post1.setListTags(tags3);


post4.setTitle("how did i lose 100kg");
     post4.setContent("This is my way to lose 100 kg in a year");

    postRepo.save(post4);
    good.addPostToCategory(post4);
    post4.setUser(user2);
    post4.setCategory(good);
    post4.setListTags(tags2);


post2.setTitle("learn from my mistake");
post2.setContent("This is my way to lose 1 kg in a year");

    postRepo.save(post2);
    learn.addPostToCategory(post2);
    post2.setUser(user2);
    post2.setCategory(learn);
    post2.setListTags(tags2);


post3.setTitle("how to lose weight in a week?");
post3.setContent("This is my way to lose 1000 kg in a year");


    postRepo.save(post3);
    question.addPostToCategory(post3);

      post3.setCategory(question);
      post3.setUser(user2);
      post3.setListTags(tags1);













//
//    User user2 = new User();
//    user2.setEmail("sa@as.com");
//    user2.setActive(true);
//    user2.setPassword(passwordEncoder.encode("sa"));
//    user2.addRoles(admin);
//    user2.setProvider(AuthProvider.local);
//
//userRepo.save(user2);
//
//
//
//User user3 = new User();
//user3.setEmail("as@as.net");
//user3.setPassword(passwordEncoder.encode("as"));
//user3.setProvider(AuthProvider.local);
//Profile profile3 = new Profile();
//profile1.setBirthDate(LocalDate.of(1995, 12, 21));
//profile1.setCity("annaba");
//profile1.setFirstName("khaled");
//profile1.setLastName("sakhai");
//profile1.setSummary(
//  "Welcome, Hi to my profile page, hope this works one day"
//);
//profile3.setProfileCountry("Algeria");
//profile3.setUser(user3);
//user3.setUserProfile(profile3);
//
//userRepo.save(user3);



    // User user3 = new User();
    // user3.setEmail("aa");
    // user3.setPassword(passwordEncoder.encode("aa"));  
    // user3.setActive(true);
    // user3.addRoles(roleRepo.findById(1L).get(),roleRepo.findById( 2L).get());

    // user3.setProvider(AuthProvider.local);
    // userRepo.save(user3);

   
    // profile1.setUser(userRepo.findById(1L).get());
    // profileRepo.save(profile1);
    // user1.setUserProfile(profileRepo.findById(1L).get());
   
   //

//     Profile profile2 = new Profile();
//     profile2.setBirthDate(LocalDate.of(1995, 12, 21));
//     profile2.setCity("annaba");
//     profile2.setFirstName("khal");
//     profile2.setLastName("sak");
//     profile2.setSummary(
//       "Welcome, Hi to my profile pagr"
//     );

//     profile2.setUser(user2);
//     user2.setUserProfile(profile2);
//     profileRepo.save(profile2);
//     userRepo.save(user2);


//     Profile profile3 = new Profile();
//     profile3.setBirthDate(LocalDate.of(2000, 12, 21));
//     profile3.setCity("annaba");
//     profile3.setFirstName("ahmed");
//     profile3.setLastName("meftah");
//     profile3.setSummary(
//       "Welcome, Hi to my profile page, hope this works one day"
//     );
//     profile3.setUser(user3);
//     user3.setUserProfile(profile3);
//     profileRepo.save(profile3);
//     userRepo.save(user3);


//     Post post = new Post();
//     post.setContent("This is how i made 50$ a day using spring bott");
//     post.setTitle("Test post only");
//     post.setTag(Tag.CAREER);
//     postRepo.save(post);


//     Image img = new Image();
    
  }
}
