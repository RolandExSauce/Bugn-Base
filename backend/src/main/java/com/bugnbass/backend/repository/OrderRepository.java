package com.bugnbass.backend.repository;
import com.bugnbass.backend.model.Order;
import com.bugnbass.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);
};


