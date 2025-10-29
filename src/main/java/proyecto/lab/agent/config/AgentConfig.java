package proyecto.lab.agent.config;

import java.util.Map;

public record AgentConfig(
        String agentKey,
        String ingestUrl,
        int sampleIntervalSeconds,
        int sendIntervalSeconds,
        Map<String, String> hostInfo
) {
    public static AgentConfig fromEnvOrDefaults() {
        String key = getenvOrDefault("AGENT_KEY", "DEV-PC-01");
        String url = getenvOrDefault("INGEST_URL", "http://localhost:8080/v1/ingest/batch");
        int sample = Integer.parseInt(getenvOrDefault("SAMPLE_SEC", "15"));
        int send = Integer.parseInt(getenvOrDefault("SEND_SEC", "60"));
        Map<String, String> hostInfo = Map.of(
                "name", getenvOrDefault("HOST_NAME", key),
                "os", System.getProperty("os.name"),
                "arch", System.getProperty("os.arch")
        );
        return new AgentConfig(key, url, sample, send, hostInfo);
    }

    private static String getenvOrDefault(String k, String d) {
        String v = System.getenv(k);
        return (v == null || v.isBlank()) ? d : v;
    }
}
