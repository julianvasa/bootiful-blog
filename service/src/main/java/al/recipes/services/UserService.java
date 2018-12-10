package al.recipes.services;

import al.recipes.models.Roles;
import al.recipes.models.Users;
import al.recipes.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    
    public Users findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
    
    public Users loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return user;
    }
    
    public void signup(String username, String password, String fullname) {
        Users u = new Users();
        u.setFullName(fullname);
        u.setPassword(bCryptPasswordEncoder.encode(password));
        u.setUsername(username);
        Roles r = new Roles();
        r.setRole("ROLE_USER");
        u.setRole(r);
        userRepo.save(u);
    }
} 
