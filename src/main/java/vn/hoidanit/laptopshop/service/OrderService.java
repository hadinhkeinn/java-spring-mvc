package vn.hoidanit.laptopshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.hoidanit.laptopshop.repository.OrderDetailRepository;
import vn.hoidanit.laptopshop.repository.OrderRepository;

import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderDetail;
import vn.hoidanit.laptopshop.domain.User;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return this.orderRepository.findAll(pageable);
    }

    public Order getOrder(long id) {
        return this.orderRepository.findById(id).get();
    }

    public void deleteOrderByID(long id) {
        Order order = this.getOrder(id);
        if (order != null) {
            List<OrderDetail> details = order.getOrderDetails();
            for (OrderDetail od : details) {
                this.orderDetailRepository.delete(od);
            }
        }
        this.orderRepository.delete(order);
    }

    public void updateOrder(Order order) {
        Order currentOrder = this.getOrder(order.getId());
        if (currentOrder != null) {
            currentOrder.setStatus(order.getStatus());
            this.orderRepository.save(currentOrder);
        }
    }

    public List<Order> getOrderByUser(User user) {
        return this.orderRepository.findByUser(user);
    }
}
