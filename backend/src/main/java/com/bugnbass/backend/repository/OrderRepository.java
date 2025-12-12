package com.bugnbass.backend.repository;

import com.bugnbass.backend.model.Order;
import com.bugnbass.backend.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing Order entities. Provides CRUD operations and methods to retrieve orders
 * by user.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

  /**
   * Finds all orders associated with a specific user.
   *
   * @param user the user
   * @return a list of orders for the given user
   */
  List<Order> findByUser(User user);
}
