package rs.karajovic.milan.demo_kafka_consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import rs.karajovic.milan.demo_kafka_consumer.entity.StockEvent;
import rs.karajovic.milan.demo_kafka_consumer.jpa.StockEventsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
//import org.springframework.util.concurrent.ListenableFuture;
//import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Optional;

@Service
@Slf4j
public class StockEventsService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    KafkaTemplate<Integer,String> kafkaTemplate;

    @Autowired
    private StockEventsRepository stockEventsRepository;

    public void processStockEvent(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {
        // JSON iz 'consumerRecord' si pretvorio u 'StockEvent.class'
        StockEvent stockEvent = objectMapper.readValue(consumerRecord.value(), StockEvent.class);
        log.info("stockEvent : {} ", stockEvent);

        // Simulacija Exception-a, da baca Exception kaa je stockEventId == 999
        if(stockEvent.getStockEventId()!=null && ( stockEvent.getStockEventId()==999 )){
            throw new RecoverableDataAccessException("Temporary Network Issue");
        }

        switch(stockEvent.getStockEventType()){
            case NEW:
                save(stockEvent);
                break;
            case UPDATE:
                //validate the stockEvent
                validate(stockEvent);
                save(stockEvent);
                break;
            default:
                log.info("Invalid Stock Event Type");
        }

    }

    // Sa exception-ima resavas da li je validan ili nije update
    private void validate(StockEvent stockEvent) {
        if(stockEvent.getStockEventId()==null){
            throw new IllegalArgumentException("Stocky Event Id is missing");
        }

        Optional<StockEvent> stockEventOptional = stockEventsRepository.findById(stockEvent.getStockEventId());
        if(!stockEventOptional.isPresent()){
            throw new IllegalArgumentException("Not a valid stock Event");
        }
        log.info("Validation is successful for the stock Event : {} ", stockEventOptional.get());
    }

    private void save(StockEvent stockEvent) {
        stockEvent.getArticle().setStockEvent(stockEvent);
        stockEventsRepository.save(stockEvent);
        log.info("Successfully Persisted the libary Event {} ", stockEvent);
    }

    public void handleRecovery(ConsumerRecord<Integer,String> record){

        Integer key = record.key();
        String message = record.value();

        var listenableFuture = kafkaTemplate.sendDefault(key, message);
        listenableFuture.whenComplete((sendResult, throwable) -> {
            if (throwable != null) {
                handleFailure(key, message, throwable);
            } else {
                handleSuccess(key, message, sendResult);

            }
        });
    }

    private void handleFailure(Integer key, String value, Throwable ex) {
        log.error("Error Sending the Message and the exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in OnFailure: {}", throwable.getMessage());
        }
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
        log.info("Message Sent SuccessFully for the key : {} and the value is {} , partition is {}", key, value, result.getRecordMetadata().partition());
    }
}
