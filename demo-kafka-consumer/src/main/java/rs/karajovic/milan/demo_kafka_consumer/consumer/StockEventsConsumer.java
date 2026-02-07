package rs.karajovic.milan.demo_kafka_consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import rs.karajovic.milan.demo_kafka_consumer.service.StockEventsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author Milan Karajovic <milan.karajovic.rs@gmail.com>
 *
 */

@Component
@Slf4j
//@KafkaListener
public class StockEventsConsumer {

    @Autowired
    private StockEventsService stockEventsService;

    @KafkaListener(
            topics = {"stock-events"}
            , autoStartup = "${stockListener.startup:true}"
            , groupId = "stock-events-listener-group")
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {

        log.info("ConsumerRecord : {} ", consumerRecord);
        stockEventsService.processStockEvent(consumerRecord);

    }

}
