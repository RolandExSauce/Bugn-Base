package com.bugnbass.backend.repository;

import com.bugnbass.backend.model.Order;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.model.enums.OrderStatus;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /**
     * Checks whether an order exists for a given user that contains a specific product
     * and has one of the provided order statuses.
     *
     * @param user the User entity
     * @param statuses the collection of order statuses to filter by
     * @param productId the product identifier
     * @return true if such an order exists, otherwise false
     */
    boolean existsByUserAndOrderStatusInAndOrderItemsProductId(
            User user,
            Collection<OrderStatus> statuses,
            Long productId
    );

    /**
     * Retrieves all orders for a given user including their order items and associated products.
     * Uses fetch joins to avoid the N+1 select problem.
     *
     * @param user the User entity
     * @return a list of orders with items and products loaded
     */
    @Query(
        """
            select distinct o from Order o
            left join fetch o.user u
            left join fetch o.orderItems oi
            left join fetch oi.product
            where o.user = :user
        """)
    List<Order> findByUserWithItemsAndProducts(@Param("user") User user);

    /**
     * Retrieves a single order by its ID including its order items and associated products.
     * Uses fetch joins to eagerly load related entities.
     *
     * @param id the order identifier
     * @return an Optional containing the order if found
     */
    @Query(
        """
            select o from Order o
            left join fetch o.user u
            left join fetch o.orderItems oi
            left join fetch oi.product
            where o.id = :id
        """)
    Optional<Order> findByIdWithItemsAndProducts(@Param("id") Long id);

    /**
     * Retrieves all orders including their user, order items and associated products.
     * Uses fetch joins to avoid the N+1 select problem.
     *
     * @return a list of orders with user, items and products loaded
     */
    @Query(
        """
            select distinct o from Order o
            left join fetch o.user u
            left join fetch o.orderItems oi
            left join fetch oi.product
        """)
    List<Order> findAllWithUserItemsAndProducts();

}
