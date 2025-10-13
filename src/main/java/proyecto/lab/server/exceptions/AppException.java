package proyecto.lab.server.common;

/**
 * Excepción personalizada para manejar errores de aplicación.
 * Sustituye SQLException o IllegalArgumentException en la capa de servicio/controlador.
 */
public class AppException extends RuntimeException {

    private final int status;

    public AppException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    // Métodos de fábrica para crear excepciones comunes
    public static AppException badRequest(String message) {
        return new AppException(400, message);
    }

    public static AppException notFound(String message) {
        return new AppException(404, message);
    }

    public static AppException conflict(String message) {
        return new AppException(409, message);
    }

    public static AppException internal(String message) {
        return new AppException(500, message);
    }
}
