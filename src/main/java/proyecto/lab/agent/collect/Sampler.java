package proyecto.lab.agent.collect;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import proyecto.lab.agent.model.MetricSample;

import java.time.Instant;

public final class Sampler {
    private final SystemInfo si = new SystemInfo();
    private final HardwareAbstractionLayer hal = si.getHardware();
    private long[] prevTicks = hal.getProcessor().getSystemCpuLoadTicks();

    public MetricSample sample() {
        CentralProcessor cpu = hal.getProcessor();
        GlobalMemory mem = hal.getMemory();

        double cpuTotal = cpu.getSystemCpuLoadBetweenTicks(prevTicks) * 100.0;
        prevTicks = cpu.getSystemCpuLoadTicks();

        long memTotal = mem.getTotal();
        long memAvail = mem.getAvailable();
        long memUsed = memTotal - memAvail;

        return new MetricSample(
                Instant.now(),
                cpuTotal,
                memUsed,
                memTotal,
                null,
                null,
                null,
                null,
                null
        );
    }
}
