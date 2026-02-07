package rs.karajovic.milan.demo_kafka_producer.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Milan Karajovic <milan.karajovic.rs@gmail.com>
 *
 */

public record StockEvent(
        Integer stockEventId,
        StockEventType stockEventType,
        @NotNull
        @Valid
        Article article
) {
}
