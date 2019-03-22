package org.chirper.domain.entities;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class UserRole extends BaseEntity implements GrantedAuthority {

    private String authority;

    public UserRole() {
    }

    @Override
    @Column(name = "authority", nullable = false, unique = true)
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}