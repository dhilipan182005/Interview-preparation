package t.com.onlinebooking.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import t.com.onlinebooking.DHO.User;
import t.com.onlinebooking.Repository.UserRepo;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserService{
    @Autowired
    private UserRepo userRepo;

    public List<User> getUser(){
        return userRepo.findAll();
    }

    public String addUser(User user){
        user.setCreatedDate(LocalDate.now());
        if(user.getCity()!=null && user.getCity().toLowerCase().contains("trichy")) {
            user.setDiscount(20.0);
        }
        else{
            user.setDiscount(0.0);
        }
        userRepo.save(user);
        return "User added successfully";
    }

    public String updateUser(Long userId,User user){
        User existingUser=userRepo.findById(userId).orElse(null);
        if(existingUser!=null){
            existingUser.setUserName(user.getUserName());
            existingUser.setPhoneNo(user.getPhoneNo());
            existingUser.setAddress(user.getAddress());
            existingUser.setCity(user.getCity());
            existingUser.setDiscount(user.getDiscount());
            userRepo.save(existingUser);
            return "User updated successfully";
        }
        return "User not found";
    }

    public String deleteUser(Long userId){
        if(userRepo.existsById(userId)){
            userRepo.deleteById(userId);
            return "User deleted successfully";
        }
        return "User not found";
    }

    public long getTodayUserCount(){
        return userRepo.countByCreatedDate(LocalDate.now());
    }
}