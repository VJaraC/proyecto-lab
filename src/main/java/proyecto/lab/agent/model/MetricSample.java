package proyecto.lab.agent.model;

import java.time.Instant;

public record MetricSample(
        Instant tsUtc,
        Double cpuTotal,
        Long memUsedBytes,
        Long memTotalBytes,
        Long diskUsedBytes,
        Long diskTotalBytes,
        Long netRxBytes,
        Long netTxBytes,
        Double gpuUtil
) {}
