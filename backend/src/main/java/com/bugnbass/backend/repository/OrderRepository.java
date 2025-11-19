package com.bugnbass.backend.repository;
import com.bugnbass.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> { };


