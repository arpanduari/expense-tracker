package dev.arpan.expensetracker.scheduler;

import dev.arpan.expensetracker.ledgershare.LedgerShareRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * @author arpan
 * @since 12/23/25
 */
@Component
public class LedgerSharedLinkCleanupScheduler {
    private final LedgerShareRepository ledgerShareRepository;

    public LedgerSharedLinkCleanupScheduler(LedgerShareRepository ledgerShareRepository) {
        this.ledgerShareRepository = ledgerShareRepository;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void deleteExpiredLinks() {
        ledgerShareRepository.deleteExpired(Instant.now());
    }
}
