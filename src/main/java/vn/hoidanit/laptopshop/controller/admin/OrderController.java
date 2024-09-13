package vn.hoidanit.laptopshop.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderDetail;
import vn.hoidanit.laptopshop.service.OrderService;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/admin/order")
    public String getOrderPage(Model model, @RequestParam("page") Optional<String> pageOptional) {
        int page = 1;
        try {
            if (pageOptional.isPresent())
                page = Integer.parseInt(pageOptional.get());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Pageable pageable = PageRequest.of(page - 1, 5);
        Page<Order> list = this.orderService.getAllOrders(pageable);
        model.addAttribute("listOrders", list.getContent());
        model.addAttribute("currPage", page);
        model.addAttribute("totalPages", list.getTotalPages());
        return "admin/order/show";
    }

    @GetMapping("/admin/order/{id}")
    public String getOrderDetailPage(Model model, @PathVariable long id) {
        Order order = this.orderService.getOrder(id);
        List<OrderDetail> detail = order.getOrderDetails();
        model.addAttribute("id", id);
        model.addAttribute("listDetails", detail);
        return "admin/order/detail";
    }

    @GetMapping("/admin/order/update/{id}")
    public String getUpdateOrderPage(Model model, @PathVariable long id) {
        Order order = this.orderService.getOrder(id);
        model.addAttribute("newOrder", order);
        return "admin/order/update";
    }

    @GetMapping("/admin/order/delete/{id}")
    public String getDeleteOrderPage(Model model, @PathVariable long id) {
        Order order = new Order();
        order.setId(id);
        model.addAttribute("order", order);
        model.addAttribute("id", id);
        return "admin/order/delete";
    }

    // handle delete order
    @PostMapping("/admin/order/delete")
    public String handleDeleteOrder(@ModelAttribute Order order) {
        this.orderService.deleteOrderByID(order.getId());
        return "redirect:/admin/order";
    }

    // handle delete order
    @PostMapping("/admin/order/update")
    public String handleUpdateOrder(@ModelAttribute("newOrder") Order order) {
        this.orderService.updateOrder(order);
        return "redirect:/admin/order";
    }

}
