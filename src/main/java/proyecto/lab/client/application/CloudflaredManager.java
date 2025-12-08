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

        // Buscar cloudflared.exe en el directorio actual (donde está el .jar/.bat)
        String exeName = "cloudflared.exe";
        Path exePath = Paths.get(exeName).toAbsolutePath();

        if (!Files.exists(exePath)) {
            throw new IOException("No se encontró el ejecutable de Cloudflared en: " + exePath);
        }

        // Comando: cloudflared access tcp --hostname pg.proyecto-lab.sbs --url localhost:15432
        ProcessBuilder pb = new ProcessBuilder(
                exePath.toString(),
                "access", "tcp",
                "--hostname", "pg.proyecto-lab.sbs",
                "--url", "localhost:15432"
        );

        // Ver logs en la consola de la app
        pb.redirectErrorStream(true);
        pb.inheritIO();

        cloudflaredProcess = pb.start();

        // Esperar un poco a que levante el túnel
        Thread.sleep(2000);
    }

    public static void stopTunnel() {
        if (cloudflaredProcess != null && cloudflaredProcess.isAlive()) {
            cloudflaredProcess.destroy();
        }
    }
}
