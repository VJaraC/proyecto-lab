package proyecto.lab.agent.model;

// 1. Cambia la importación a Gson
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public record BatchPayload(
        @SerializedName("agent_key") String agentKey,
        @SerializedName("host") Map<String, String> hostInfo,
        @SerializedName("samples") List<MetricSample> samples
) {
    public static BatchPayload of(String agentKey, Map<String, String> hostInfo, List<MetricSample> samples) {
        return new BatchPayload(agentKey, hostInfo, samples);
    }
}