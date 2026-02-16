package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.AdminUpdateUserDto;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.service.AdminService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for administrative user management.
 *
 * <p>This controller provides endpoints for administrators to:
 * <ul>
 *     <li>Retrieve all users</li>
 *     <li>Retrieve a user by ID</li>
 *     <li>Update user information and roles</li>
 *     <li>Deactivate (soft delete) user accounts</li>
 * </ul>
 *
 * <p>All endpoints require the {@code ADMIN} role.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/bugnbass/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    /**
     * Service responsible for administrative operations.
     */
    private final AdminService adminService;

    /**
     * Retrieves all users in the system.
     *
     * @return list of {@link User} entities
     */
    @GetMapping
    public List<User> getUsers() {
        return adminService.getUsers();
    }

    /**
     * Retrieves a specific user by identifier.
     *
     * @param id the user identifier as string (UUID)
     * @return the {@link User} entity
     */
    @GetMapping("/{id}")
    public User getUserById(
            @PathVariable("id") String id
    ) {
        return adminService.getUserById(id);
    }

    /**
     * Updates user profile and administrative properties.
     *
     * @param id  the user identifier as string (UUID)
     * @param dto the update data transfer object
     * @return the updated {@link User}
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable("id") String id,
            @Valid @RequestBody AdminUpdateUserDto dto
    ) {
        User updated = adminService.updateUser(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deactivates (soft deletes) a user account.
     *
     * <p>The user remains in the database but is marked as inactive.
     *
     * @param id the user identifier as string (UUID)
     * @return HTTP 204 No Content if successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("id") String id
    ) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
