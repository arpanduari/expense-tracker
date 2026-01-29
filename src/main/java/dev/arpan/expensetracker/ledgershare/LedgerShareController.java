package dev.arpan.expensetracker.ledgershare;

import dev.arpan.expensetracker.constants.application.PageConstants;
import dev.arpan.expensetracker.ledger.dto.LedgerEntryResponse;
import dev.arpan.expensetracker.ledgershare.dto.LedgerShareRequest;
import dev.arpan.expensetracker.ledgershare.dto.LedgerShareResponse;
import dev.arpan.expensetracker.security.CustomUserDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author arpan
 * @since 12/22/25
 */
@RestController
@RequestMapping("${api.base}${api.version}/ledger/share")
public class LedgerShareController {
    private final LedgerShareService ledgerService;

    public LedgerShareController(LedgerShareService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @PostMapping
    public ResponseEntity<LedgerShareResponse> shareLedger(
            @RequestBody @Valid LedgerShareRequest ledgerShareRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        LedgerShareResponse ledgerShareResponse = ledgerService.shareLedger(ledgerShareRequest, userDetails.getUser());
        return ResponseEntity.ok(ledgerShareResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSharedLedger(
            @PathVariable UUID id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        ledgerService.deleteLedgerShare(id, userDetails.getUser());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{ledgerUserId}")
    public ResponseEntity<List<LedgerShareResponse>> getSharedLedgers(
            @PathVariable Long ledgerUserId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<LedgerShareResponse> response = ledgerService.getAllLedgerShares(userDetails.getUser(), ledgerUserId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/public/shared-entries/{id}")
    public ResponseEntity<Page<LedgerEntryResponse>> getSharedLedgerEntries(
            @PathVariable UUID id,
            @RequestParam(defaultValue = PageConstants.DEFAULT_PAGE_NUMBER) @Min(PageConstants.MIN_PAGE_NUMBER)
                    int page,
            @RequestParam(defaultValue = PageConstants.DEFAULT_PAGE_SIZE)
                    @Min(PageConstants.MIN_PAGE_SIZE_LIMIT)
                    @Max(PageConstants.MAX_PAGE_SIZE_LIMIT)
                    int size) {
        Page<LedgerEntryResponse> ledgerEntries = ledgerService.getSharedLedger(id, page, size);
        return ResponseEntity.ok(ledgerEntries);
    }
}
