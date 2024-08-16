package vn.hoidanit.laptopshop.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;

@Controller
public class HomePageController {
    private final ProductService productService;

    public HomePageController(ProductService productService) {
        this.productService = productService;
    }

    // Get HomePage
    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Product> list = this.productService.getAllProducts();
        model.addAttribute("listProducts", list);
        return "client/homepage/show";
    }

}
