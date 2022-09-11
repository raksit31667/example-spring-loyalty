package com.raksit.example.loyalty.job.subscriptionimport.processor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.raksit.example.loyalty.job.subscriptionimport.invoice.Invoice;
import com.raksit.example.loyalty.job.subscriptionimport.subscription.entity.UserSubscription;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

class InvoiceToUserSubscriptionProcessorTest {

  @Test
  void shouldReturnUserSubscription_whenProcess_givenInvoice() {
    // Given
    Invoice invoice = new Invoice(
        UUID.randomUUID().toString(),
        RandomStringUtils.random(5),
        RandomStringUtils.random(5),
        Instant.now().toString()
    );
    InvoiceToUserSubscriptionProcessor processor = new InvoiceToUserSubscriptionProcessor();

    // When
    UserSubscription userSubscription = processor.process(invoice);

    // Then
    assertThat(userSubscription.getUserId(), equalTo(UUID.fromString(invoice.userId())));
    assertThat(userSubscription.getProductName(), equalTo(invoice.productName()));
    assertThat(userSubscription.getProductId(), equalTo(invoice.productId()));
    assertThat(userSubscription.getSubscribedOn(), equalTo(Instant.parse(invoice.issuedDate())));
    assertThat(userSubscription.getActiveUntil(), equalTo(Instant.parse(invoice.issuedDate())
        .plus(365, ChronoUnit.DAYS))
    );
  }
}