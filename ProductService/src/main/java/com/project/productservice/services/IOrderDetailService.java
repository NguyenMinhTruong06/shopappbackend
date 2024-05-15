package com.project.productservice.services;

import com.project.productservice.dtos.OrderDetailDTO;
import com.project.productservice.excepions.DataNotFoundException;
import com.project.productservice.models.OrderDetail;


import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;
    OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetailData) throws DataNotFoundException;
    void deleteById(Long id);
    List<OrderDetail>findByOrderId (Long orderId);
}
