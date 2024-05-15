package com.project.productservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



public class OrderStatus {

    public static final String PENDING = "pending";
    public static final String PROCESSING = "processing";
    public static final String SHIPPED = "shipped";
    public static final String DELIVERED = "delivered";
    public static final String CANCELLED = "cancelled";







}
