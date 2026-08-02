package com.thaimei.myapp.model;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProcessWebhook {
    //no need to auto-generate the ID since it will be provided by Stripe in the webhook payload.
    @Id
    private String eventId;

    //this will store the timestamp when the webhook was processed, which can be useful for auditing and debugging purposes.
    //Instant is a class in Java that represents a specific moment on the timeline in UTC with nanosecond precision. It is part of the java.time package introduced in Java 8.
    private Instant processedAt;
}
