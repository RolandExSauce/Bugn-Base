package com.bugnbass.backend.repository;

import com.bugnbass.backend.model.Order;
import com.bugnbass.backend.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for CRUD operations on Order entities.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Retrieves all orders placed by a specific user.
     *
     * @param user the User entity
     * @return a list of Order entities associated with the user
     */
    List<Order> findByUser(User user);
}
