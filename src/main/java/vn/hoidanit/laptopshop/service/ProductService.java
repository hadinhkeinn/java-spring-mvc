package vn.hoidanit.laptopshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderDetail;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.CartDetailRepository;
import vn.hoidanit.laptopshop.repository.CartRepository;
import vn.hoidanit.laptopshop.repository.OrderDetailRepository;
import vn.hoidanit.laptopshop.repository.OrderRepository;
import vn.hoidanit.laptopshop.repository.ProductRepository;
import vn.hoidanit.laptopshop.service.specification.ProductSpecs;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public ProductService(ProductRepository productRepository, CartRepository cartRepository,
            CartDetailRepository cartDetailRepository, UserService userService, OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public void handleSaveProduct(Product product) {
        this.productRepository.save(product);
    }

    public Page<Product> getAllProducts(Pageable pageable, String name) {
        return this.productRepository.findAll(ProductSpecs.nameLike(name), pageable);
    }

    // case 1
    // public Page<Product> getAllProductsWithSpec(Pageable pageable, double
    // minPrice) {
    // return this.productRepository.findAll(ProductSpecs.minPrice(minPrice),
    // pageable);
    // }

    // case 2
    // public Page<Product> getAllProductsWithSpec(Pageable pageable, double
    // maxPrice) {
    // return this.productRepository.findAll(ProductSpecs.maxPrice(maxPrice),
    // pageable);
    // }

    // case 3
    // public Page<Product> getAllProductsWithSpec(Pageable pageable, String
    // factory) {
    // return this.productRepository.findAll(ProductSpecs.factoryEqual(factory),
    // pageable);
    // }

    // case 4
    // public Page<Product> getAllProductsWithSpec(Pageable pageable, List<String>
    // factory) {
    // return this.productRepository.findAll(ProductSpecs.factoryIn(factory),
    // pageable);
    // }

    // case 5
    // public Page<Product> getAllProductsWithSpec(Pageable pageable, String price)
    // {
    // double min = 0, max = 0;
    // if (price.equals("duoi-10-trieu")) {
    // max = 10000000;
    // } else if (price.equals("10-15-trieu")) {
    // min = 10000000;
    // max = 15000000;
    // } else if (price.equals("15-20-trieu")) {
    // min = 15000000;
    // max = 20000000;
    // } else if (price.equals("tren-20-trieu")) {
    // min = 20000000;
    // max = Double.MAX_VALUE;
    // } else {
    // return this.getAllProducts(pageable);
    // }
    // return this.productRepository.findAll(ProductSpecs.matchPrice(min, max),
    // pageable);
    // }

    // case 6
    public Page<Product> getAllProductsWithSpec(Pageable pageable, List<String> price) {
        Specification<Product> combinedSpec = (root, query, criteriaBuilder) -> criteriaBuilder.disjunction();

        int count = 0;
        for (String p : price) {
            double min = 0, max = 0;

            switch (p) {
                case "duoi-10-trieu":
                    min = 0;
                    max = 10000000;
                    count++;
                    break;
                case "10-15-trieu":
                    min = 10000000;
                    max = 15000000;
                    count++;
                    break;
                case "15-20-trieu":
                    min = 15000000;
                    max = 20000000;
                    break;
                case "tren-20-trieu":
                    min = 20000000;
                    max = Double.MAX_VALUE;
                    count++;
                    break;
                default:
                    break;
            }

            if (min != 0 && max != 0) {
                Specification<Product> rangeSpec = ProductSpecs.matchMutiplePrice(min, max);
                combinedSpec = combinedSpec.or(rangeSpec);
            }
        }

        if (count == 0) {
            return this.productRepository.findAll(pageable);
        }

        return this.productRepository.findAll(combinedSpec, pageable);
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return this.productRepository.findAll(pageable);
    }

    public Product getProduct(long id) {
        return this.productRepository.findById(id);
    }

    public void deleteProduct(long id) {
        this.productRepository.deleteById(id);
    }

    public void handleAddProductToCart(String email, long prd_Id, HttpSession session, int quantity) {
        User user = this.userService.getUserByEmail(email);

        if (user != null) {
            Cart cart = this.cartRepository.findByUser(user);
            if (cart == null) {
                Cart newCart = new Cart();
                newCart.setUser(user);
                newCart.setSum(0);
                cart = this.cartRepository.save(newCart);
            }

            // Tìm prd by id
            Product p = this.getProduct(prd_Id);

            if (p != null) {
                // Check sản phẩm đã được thêm vào giỏ hàng hay chưa
                CartDetail detail = this.cartDetailRepository.findByCartAndProduct(cart, p);

                if (detail != null) {
                    detail.setQuantity(detail.getQuantity() + quantity);
                } else {
                    detail = new CartDetail();
                    // save cart_detail new product
                    detail.setCart(cart);
                    detail.setProduct(p);
                    detail.setQuantity(quantity);
                    detail.setPrice(p.getPrice());
                    // update sum(cart)
                    long s = cart.getSum() + 1;
                    cart.setSum(s);
                    this.cartRepository.save(cart);
                    session.setAttribute("sum", s);
                }
                // save
                this.cartDetailRepository.save(detail);
            }

        }
    }

    public Cart getUserCart(User user) {
        return this.cartRepository.findByUser(user);
    }

    public void deleteCartDetail(long id, HttpSession session) {
        Optional<CartDetail> cartDetailOptional = this.cartDetailRepository.findById(id);
        if (cartDetailOptional.isPresent()) {
            CartDetail cartDetail = cartDetailOptional.get();
            Cart currentCart = cartDetail.getCart();
            this.cartDetailRepository.deleteById(id);
            long sum = currentCart.getSum();
            if (sum > 1) {
                currentCart.setSum(sum - 1);
                session.setAttribute("sum", currentCart.getSum());
                this.cartRepository.save(currentCart);
            } else {
                this.cartRepository.delete(currentCart);
                session.setAttribute("sum", 0);
            }
        }
    }

    public void handleUpdateCartBeforeCheckout(List<CartDetail> cartDetails) {
        for (CartDetail cartDetail : cartDetails) {
            Optional<CartDetail> cdOptional = this.cartDetailRepository.findById(cartDetail.getId());
            if (cdOptional.isPresent()) {
                CartDetail currentCartDetail = cdOptional.get();
                currentCartDetail.setQuantity(cartDetail.getQuantity());
                this.cartDetailRepository.save(currentCartDetail);
            }
        }
    }

    public void handlePlaceOrder(User user, HttpSession session, String receiverName, String receiverAddress,
            String receiverPhone) {
        // save order
        Order order = new Order();
        order.setUser(user);
        order.setReceiverName(receiverName);
        order.setReceiverAddress(receiverAddress);
        order.setReceiverPhone(receiverPhone);
        order.setStatus("PENDING");
        order = this.orderRepository.save(order);

        // save order detail
        Cart cart = this.cartRepository.findByUser(user);
        if (cart != null) {
            List<CartDetail> cartDetails = cart.getCartDetails();

            double totalPrice = 0;
            for (CartDetail cartDetail : cartDetails) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProduct(cartDetail.getProduct());
                orderDetail.setPrice(cartDetail.getPrice());
                orderDetail.setQuantity(cartDetail.getQuantity());
                double total = orderDetail.getPrice() * orderDetail.getQuantity();
                totalPrice += total;
                this.orderDetailRepository.save(orderDetail);
            }

            // update total price
            order.setTotalPrice(totalPrice);
            this.orderRepository.save(order);

            // delete cart detail and cart
            for (CartDetail cartDetail : cartDetails) {
                this.cartDetailRepository.delete(cartDetail);
            }

            this.cartRepository.delete(cart);

            // set session
            session.setAttribute("sum", 0);
        }
    }
}
