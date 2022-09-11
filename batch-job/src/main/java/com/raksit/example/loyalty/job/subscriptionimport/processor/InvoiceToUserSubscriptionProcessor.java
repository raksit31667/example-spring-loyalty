package com.raksit.example.loyalty.job.subscriptionimport.processor;

import com.raksit.example.loyalty.job.subscriptionimport.invoice.Invoice;
import com.raksit.example.loyalty.job.subscriptionimport.subscription.entity.UserSubscription;
import org.springframework.batch.item.ItemProcessor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class InvoiceToUserSubscriptionProcessor implements ItemProcessor<Invoice, UserSubscription> {

  @Override
  public UserSubscription process(Invoice invoice) {
    return new UserSubscription(
        UUID.fromString(invoice.userId()),
        invoice.productId(),
        invoice.productName(),
        Instant.parse(invoice.issuedDate()),
        Instant.parse(invoice.issuedDate())
            .plus(365, ChronoUnit.DAYS)
    );
  }
}
