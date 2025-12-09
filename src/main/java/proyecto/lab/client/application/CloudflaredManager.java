package proyecto.lab.client.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CloudflaredManager {

    private static Process cloudflaredProcess;

    public static void startTunnel() throws IOException, InterruptedException {
        // Si ya está levantado, no hacer nada
        if (cloudflaredProcess != null && cloudflaredProcess.isAlive()) {
            return;
        }

        // Detectar sistema operativo
        String os = System.getProperty("os.name").toLowerCase();
        Path exePath;

        if (os.contains("win")) {
            // Windows
            exePath = Paths.get("cloudflared.exe").toAbsolutePath();
        } else {
            // Mac o Linux: buscar variable de entorno primero
            String envPath = System.getenv("CLOUDFLARED_PATH");
            if (envPath != null && !envPath.isEmpty()) {
                exePath = Paths.get(envPath);
            } else {
                exePath = Paths.get("cloudflared").toAbsolutePath();
            }
        }

        if (!Files.exists(exePath)) {
            throw new IOException("No se encontró el ejecutable de Cloudflared en: " + exePath);
        }

        // En Mac/Linux, asegurar permisos de ejecución
        if (!os.contains("win")) {
            exePath.toFile().setExecutable(true);
        }

        ProcessBuilder pb = new ProcessBuilder(
                exePath.toString(),
                "access", "tcp",
                "--hostname", "pg.proyecto-lab.sbs",
                "--url", "localhost:15432"
        );

        pb.redirectErrorStream(true);
        pb.inheritIO();

        cloudflaredProcess = pb.start();
        Thread.sleep(2000);
    }

    public static void stopTunnel() {
        if (cloudflaredProcess != null && cloudflaredProcess.isAlive()) {
            cloudflaredProcess.destroy();
        }
    }
}