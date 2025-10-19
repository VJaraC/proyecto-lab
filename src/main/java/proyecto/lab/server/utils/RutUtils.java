package proyecto.lab.server.utils;

public final class RutUtils {
    private RutUtils() {}
    /**
     * Normaliza y valida el RUT a un único estándar: XX.XXX.XXX-DV (DV en mayúscula).
     * Debe incluir guion obligatoriamente.
     * Ejemplos válidos: 12.345.678-9, 12345678-5, 8.675.309-K
     * Ejemplos inválidos: 123456789, 12345678K, 12345678 9
     */
    public static String normalizarRut(String rutRaw) {
        if (rutRaw == null || rutRaw.isEmpty()) {
            throw new IllegalArgumentException("El RUT es obligatorio");
        }

        // 🔸 Validar que contenga guion
        if (!rutRaw.contains("-")) {
            throw new IllegalArgumentException("El RUT debe incluir guion antes del dígito verificador");
        }

        // 🔸 Eliminar puntos, convertir a mayúsculas
        String s = rutRaw.replace(".", "").toUpperCase().trim();

        // 🔸 Separar cuerpo y DV usando el guion
        String[] partes = s.split("-");
        if (partes.length != 2) {
            throw new IllegalArgumentException("Formato de RUT incorrecto (debe tener solo un guion)");
        }

        String cuerpo = partes[0];
        String dvStr = partes[1];

        if (!cuerpo.matches("\\d+")) {
            throw new IllegalArgumentException("El cuerpo del RUT debe ser numérico");
        }
        if (!dvStr.matches("[0-9K]")) {
            throw new IllegalArgumentException("El dígito verificador del RUT debe ser numérico o 'K'");
        }

        char dv = dvStr.charAt(0);

        // 🔸 Validar dígito verificador real
        if (dv != calcularDV(cuerpo)) {
            throw new IllegalArgumentException("El RUT ingresado no es válido");
        }

        // 🔸 Formatear estándar (12.345.678-K)
        return formatearCuerpoConDV(cuerpo, dv);
    }

    private static char calcularDV(String cuerpo) {
        int suma = 0, mul = 2;
        for (int i = cuerpo.length() - 1; i >= 0; i--) {
            suma += (cuerpo.charAt(i) - '0') * mul;
            mul = (mul == 7) ? 2 : (mul + 1);
        }
        int resto = 11 - (suma % 11);
        if (resto == 11) return '0';
        if (resto == 10) return 'K';
        return (char) ('0' + resto);
    }

    private static String formatearCuerpoConDV(String cuerpo, char dv) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = cuerpo.length() - 1; i >= 0; i--) {
            sb.insert(0, cuerpo.charAt(i));
            count++;
            if (count == 3 && i > 0) {
                sb.insert(0, '.');
                count = 0;
            }
        }
        sb.append('-').append(dv);
        return sb.toString();
    }
}
