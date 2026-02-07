package rs.karajovic.milan.demo_kafka_consumer.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 *
 * @author Milan Karajovic <milan.karajovic.rs@gmail.com>
 *
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class StockEvent {

    @Id
    @GeneratedValue
    private Integer stockEventId;
    @Enumerated(EnumType.STRING)
    private StockEventType stockEventType;
    @OneToOne(mappedBy = "stockEvent", cascade = {CascadeType.ALL})
    @ToString.Exclude
    private Article article;

}
