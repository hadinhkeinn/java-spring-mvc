package vn.hoidanit.laptopshop.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.UserRepository;
import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
    }

    // Home page
    @RequestMapping("/")
    public String getHomePage(Model model) {
        List<User> users = userService.getAllUsersByEmail("congbao@gmail.com");
        System.out.println(users);
        String test = this.userService.handleHello();
        model.addAttribute("eric", test);
        model.addAttribute("dinkien", "From controller");

        return "hello";
    }

    // Create user page
    @RequestMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    // Table user page
    @RequestMapping("/admin/user")
    public String getTableUserPage(Model model) {
        List<User> users = this.userService.getAllUsers();
        model.addAttribute("listUsers", users);
        return "admin/user/table";
    }

    // Detail user page
    @RequestMapping(value = "/admin/user/{id}", method = RequestMethod.GET)
    public String getUserDetailPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/user/user-detail";

    }

    // Handle create user
    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    public String handleCreateUser(Model model, @ModelAttribute("newUser") User user) {
        this.userService.handleSaveUser(user);
        return "redirect:/admin/user";
    }
}
