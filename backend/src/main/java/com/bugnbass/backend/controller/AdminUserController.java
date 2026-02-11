package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.AdminUpdateUserDto;
import com.bugnbass.backend.dto.OrderDto;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.service.AdminService;
import com.bugnbass.backend.service.OrderService;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing orders in the admin context.
 *
 * <p>Provides endpoints for admins to retrieve, update, and delete orders.
 * All endpoints are secured and accessible only to users with ROLE_ADMIN authority.</p>
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/bugnbass/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminService adminService;

    @GetMapping
    public List<User> getUsers() {

        return adminService.getUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserById(
            @PathVariable("id") String id
    ) {
        return adminService.getUserById(id);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable("id") String id,
            @Valid @RequestBody AdminUpdateUserDto dto
    ) {
        User updated = adminService.updateUser(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("id") String id
    ) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


}
