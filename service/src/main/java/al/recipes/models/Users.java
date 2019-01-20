package al.recipes.models;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString
@ApiModel(description = "Users Data Model")

public class Users implements UserDetails, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 30, unique = true, name = "username")
    private String username;
    @Column(length = 60, name = "password")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;
    @Column(length = 100, name = "fullname")
    @NotEmpty(message = "*Please provide your full name")
    private String fullName;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role")
    private Roles role;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
}
