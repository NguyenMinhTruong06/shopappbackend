package com.project.productservice.services;

import com.project.productservice.dtos.OrderDTO;
import com.project.productservice.excepions.DataNotFoundException;
import com.project.productservice.models.Order;


import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws DataNotFoundException;

    Order getOrder(Long id);

    Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;

    void deleteOrder(Long id) throws DataNotFoundException;

    List<Order> findByUserIdAndActiveTrue(Long userId);
}
