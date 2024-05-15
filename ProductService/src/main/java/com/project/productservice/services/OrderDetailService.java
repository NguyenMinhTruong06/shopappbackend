package com.project.productservice.services;

import com.project.productservice.dtos.OrderDetailDTO;
import com.project.productservice.excepions.DataNotFoundException;
import com.project.productservice.models.Order;
import com.project.productservice.models.OrderDetail;
import com.project.productservice.models.Product;
import com.project.productservice.repositories.OrderDetailRepository;
import com.project.productservice.repositories.OrderRepository;
import com.project.productservice.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class OrderDetailService implements IOrderDetailService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(()->new DataNotFoundException("cannot find order with id: "+orderDetailDTO.getOrderId()));
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(()->new DataNotFoundException("Cannot find product with id"+orderDetailDTO.getProductId()));
         OrderDetail orderDetail = OrderDetail.builder()
                 .order(order)
                 .product(product)
                 .numberOfProducts(orderDetailDTO.getNumberOfProduct())
                 .price(orderDetailDTO.getPrice())
                 .totalMoney(orderDetailDTO.getTotalMoney())
                 .color(orderDetailDTO.getColor())
                 .build();
         return orderDetailRepository.save(orderDetail);

    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {

        return orderDetailRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("cannot find orderdetail with id"+id));
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("cannot find order detail with id"+id));
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(()-> new DataNotFoundException("cannot find order with id"+ id));
        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(()->new DataNotFoundException("Cannot find product with id"+orderDetailDTO.getProductId()));
        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProduct());
        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);

        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public void deleteById(Long id) {
        orderDetailRepository.deleteById(id);

    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
