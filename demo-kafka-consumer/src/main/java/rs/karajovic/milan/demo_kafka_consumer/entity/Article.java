package rs.karajovic.milan.demo_kafka_consumer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class Article {
    @Id
    private Integer articleId;
    private String articleName;
    private String articlePrice;
    @OneToOne
    @JoinColumn(name = "stockEventId")
    private StockEvent stockEvent;
}
