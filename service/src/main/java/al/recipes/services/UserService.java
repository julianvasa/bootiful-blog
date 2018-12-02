package al.recipes.services;

import al.recipes.models.Users;
import al.recipes.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;
    
    public Users findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
    
} 
