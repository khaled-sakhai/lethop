package com.example.springsocial.specification;

import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.Role;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.APPRole;
import com.example.springsocial.enums.AuthProvider;
import com.example.springsocial.enums.Country;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileSpecification {


    public static Specification<Profile> byFirstName(String firstName ){

        return (root, query, criteriaBuilder) -> {
            if(firstName==null || firstName.isBlank()){
                return null;
            }
            return criteriaBuilder.equal(root.get("firstName"), firstName);

        };
    }
    public static Specification<Profile> byLastName(String lastName ){
        return (root, query, criteriaBuilder) -> {
            if(lastName==null || lastName.isBlank()){
                return null;
            }
            return criteriaBuilder.equal(root.get("lastName"), lastName);

        };
    }
    public static Specification<Profile> byCity(String city ){

        return (root, query, criteriaBuilder) -> {
            if(city==null || city.isBlank()){
                return null;
            }
            return criteriaBuilder.equal(root.get("city"), city);

        };
    }

    public static Specification<Profile> byBirthDate(Integer year, Integer month, Integer day) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (year != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.function("year", Integer.class, root.get("birthDate")), year));
            }
            if (month != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.function("month", Integer.class, root.get("birthDate")), month));
            }
            if (day != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.function("day", Integer.class, root.get("birthDate")), day));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

        public static Specification<Profile> byCountry(String country) {
            return (root, query, criteriaBuilder) -> {
                if (country == null || country.isEmpty()) {
                    return null;
                }
                try {
                    Country countryEnum = Country.valueOf(country.toUpperCase());
                    return criteriaBuilder.equal(root.get("country"),countryEnum);

                } catch (IllegalArgumentException e) {
                    return null;
                }
            };
        }

    public static Specification<Profile> byProfileId(Long id ){

        return (root, query, builder) -> {
            if(id==null){
                return null;
            }
            return builder.equal(root.get("id"),id);
        };
    }
    public static Specification<Profile> byUserId(Long id ){

        return (root, query, builder) -> {
            if(id==null){
                return null;
            }
            Join<Profile, User> userJoin = root.join("users");
            return builder.equal(userJoin.get("user"),id);
        };
    }

}
