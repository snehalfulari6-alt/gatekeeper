package in.coderarmy.gatekeeper.management.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GatewayConfigurationChangeEvent {

    private String eventId;
    private String eventType;
    private String changeType;
    private Long organizationId;
    private String routeId;
    private Long configurationVersion;
    private LocalDateTime occurredAt;
}
