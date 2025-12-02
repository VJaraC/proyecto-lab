package proyecto.lab.agent.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Map;

public record AgentConfig(
        String agentKey,
        String ingestUrl,
        int sampleIntervalSeconds,
        int sendIntervalSeconds,
        Map<String, String> hostInfo
) {
    public static AgentConfig fromEnvOrDefaults() {
        String key = getenvOrDefault("AGENT_KEY", resolveHostname());
        String url = getenvOrDefault("INGEST_URL", "http://localhost:8080/metrics");
        int sample = Integer.parseInt(getenvOrDefault("SAMPLE_SEC", "15"));
        int send = Integer.parseInt(getenvOrDefault("SEND_SEC", "60"));
        Map<String, String> hostInfo = Map.of(
                "hostname", resolveHostname(),
                "os_name", System.getProperty("os.name"),
                "os_version", System.getProperty("os.version"),
                "os_arch", System.getProperty("os.arch"),
                "ip", resolveIp(),
                "mac", resolveMac(),
                "serial", getSerialNumber() // <--- Nueva función
        );

        return new AgentConfig(key, url, sample, send, hostInfo);
    }
    private static String getSerialNumber() {
            String os = System.getProperty("os.name").toLowerCase();

            // 1. Intento principal: PowerShell (Windows Moderno)
            if (os.contains("win")) {
                try {
                    // Comando PowerShell para obtener el serial
                    String[] cmd = {"powershell", "-Command", "Get-CimInstance -ClassName Win32_BIOS | Select-Object -ExpandProperty SerialNumber"};
                    Process p = Runtime.getRuntime().exec(cmd);

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                        String line = reader.readLine();
                        if (line != null && !line.isBlank()) {
                            return line.trim();
                        }
                    }
                } catch (Exception e) {
                    // Si falla PowerShell, intentamos silenciosamente con el siguiente método
                }

                // 2. Intento secundario: WMIC (Windows Antiguo) con ruta absoluta
                try {
                    // Usamos la ruta completa por si no está en el PATH
                    String[] cmd = {"C:\\Windows\\System32\\wbem\\wmic.exe", "bios", "get", "serialnumber"};
                    Process p = Runtime.getRuntime().exec(cmd);

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (!line.trim().isEmpty() && !line.toLowerCase().contains("serialnumber")) {
                                return line.trim();
                            }
                        }
                    }
                } catch (Exception e) {
                    // Ignorar error, pasamos al return final
                }
            }
            // 3. Linux / Mac
            else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                try {
                    // Intentamos leer el archivo directo del sistema (más rápido y seguro en Linux)
                    Process p = Runtime.getRuntime().exec(new String[]{"cat", "/sys/class/dmi/id/product_serial"});
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                        String line = reader.readLine();
                        if (line != null) return line.trim();
                    }

                    // Fallback a dmidecode
                    p = Runtime.getRuntime().exec(new String[]{"dmidecode", "-s", "system-serial-number"});
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                        String line = reader.readLine();
                        if (line != null) return line.trim();
                    }
                } catch (Exception e) {
                    // Ignorar
                }
            }

            // 4. Si todo falla, no rompemos el programa
            return "UNKNOWN-SERIAL";
        }

    private static String getenvOrDefault(String k, String d) {
        String v = System.getenv(k);
        return (v == null || v.isBlank()) ? d : v;
    }

    private static String resolveHostname() {
        try { return InetAddress.getLocalHost().getHostName(); }
        catch (Exception e) { return "UNKNOWN-HOST"; }
    }

    //Busca la IP real (ignora localhost 127.0.0.1)
    private static String resolveIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) { return "0.0.0.0"; }
    }

    //Busca la dirección MAC de la primera tarjeta de red
    private static String resolveMac() {
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while (networks.hasMoreElements()) {
                NetworkInterface net = networks.nextElement();
                byte[] mac = net.getHardwareAddress();

                // 1. Validaciones básicas
                if (mac == null || mac.length == 0 || net.isLoopback() || !net.isUp()) {
                    continue;
                }

                // 2. FILTRO DE ADAPTADORES VIRTUALES
                // Obtenemos el nombre para analizarlo
                String display = net.getDisplayName().toLowerCase();
                String name = net.getName().toLowerCase();

                // Lista negra de palabras clave de adaptadores virtuales
                if (display.contains("virtual") || display.contains("vmware") ||
                        display.contains("vbox") || display.contains("host-only") ||
                        display.contains("vpn") || display.contains("docker") ||
                        display.contains("container") || display.contains("pseudo") ||
                        display.contains("teredo") || display.contains("wsl")) {
                    continue; // Saltamos este adaptador, es falso
                }

                // (Opcional) Preferir Ethernet o Wi-Fi explícitamente si quieres ser más estricto
                // if (!display.contains("ethernet") && !display.contains("wi-fi") && !display.contains("wireless")) continue;

                // 3. Si pasó el filtro, es una tarjeta física real
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                return sb.toString();
            }
        } catch (Exception e) { e.printStackTrace(); }

        // Retorno por defecto si no encuentra nada
        return "00-00-00-00-00-00";
    }
}