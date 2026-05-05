package t.com.onlinebooking.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import t.com.onlinebooking.DHO.User;
import t.com.onlinebooking.Service.UserService;
import java.util.List;

@RestController
@RequestMapping("/User")
public class UserController{
    @Autowired
    private UserService userService;

    @GetMapping("/UserDetails")
    public List<User> getUser(){
        return userService.getUser();
    }

    @PostMapping("/AddUser")
    public String addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @PutMapping("/{userId}")
    public String updateUser(@PathVariable Long userId,@RequestBody User user){
        return userService.updateUser(userId,user);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Long userId){
        return userService.deleteUser(userId);
    }

    @GetMapping("/today-count")
    public long getTodayUsers(){
        return userService.getTodayUserCount();
    }
}