package rs.karajovic.milan.demo_kafka_consumer.scheduler;

import rs.karajovic.milan.demo_kafka_consumer.config.StockEventsConsumerConfig;
import rs.karajovic.milan.demo_kafka_consumer.entity.FailureRecord;
import rs.karajovic.milan.demo_kafka_consumer.jpa.FailureRecordRepository;
import rs.karajovic.milan.demo_kafka_consumer.service.StockEventsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * @author Milan Karajovic <milan.karajovic.rs@gmail.com>
 *
 */

//@Component
@Slf4j
public class RetryScheduler {

    @Autowired
    StockEventsService stockEventsService;

    @Autowired
    FailureRecordRepository failureRecordRepository;


    @Scheduled(fixedRate = 10000 )
    public void retryFailedRecords(){

        log.info("Retrying Failed Records Started!");
        var status = StockEventsConsumerConfig.RETRY;
        failureRecordRepository.findAllByStatus(status)
                .forEach(failureRecord -> {
                    try {
                        //stockEventsService.processStockEvent();
                        var consumerRecord = buildConsumerRecord(failureRecord);
                        stockEventsService.processStockEvent(consumerRecord);
                        // stockEventsConsumer.onMessage(consumerRecord); // This does not involve the recovery code for in the consumerConfig
                        failureRecord.setStatus(StockEventsConsumerConfig.SUCCESS);
                    } catch (Exception e){
                        log.error("Exception in retryFailedRecords : ", e);
                    }

                });

    }

    private ConsumerRecord<Integer, String> buildConsumerRecord(FailureRecord failureRecord) {

        return new ConsumerRecord<>(failureRecord.getTopic(),
                failureRecord.getPartition(), failureRecord.getOffset_value(), failureRecord.getKey_value(),
                failureRecord.getErrorRecord());

    }
}

