package com.bugnbass.backend.model._interface;
import com.bugnbass.backend.model.enums.UserRole;
import org.springframework.security.core.userdetails.UserDetails;


public interface IBaseUser extends UserDetails {
    String getEmail();
    UserRole getRole();
};
