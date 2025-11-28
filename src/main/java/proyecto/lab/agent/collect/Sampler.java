package proyecto.lab.agent.collect;

import proyecto.lab.agent.model.MetricSample;

import java.io.File;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.time.Instant;

public class Sampler {
    private final OperatingSystemMXBean osBean;

    public Sampler() {
        this.osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    public MetricSample sample() {
        // 1. CPU
        double cpuLoad = osBean.getCpuLoad() * 100.0; // MX manda un numero entre 0 y 1, se multiplica por 100 para convertirlo en porcentaje
        if (Double.isNaN(cpuLoad)) cpuLoad = 0.0;

        // 2. RAM
        long totalMem = osBean.getTotalMemorySize(); //obtiene la RAM total
        long usedMem = totalMem - osBean.getFreeMemorySize(); //obtiene la RAM libre, se resta la total con la libre para tener la usada
        double ramPercent = (totalMem > 0) ? ((double) usedMem / totalMem) * 100.0 : 0.0; //convertir la RAM usada en porcentaje

        //3. Obtener el disco usado (en GB)
        File discoC = new File("C:"); // "/" en Linux
        long totalBytes = discoC.getTotalSpace();
        long freeBytes = discoC.getFreeSpace();
        long usedBytes = totalBytes - freeBytes;
        double diskGb = (double) usedBytes / (1024 * 1024 * 1024); //convertir a GB

        return new MetricSample(
                Instant.now().toString(), //momento exacto de la lectura
                cpuLoad,
                ramPercent,
                diskGb
        );
    }
}