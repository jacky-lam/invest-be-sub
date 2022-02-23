package com.investment.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;

public class JwtUser extends User{
    public JwtUser(String username, Collection<? extends GrantedAuthority> authorities){
        super(username, "trollface", authorities);
    }
}