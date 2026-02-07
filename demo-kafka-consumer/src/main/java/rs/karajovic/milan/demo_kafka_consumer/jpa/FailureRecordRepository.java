package rs.karajovic.milan.demo_kafka_consumer.jpa;

import rs.karajovic.milan.demo_kafka_consumer.entity.FailureRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 *
 * @author Milan Karajovic <milan.karajovic.rs@gmail.com>
 *
 */

public interface FailureRecordRepository extends CrudRepository<FailureRecord,Integer> {

    List<FailureRecord> findAllByStatus(String status);
}
