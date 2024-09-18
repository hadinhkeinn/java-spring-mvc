package vn.hoidanit.laptopshop.controller.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ItemController {
    private final ProductService productService;
    private final UserService userService;

    public ItemController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/products")
    public String getProductsPage(Model model,
            @RequestParam("page") Optional<String> pageOptional,
            @RequestParam("name") Optional<String> nameOptional,
            @RequestParam("factory") Optional<String> factoryOptional,
            @RequestParam("target") Optional<String> targetOptional,
            @RequestParam("price") Optional<String> priceOptional,
            @RequestParam("sort") Optional<String> sortOptional) {
        int page = 1;
        try {
            if (pageOptional.isPresent())
                page = Integer.parseInt(pageOptional.get());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Pageable pageable = PageRequest.of(page - 1, 60);
        String name = nameOptional.isPresent() ? nameOptional.get() : "";

        // case 1
        // double minPrice = minPriceOptional.isPresent() ?
        // Double.parseDouble(minPriceOptional.get()) : 0;

        // case 2
        // double maxPrice = maxPriceOptional.isPresent() ?
        // Double.parseDouble(maxPriceOptional.get()) : 0;

        // case 3
        // String factory = factoryOptional.isPresent() ? factoryOptional.get() : "";

        // case 4
        // List<String> factoryList = Arrays.asList(factoryOptional.get().split(","));

        // case 5
        // String price = priceOptional.isPresent() ? priceOptional.get() : "";

        // case 6
        // List<String> price = Arrays.asList(priceOptional.get().split(","));

        Page<Product> list = this.productService.getAllProducts(pageable, name);
        model.addAttribute("listProducts", list.getContent());
        model.addAttribute("currPage", page);
        model.addAttribute("totalPages", list.getTotalPages());
        return "client/product/show";
    }

    @GetMapping("/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id) {
        Product prd = this.productService.getProduct(id);
        model.addAttribute("product", prd);
        return "client/product/detail";
    }

    @GetMapping("/cart")
    public String getUserCart(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        User user = this.userService.getUserById(id);
        Cart cart = this.productService.getUserCart(user) != null ? this.productService.getUserCart(user) : new Cart();
        List<CartDetail> list = cart.getCartDetails();
        model.addAttribute("cartDetails", list);
        model.addAttribute("cart", cart);
        return "client/cart/show";
    }

    @GetMapping("/checkout")
    public String getCheckoutPage(Model model, HttpServletRequest request) {
        User currentUser = new User();// null
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        currentUser.setId(id);

        Cart cart = this.productService.getUserCart(currentUser);

        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();

        double totalPrice = 0;
        for (CartDetail cd : cartDetails) {
            totalPrice += cd.getPrice() * cd.getQuantity();
        }

        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);

        return "client/cart/checkout";
    }

    @GetMapping("/thanks")
    public String getThanksPage() {
        return "client/cart/thanks";
    }

    // Add product to cart
    @PostMapping("/add-product-to-cart/{id}")
    public String handleAddProductToCart(@PathVariable long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String userEmail = (String) session.getAttribute("email");
        long prdId = id;
        this.productService.handleAddProductToCart(userEmail, prdId, session, 1);
        return "redirect:/";
    }

    // Delete cart item
    @PostMapping("/delete-cart-product/{id}")
    public String handleDeleteCartItem(@PathVariable long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        this.productService.deleteCartDetail(id, session);
        return "redirect:/cart";
    }

    // Confirm checkout
    @PostMapping("/confirm-checkout")
    public String getCheckoutPage(@ModelAttribute("cart") Cart cart) {
        List<CartDetail> list = (cart == null) ? new ArrayList<CartDetail>() : cart.getCartDetails();
        this.productService.handleUpdateCartBeforeCheckout(list);
        return "redirect:/checkout";
    }

    // Handle check out
    @PostMapping("/place-order")
    public String handlePlaceOrder(HttpServletRequest request, @RequestParam String receiverName,
            @RequestParam String receiverAddress, @RequestParam String receiverPhone) {
        User currentUser = new User();// null
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        currentUser.setId(id);

        this.productService.handlePlaceOrder(currentUser, session, receiverName, receiverAddress, receiverPhone);
        return "redirect:/thanks";
    }

    // Add product to cart from view product detail
    @PostMapping("/add-product-from-view-detail")
    public String addProductToCartFromViewDetail(@RequestParam long id, @RequestParam int quantity,
            HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        this.productService.handleAddProductToCart(email, id, session, quantity);
        return "redirect:/";
    }

}
