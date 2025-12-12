package com.bugnbass.backend.repository;

import com.bugnbass.backend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repository for managing OrderItem entities. Provides basic CRUD operations for order items. */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {}
