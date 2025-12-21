package com.bugnbass.backend.repository;

import com.bugnbass.backend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for CRUD operations on OrderItem entities.
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
