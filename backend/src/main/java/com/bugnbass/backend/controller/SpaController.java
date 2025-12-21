package com.bugnbass.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsible for forwarding all non-API, non-Swagger requests
 * to the React Single Page Application (SPA) entry point.
 *
 * <p>This enables client-side routing in the React application when it is
 * served by Spring Boot. Without this controller, direct navigation or
 * page refreshes on routes like {@code /login} or {@code /shop/123} would
 * result in a 404 error because Spring MVC has no matching controller.</p>
 *
 * <p>The mapping explicitly excludes backend API endpoints and Swagger
 * resources so that they continue to be handled by Spring MVC:</p>
 *
 * <ul>
 *   <li>{@code /bugnbass/api/**} – REST API endpoints</li>
 *   <li>{@code /swagger-ui/**} – Swagger UI</li>
 *   <li>{@code /v3/**} – OpenAPI specification endpoints</li>
 * </ul>
 *
 * <p>All other paths are forwarded to {@code /index.html}, which is served
 * from {@code classpath:/static/index.html} and bootstraps the React SPA.</p>
 */
@Controller
public class SpaController {

    /**
     * Forward all non-API, non-Swagger, non-static requests to index.html.
     */
    @GetMapping(value = {"/", "/{path:[^\\.]*}"})
    public String forward() {
        return "forward:/index.html";
    }
}
