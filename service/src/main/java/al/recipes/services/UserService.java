package al.recipes.services;

import al.recipes.models.Users;
import al.recipes.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    public Users getUserById(long userId) {
        return userRepo.getUserById(userId);
    }

    public List<Users> getAllUsers() {
        return userRepo.getAllUsers();
    }

    public synchronized boolean addUser(Users users) {
        userRepo.addUser(users);
        return true;
    }

    public Users findUser(String username) {
        return userRepo.getUser(username);
    }

    public void updateUser(Users users) {
        userRepo.updateUser(users);
    }

    public void deleteUser(long userId) {
        userRepo.deleteUser(userId);
    }
} 
