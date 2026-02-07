package rs.karajovic.milan.demo_kafka_producer.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Milan Karajovic <milan.karajovic.rs@gmail.com>
 *
 */

public record Article(
        @NotNull
        Integer articleId,
        @NotBlank
        String articleName,
        @NotBlank
        String articlePrice) {
}
