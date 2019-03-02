package al.recipes.security;

import al.recipes.models.Users;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component(value = "userDetailService")
public class BlogUserDetailService implements UserDetailsService {
    
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            String urlConn = "http://" + InetAddress.getLocalHost().getHostName();
            RestTemplate restTemplate = new RestTemplate();
            Users user = restTemplate.getForObject(urlConn + "/api/users/" + username, Users.class, 200);
            return new User(String.valueOf(Objects.requireNonNull(user).getUsername()), user.getPassword(), getAuthority(user));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private List<SimpleGrantedAuthority> getAuthority(Users user) {
        if (user.getRole().getRole().equals("ADMIN")) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }
    
}