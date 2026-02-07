package rs.karajovic.milan.demo_kafka_consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import rs.karajovic.milan.demo_kafka_consumer.service.StockEventsService;

/**
 *
 * @author Milan Karajovic <milan.karajovic.rs@gmail.com>
 *
 */

@Component
@Slf4j
public class StockEventsRetryConsumer {

    @Autowired
    private StockEventsService stockEventsService;

    @KafkaListener(topics = {"${topics.retry}"}
            , autoStartup = "${retryListener.startup:true}"
            , groupId = "retry-listener-group")
    public void onMessage(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {

        log.info("ConsumerRecord in Retry Consumer: {} ", consumerRecord );
        stockEventsService.processStockEvent(consumerRecord);

    }

}
