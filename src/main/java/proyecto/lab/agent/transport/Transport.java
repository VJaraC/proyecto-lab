package proyecto.lab.agent.transport;

import com.fasterxml.jackson.databind.ObjectMapper;
import proyecto.lab.agent.model.BatchPayload;
import proyecto.lab.agent.util.Json;

public interface Transport {
    void send(BatchPayload payload) throws Exception;

    static Transport noop() {
        return new Transport() {
            private final ObjectMapper om = Json.mapper();
            @Override
            public void send(BatchPayload payload) throws Exception {
                String json = om.writeValueAsString(payload);
                System.out.println("[Transport/NOOP] " + json);
            }
        };
    }
}
