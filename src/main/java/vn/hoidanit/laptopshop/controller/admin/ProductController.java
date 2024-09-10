package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UploadService;

@Controller
public class ProductController {

    private final UploadService uploadService;
    private final ProductService productService;

    public ProductController(UploadService uploadService, ProductService productService) {
        this.uploadService = uploadService;
        this.productService = productService;
    }

    // List product Page
    @GetMapping("/admin/product")
    public String getProductPage(Model model) {
        List<Product> listProducts = this.productService.getAllProducts();
        model.addAttribute("listProducts", listProducts);

        return "admin/product/show";
    }

    // Detail prd page
    @GetMapping("/admin/product/{id}")
    public String getDetailProductPage(Model model, @PathVariable long id) {
        Product product = this.productService.getProduct(id);
        model.addAttribute("product", product);
        return "admin/product/detail";
    }

    // Create prd page
    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    // Update prd page
    @GetMapping("/admin/product/update/{id}")
    public String getUpdateProductPage(Model model, @PathVariable long id) {
        Product product = this.productService.getProduct(id);
        model.addAttribute("updatedProduct", product);
        return "admin/product/update";
    }

    // Delete prd page
    @GetMapping("/admin/product/delete/{id}")
    public String getDeletProductPage(Model model, @PathVariable long id) {
        Product prd = new Product();
        prd.setId(id);
        model.addAttribute("product", prd);
        model.addAttribute("id", id);
        return "admin/product/delete";
    }

    // handle create product
    @PostMapping("/admin/product/create")
    public String handleCreateProduct(Model model,
            @ModelAttribute("newProduct") @Valid Product product,
            BindingResult newProductBindingResult,
            @RequestParam("productImg") MultipartFile file) {

        // validate
        if (newProductBindingResult.hasErrors()) {
            return "admin/product/create";
        }

        String productImg = this.uploadService.handleSaveUploadFile(file, "product");
        product.setImage(productImg);
        this.productService.handleSaveProduct(product);
        return "redirect:/admin/product";
    }

    // handle update product
    @PostMapping("/admin/product/update")
    public String handleUpdateProduct(Model model, @ModelAttribute("updatedProduct") @Valid Product product,
            BindingResult updatedProductBindingResult,
            @RequestParam("productImg") MultipartFile file) {

        if (updatedProductBindingResult.hasErrors()) {
            return "/admin/product/update";
        }

        Product currentProduct = this.productService.getProduct(product.getId());
        String productImg = this.uploadService.handleSaveUploadFile(file, "product");

        if (currentProduct != null) {
            currentProduct.setName(product.getName());
            currentProduct.setPrice(product.getPrice());
            currentProduct.setDetailDesc(product.getDetailDesc());
            currentProduct.setShortDesc(product.getShortDesc());
            currentProduct.setQuantity(product.getQuantity());
            currentProduct.setFactory(product.getFactory());
            currentProduct.setTarget(product.getTarget());
            if (productImg != "") {
                currentProduct.setImage(productImg);
            }
        }
        this.productService.handleSaveProduct(currentProduct);
        return "redirect:/admin/product";
    }

    // handle delete product
    @PostMapping("/admin/product/delete")
    public String handleDeleteProduct(Model model, @ModelAttribute Product product) {
        this.productService.deleteProduct(product.getId());
        return "redirect:/admin/product";
    }
}
