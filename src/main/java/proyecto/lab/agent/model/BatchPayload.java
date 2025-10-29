package proyecto.lab.agent.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public record BatchPayload(
        @JsonProperty("agent_key") String agentKey,
        @JsonProperty("host") Map<String, String> hostInfo,
        @JsonProperty("samples") List<MetricSample> samples
) {
    public static BatchPayload of(String agentKey, Map<String, String> hostInfo, List<MetricSample> samples) {
        return new BatchPayload(agentKey, hostInfo, samples);
    }
}
