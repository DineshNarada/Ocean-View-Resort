package com.orrs.config;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * OpenAPI/Swagger Configuration for the Ocean View Resort REST API
 * Provides comprehensive API documentation
 */
@OpenAPIDefinition(
    info = @Info(
        title = "Ocean View Resort REST API",
        version = "1.0.0",
        description = "REST API endpoints for the Ocean View Resort reservation system. " +
                     "Supports authentication, reservation management, billing, and reporting.",
        contact = @Contact(
            name = "Ocean View Resort",
            email = "support@oceanviewresort.com"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0.html"
        )
    ),
    servers = {
        @Server(
            url = "/OceanViewResort/resources",
            description = "Production Server"
        )
    },
    tags = {
        @Tag(
            name = "Authentication",
            description = "User login and logout endpoints"
        ),
        @Tag(
            name = "Reservations",
            description = "Reservation management (CRUD operations)"
        ),
        @Tag(
            name = "Bills",
            description = "Billing and payment endpoints"
        ),
        @Tag(
            name = "Reports",
            description = "Occupancy, revenue, and guest report generation"
        )
    }
)
public class OpenAPIConfiguration {
    // This class is used only for OpenAPI annotations
}
