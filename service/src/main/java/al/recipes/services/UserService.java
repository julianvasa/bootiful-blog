package al.recipes.services;

import al.recipes.models.Roles;
import al.recipes.models.Users;
import al.recipes.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Users findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Users loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return user;
    }

    public void signup(String username, String password, String fullName) {
        Users u = new Users();
        u.setFullName(fullName);
        u.setPassword(bCryptPasswordEncoder.encode(password));
        u.setUsername(username);
        Roles r = new Roles();
        r.setRole("USER");
        u.setRole(r);
        userRepository.save(u);
    }
}
