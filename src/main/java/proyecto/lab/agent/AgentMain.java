package proyecto.lab.agent;

import proyecto.lab.agent.collect.Sampler;
import proyecto.lab.agent.config.AgentConfig;
import proyecto.lab.agent.model.BatchPayload;
import proyecto.lab.agent.model.MetricSample;
import proyecto.lab.agent.transport.HttpTransport; // <--- 1. Importante: Importar tu nueva clase
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

    // buffer de muestras (para 1 minuto aprox)
    private final Deque<MetricSample> ring = new ArrayDeque<>();
    private final ScheduledExecutorService ses = Executors.newScheduledThreadPool(2);

    public AgentMain(AgentConfig cfg, Sampler sampler, Transport transport) {
        this.cfg = cfg;
        this.sampler = sampler;
        this.transport = transport;
    }

    public void start() {
        // TAREA 1: Captura cada X segundos (por defecto 15s)
        ses.scheduleAtFixedRate(() -> {
            try {
                MetricSample s = sampler.sample();
                synchronized (ring) {
                    ring.addLast(s);
                    while (ring.size() > 60) ring.removeFirst(); // Límite de seguridad aumentado un poco
                }
                System.out.println("[Agent] sample @ " + Instant.now() + " -> " + s);
            } catch (Exception e) {
                System.err.println("[Agent] Error al capturar: " + e.getMessage());
            }
        }, 0, cfg.sampleIntervalSeconds(), TimeUnit.SECONDS);

        // TAREA 2: Envío cada Y segundos (por defecto 60s)
        ses.scheduleAtFixedRate(() -> {
            List<MetricSample> batch;
            synchronized (ring) {
                if (ring.isEmpty()) return;
                batch = new ArrayList<>(ring);
                ring.clear();
            }
            try {
                // Construye el paquete con la info del Host y la lista de métricas
                BatchPayload payload = BatchPayload.of(cfg.agentKey(), cfg.hostInfo(), batch);

                // Envía usando el transporte configurado (ahora es HttpTransport)
                transport.send(payload);

                System.out.println("[Agent] batch sent: " + batch.size() + " samples");
            } catch (Exception e) {
                System.err.println("[Agent] send failed: " + e.getMessage());
                // Rollback: Devuelve las métricas al buffer para reintentar luego
                synchronized (ring) {
                    // Solo devolvemos si hay espacio para evitar desbordamiento
                    if(ring.size() < 100) batch.forEach(ring::addFirst);
                }
            }
        }, cfg.sendIntervalSeconds(), cfg.sendIntervalSeconds(), TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        // 1. Cargar configuración (Variables de entorno o valores por defecto)
        AgentConfig cfg = AgentConfig.fromEnvOrDefaults();

        // 2. Inicializar el recolector de métricas
        Sampler sampler = new Sampler();

        // 3. Inicializar el Transporte REAL
        //    Si quieres cambiar la URL, configura la variable de entorno INGEST_URL
        Transport transport = new HttpTransport(cfg.ingestUrl());

        System.out.println("Iniciando Agente...");
        System.out.println(" -> Server URL: " + cfg.ingestUrl());
        System.out.println(" -> Agent Key:  " + cfg.agentKey());

        // 4. Arrancar el agente
        new AgentMain(cfg, sampler, transport).start();
    }
}