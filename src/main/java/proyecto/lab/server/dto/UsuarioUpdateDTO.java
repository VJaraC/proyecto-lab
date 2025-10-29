package proyecto.lab.server.dto;

import proyecto.lab.server.models.Rol;

import java.io.Serializable;

public record UsuarioUpdateDTO(
    int id,
    String nombres,
    String apellidos,
    String estado,
    String email,
    String telefono,
    Rol rol,
    String contrasena,
    String cargo
)
{}
