package ru.runner.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "t_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Size(min = 3, max = 256, message = "Не меньше 3 знаков")
    @Getter
    @Setter
    private String username;

    @Size(min = 5, max = 256, message = "Не меньше 5 знаков")
    @Getter
    @Setter
    private String password;

    @Transient
    @Getter
    @Setter
    private String passwordConfirm;

    @Getter
    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Getter
    @Setter
    @Column(name = "firstName")
    @Size(max = 256, message = "Не более 255 символов")
    private String firstName;

    @Getter
    @Setter
    @Column(name = "lastName")
    @Size(max = 256, message = "Не более 255 символов")
    private String lastName;

    @Getter
    @Setter
    @Column(name = "email")
    private String email;

    @Getter
    @Setter
    @Column(name = "memo")
    @Type(type = "text")
    private String memo;

    @Getter
    @Setter
    @Column(name = "short_memo")
    @Type(type = "text")
    private String shortMemo;

    @Getter
    @Setter
    @Column(name = "photo")
    @Type(type = "text")
    private String photo;

    @Getter
    @Setter
    @Column(name = "registeringDate")
    private Date registeringDate;

    public User() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
