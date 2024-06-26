package com.project.productservice.services;

import com.project.productservice.dtos.OrderDTO;
import com.project.productservice.excepions.DataNotFoundException;
import com.project.productservice.models.Order;
import com.project.productservice.models.OrderStatus;
import com.project.productservice.repositories.OrderRepository;
import com.project.userservice.exception.ValidationException;
import com.project.userservice.model.User;
import com.project.userservice.respositories.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService implements IOrderService{
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;


    @Override
    public Order createOrder(OrderDTO orderDTO) throws DataNotFoundException {
        User user = userRepository.findById(
                orderDTO.getUserId())
                .orElseThrow(()-> new DataNotFoundException( (" cannot find user with id"+ orderDTO.getUserId())));
        //convert orderDTO qua order, dùng thư viện model mapper
        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper ->mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderDTO, order);
//        order.setUser(user);
        order.setUserId(Math.toIntExact(user.getId()));
        Date orderDate = new Date();
        order.setOrderDate(orderDate);
//        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        String shippingMethod = orderDTO.getShippingMethod();
        LocalDate localOrderDate = orderDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate shippingDate =null;
        if(shippingMethod.equals("Hoả tốc")){
            shippingDate = localOrderDate;
        }
        if(shippingMethod.equals("Nhanh")){
            shippingDate = localOrderDate.plusDays(1);
        }
        if(shippingMethod.equals("Tiết kiệm")){
            shippingDate = localOrderDate.plusDays(3);
        }


        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);

        return order;
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }


    public List<Order> getAllOrder(){
        return orderRepository.findOrderByActiveTrue();
    }

    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {

        Order order = orderRepository.findById(id).orElseThrow(()->new DataNotFoundException("cannot find order with id: "+id));
        User existingUser = userRepository.findById(orderDTO.getUserId()).orElseThrow(()-> new DataNotFoundException("cannot find user with id: "+id));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper ->mapper.skip(Order::setId));

        modelMapper.map(orderDTO,order);
//        order.setUser(existingUser);
        order.setUserId(Math.toIntExact(existingUser.getId()));
        return orderRepository.save(order);

    }

    @Override
    public Order updateStatus(Long id, String newStatus) throws ValidationException {
        Order order = orderRepository.findById(id).orElseThrow(()->new ValidationException(HttpStatus.BAD_REQUEST,"cannot find order with id: "+id));
        if (!isValidStatus(newStatus)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Invalid status: " + newStatus);
        }
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
    private boolean isValidStatus(String status) {
        return OrderStatus.PENDING.equals(status) ||
                OrderStatus.PROCESSING.equals(status) ||
                OrderStatus.SHIPPED.equals(status) ||
                OrderStatus.DELIVERED.equals(status) ||
                OrderStatus.CANCELLED.equals(status);
    }

    @Override
    public void deleteOrder(Long id) throws DataNotFoundException {
        Order order = orderRepository.findById(id).orElse(null);
        if(order!=null){
            order.setActive(false);
            orderRepository.save(order);
        }

    }

    @Override
    public List<Order> findByUserIdAndActiveTrue(Long userId) {

        return orderRepository.findByUserIdAndActiveTrue(userId);
    }

}
