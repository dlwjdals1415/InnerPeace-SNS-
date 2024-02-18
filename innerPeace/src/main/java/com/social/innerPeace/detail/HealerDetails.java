package com.social.innerPeace.detail;

import com.social.innerPeace.entity.Healer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HealerDetails implements UserDetails {
    private final Healer healer;

    public HealerDetails(Healer healer) {
        this.healer = healer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(healer.getRole().value()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return healer.getHealerPw();
    }

    @Override
    public String getUsername() {
        return healer.getHealerEmail();
    }

    public String getHealer_nickname() {
        return healer.getHealerNickName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {return healer.isHealerStatus();}

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return healer.isEmailVerified();
    }
}
