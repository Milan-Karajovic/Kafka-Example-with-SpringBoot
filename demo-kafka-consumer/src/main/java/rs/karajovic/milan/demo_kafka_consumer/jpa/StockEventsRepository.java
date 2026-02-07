package rs.karajovic.milan.demo_kafka_consumer.jpa;

import rs.karajovic.milan.demo_kafka_consumer.entity.StockEvent;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Milan Karajovic <milan.karajovic.rs@gmail.com>
 *
 */

public interface StockEventsRepository extends CrudRepository<StockEvent,Integer> {
}

