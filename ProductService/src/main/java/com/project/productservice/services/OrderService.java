package com.project.productservice.services;

import com.project.productservice.dtos.OrderDTO;
import com.project.productservice.excepions.DataNotFoundException;
import com.project.productservice.models.Order;
import com.project.productservice.models.OrderStatus;
import com.project.productservice.repositories.OrderRepository;
import com.project.userservice.model.User;
import com.project.userservice.respositories.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        LocalDate shippingDate = orderDTO.getShippingDate()== null ? LocalDate.now(): orderDTO.getShippingDate();
        if (shippingDate== null||shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("date must be at least today !");
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

    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {

        Order order = orderRepository.findById(id).orElseThrow(()->new DataNotFoundException("cannot find order with id: "+id));
        User existingUser = userRepository.findById(orderDTO.getUserId()).orElseThrow(()-> new DataNotFoundException("cannot find user with id: "+id));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper ->mapper.skip(Order::setId));

        modelMapper.map(orderDTO,order);
        order.setUser(existingUser);
        return orderRepository.save(order);

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
    public List<Order> findByUserId(Long userId) {

        return orderRepository.findByUserId(userId);
    }
}
