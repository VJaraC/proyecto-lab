package proyecto.lab.server.utils;

/**
 * Utilidad para manejo consistente del campo "estado" de los usuarios.
 * Define valores válidos y métodos para validación y normalización.
 */
public final class EstadoUtils {

    // 🔹 Estados válidos del sistema
    public static final String HABILITADO = "habilitado";
    public static final String DESHABILITADO = "deshabilitado";

    private EstadoUtils() {
        // Evita instanciación
    }

    /**
     * Normaliza un texto de estado: quita espacios y pasa a minúsculas.
     */
    public static String normalizar(String estado) {
        return estado == null ? null : estado.trim().toLowerCase();
    }

    /**
     * Verifica si el estado entregado es válido.
     */
    public static boolean esValido(String estado) {
        return HABILITADO.equals(estado) || DESHABILITADO.equals(estado);
    }
}
