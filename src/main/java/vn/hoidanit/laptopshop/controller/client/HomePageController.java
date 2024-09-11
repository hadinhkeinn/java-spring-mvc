package vn.hoidanit.laptopshop.controller.client;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.domain.dto.RegisterDTO;
import vn.hoidanit.laptopshop.service.OrderService;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomePageController {
    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;
    private final PasswordEncoder passwordEncoder;

    public HomePageController(ProductService productService, UserService userService, OrderService orderService,
            PasswordEncoder passwordEncoder) {
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
        this.passwordEncoder = passwordEncoder;
    }

    // Get register page
    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerUser", new RegisterDTO());
        return "client/auth/register";
    }

    // Get login page
    @GetMapping("/login")
    public String getLoginPage() {
        return "client/auth/login";
    }

    // Get HomePage
    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Product> list = this.productService.getAllProducts();
        model.addAttribute("listProducts", list);
        return "client/homepage/show";
    }

    // Get order history page
    @GetMapping("/order-history")
    public String getOrderHistoryPage(Model model, HttpServletRequest request) {
        User currentUser = new User();
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        currentUser.setId(id);

        List<Order> orders = this.orderService.getOrderByUser(currentUser);
        model.addAttribute("orders", orders);
        return "client/cart/order-history";
    }

    // Access deny Page
    @GetMapping("/access-deny")
    public String getAccessDenyPage() {
        return "client/auth/deny";
    }

    // Handle register User
    @PostMapping("/register")
    public String handleRegisterUser(@ModelAttribute("registerUser") @Valid RegisterDTO registerDTO,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/client/auth/register";
        }

        User user = this.userService.registerDTOtoUser(registerDTO);
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        user.setRole(this.userService.getRoleByName("USER"));
        this.userService.handleSaveUser(user);
        return "redirect:/login";
    }

}
