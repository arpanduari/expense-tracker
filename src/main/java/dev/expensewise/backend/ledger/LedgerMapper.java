package dev.expensewise.backend.ledger;

import dev.expensewise.backend.ledger.dto.*;
import dev.expensewise.backend.projection.ILedgerUserEntryDetails;
import org.springframework.stereotype.Component;

/**
 * @author arpan
 * @since 9/22/25
 */
@Component
public class LedgerMapper {
    public LedgerUser toLedgerUser(LedgerUserRequest ledgerUserRequest) {
        return LedgerUser.builder()
                .email(ledgerUserRequest.email())
                .name(ledgerUserRequest.name())
                .build();
    }

    public LedgerUserResponse toLedgerUserResponse(LedgerUser ledgerUser) {
        return new LedgerUserResponse(ledgerUser.getId(), ledgerUser.getName(), ledgerUser.getEmail());
    }

    public LedgerEntry toLedgerEntry(LedgerEntryRequest ledgerEntry) {
        return LedgerEntry.builder()
                .amount(ledgerEntry.amount())
                .type(ledgerEntry.type())
                .description(ledgerEntry.description())
                .createdDate(ledgerEntry.createdDate())
                .build();
    }

    public LedgerEntryResponse toLedgerEntryResponse(LedgerEntry ledgerEntry) {
        return new LedgerEntryResponse(
                ledgerEntry.getId(),
                ledgerEntry.getAmount(),
                ledgerEntry.getType(),
                ledgerEntry.getDescription(),
                ledgerEntry.getCreatedDate()
        );
    }

    public LedgerUserEntryResponse toLedgerUserEntryResponse(ILedgerUserEntryDetails userEntryDetails) {
        return new LedgerUserEntryResponse(
                userEntryDetails.getId(),
                userEntryDetails.getName(),
                userEntryDetails.getAvatarUrl(),
                userEntryDetails.getEmail(),
                userEntryDetails.getTotalAmount(),
                userEntryDetails.getLastUpdated()
        );
    }
}
