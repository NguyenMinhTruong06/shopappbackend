package com.project.productservice.controller;

import com.project.productservice.dtos.OrderDetailDTO;
import com.project.productservice.excepions.DataNotFoundException;
import com.project.productservice.models.OrderDetail;
import com.project.productservice.responses.OrderDetailResponse;
import com.project.productservice.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orderdetail")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
        @PostMapping("post")
        public ResponseEntity<?> createDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO){
            try{
                OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
                return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(newOrderDetail));
            } catch (Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }


        }
        @GetMapping("/{id}")
        public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) throws DataNotFoundException {
            OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
            return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(orderDetail));

        }
        @GetMapping("/order/{orderId}")
        public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId){
            List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
            List<OrderDetailResponse> orderDetailResponses= orderDetails
                    .stream()
                    .map(OrderDetailResponse::fromOrderDetail)
                    .toList();
            return ResponseEntity.ok(orderDetailResponses);
        }
        @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id") Long id,
                                               @RequestBody OrderDetailDTO orderDetailDTO){
            try {
                OrderDetail orderDetail = orderDetailService.updateOrderDetail(id,orderDetailDTO);
                return ResponseEntity.ok().body(orderDetail);
            } catch (DataNotFoundException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }

        @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id")Long id
        ){
            orderDetailService.deleteById(id);
            return  ResponseEntity.ok().body("delete order detail with id "+id);
            //return ResponseEntity.noContent().build();
        }

}
