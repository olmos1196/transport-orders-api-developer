package com.transportes.api.driver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateDriverRequest(
        @NotBlank(message = "Nombre es obligatorio")
        @Size(max = 100, message = "Nombre no puede pasar de 100 caracteres")
        String name,

        @NotBlank(message = "Número de licencia es obligatorio")
        @Size(max = 30, message = "el número de licencia no puede pasar de 30 caracteres")
        String licenseNumber,

        // Si no se manda, el conductor se crea activo
        Boolean isActive) {
}
