package proyecto.lab.agent;

import proyecto.lab.agent.collect.Sampler;
import proyecto.lab.agent.config.AgentConfig;
import proyecto.lab.agent.model.BatchPayload;
import proyecto.lab.agent.model.MetricSample;
import proyecto.lab.agent.transport.Transport;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class AgentMain {

    private final AgentConfig cfg;
    private final Sampler sampler;
    private final Transport transport;

    // buffer de muestras (para 1 minuto)
    private final Deque<MetricSample> ring = new ArrayDeque<>();
    private final ScheduledExecutorService ses = Executors.newScheduledThreadPool(2);

    public AgentMain(AgentConfig cfg, Sampler sampler, Transport transport) {
        this.cfg = cfg;
        this.sampler = sampler;
        this.transport = transport;
    }

    public void start() {
        // Captura cada 15s
        ses.scheduleAtFixedRate(() -> {
            try {
                MetricSample s = sampler.sample();
                synchronized (ring) {
                    ring.addLast(s);
                    while (ring.size() > 16) ring.removeFirst(); // límite de seguridad
                }
                System.out.println("[Agent] sample @ " + Instant.now() + " -> " + s);
            } catch (Exception e) {
                System.err.println("[Agent] Error al capturar: " + e.getMessage());
            }
        }, 0, cfg.sampleIntervalSeconds(), TimeUnit.SECONDS);

        // Envío cada 60s
        ses.scheduleAtFixedRate(() -> {
            List<MetricSample> batch;
            synchronized (ring) {
                if (ring.isEmpty()) return;
                batch = new ArrayList<>(ring);
                ring.clear();
            }
            try {
                BatchPayload payload = BatchPayload.of(cfg.agentKey(), cfg.hostInfo(), batch);
                transport.send(payload);
                System.out.println("[Agent] batch sent: " + batch.size() + " samples");
            } catch (Exception e) {
                System.err.println("[Agent] send failed: " + e.getMessage());
                synchronized (ring) { batch.forEach(ring::addFirst); } // rollback simple
            }
        }, cfg.sendIntervalSeconds(), cfg.sendIntervalSeconds(), TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        AgentConfig cfg = AgentConfig.fromEnvOrDefaults();
        Sampler sampler = new Sampler();
        Transport transport = Transport.noop(); // imprime JSON en consola
        new AgentMain(cfg, sampler, transport).start();
    }
}