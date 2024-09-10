package vn.hoidanit.laptopshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderDetail;
import vn.hoidanit.laptopshop.service.OrderService;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/admin/order")
    public String getOrderPage(Model model) {
        List<Order> list = this.orderService.getAllOrders();
        model.addAttribute("listOrders", list);
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
