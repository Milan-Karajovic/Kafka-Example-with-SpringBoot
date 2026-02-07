package rs.karajovic.milan.demo_kafka_producer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import rs.karajovic.milan.demo_kafka_producer.domain.StockEvent;
import rs.karajovic.milan.demo_kafka_producer.domain.StockEventType;
import rs.karajovic.milan.demo_kafka_producer.producer.StockEventProducer;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Milan Karajovic <milan.karajovic.rs@gmail.com>
 *
 */

@RestController
@Slf4j
public class StockEventsController {

    @Autowired
    StockEventProducer stockEventProducer;

    // POST
    @PostMapping("/v1/stockevent")
    public ResponseEntity<?> postStockEvent(@RequestBody @Valid StockEvent stockEvent) throws JsonProcessingException {

        if (StockEventType.NEW != stockEvent.stockEventType()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only NEW event type is supported");
        }

        stockEventProducer.sendStockEvent_Approach2(stockEvent);

        //stockEventProducer.sendStockEvent(stockEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(stockEvent);
    }

    @PutMapping("/v1/stockevent")
    public ResponseEntity<?> putStockEvent(@RequestBody @Valid StockEvent stockEvent) throws JsonProcessingException {


        ResponseEntity<String> BAD_REQUEST = validateStockEvent(stockEvent);
        if (BAD_REQUEST != null) return BAD_REQUEST;

        stockEventProducer.sendStockEvent_Approach2(stockEvent);
        log.info("after produce call");
        return ResponseEntity.status(HttpStatus.OK).body(stockEvent);
    }

    private static ResponseEntity<String> validateStockEvent(StockEvent stockEvent) {
        if (stockEvent.stockEventId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the StockEventId");
        }

        if (!StockEventType.UPDATE.equals(stockEvent.stockEventType()))  {
            log.info("Inside the if block");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only UPDATE event type is supported");
        }
        return null;
    }

}
