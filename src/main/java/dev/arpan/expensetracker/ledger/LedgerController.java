package dev.arpan.expensetracker.ledger;

import dev.arpan.expensetracker.ledger.dto.*;
import dev.arpan.expensetracker.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author arpan
 * @since 9/22/25
 */
@RestController
@RequestMapping("${api.base}${api.version}/ledger")
public class LedgerController {
    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @PostMapping("/contact")
    public ResponseEntity<LedgerUserResponse> createLedgerUser(@RequestBody LedgerUserRequest ledgerUserRequest,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        LedgerUserResponse ledgerUserResponse = ledgerService.createLedgerUser(userDetails.getUser(), ledgerUserRequest);
        return ResponseEntity.ok(ledgerUserResponse);
    }

    @PatchMapping("/contact/{id:\\d+}")
    public ResponseEntity<LedgerUserResponse> updateLedgerUser(@PathVariable Long id,
                                                               @RequestBody LedgerUserRequest ledgerUserRequest,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        LedgerUserResponse ledgerUserResponse = ledgerService.updateLedgerUser(userDetails.getUser(), id, ledgerUserRequest);
        return ResponseEntity.ok(ledgerUserResponse);
    }

    @DeleteMapping("/contact/{id:\\d+}")
    public ResponseEntity<Void> deleteLedgerUser(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        ledgerService.deleteLedgerUser(userDetails.getUser(), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/entry")
    public ResponseEntity<LedgerEntryResponse> createLedgerEntry(@RequestBody @Valid LedgerEntryRequest ledgerEntryRequest) {
        LedgerEntryResponse ledgerEntryResponse = ledgerService.createLedgerEntry(ledgerEntryRequest);
        return ResponseEntity.ok(ledgerEntryResponse);
    }

    @GetMapping("/entry/{id:\\d+}")
    public ResponseEntity<LedgerEntryResponse> getLedgerEntry(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        LedgerEntryResponse ledgerEntryResponse = ledgerService.getLedgerEntry(userDetails.getUser(), id);
        return ResponseEntity.ok(ledgerEntryResponse);
    }

    @PatchMapping("/entry/{id:\\d+}")
    public ResponseEntity<LedgerEntryResponse> updateLedgerEntry(
            @PathVariable Long id,
            @RequestBody @Valid LedgerEntryRequest ledgerEntryRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        LedgerEntryResponse ledgerEntryResponse = ledgerService.updateLedgerEntry(id, ledgerEntryRequest, userDetails.getUser());
        return ResponseEntity.ok(ledgerEntryResponse);
    }


    @DeleteMapping("/entry/{id:\\d+}")
    public ResponseEntity<Void> deleteLedgerEntry(@PathVariable Long id,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        ledgerService.deleteLedgerEntry(userDetails.getUser(), id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/contacts")
    public ResponseEntity<List<LedgerUserEntryResponse>> getAllContacts(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<LedgerUserEntryResponse> contacts = ledgerService.getAllContacts(userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK)
                .cacheControl(CacheControl.maxAge(30L, TimeUnit.SECONDS))
                .body(contacts);
    }

    @GetMapping("/contacts/{ledgerUser:\\d+}/entries")
    public ResponseEntity<List<LedgerEntryResponse>> getUserTransactions(
            @PathVariable Long ledgerUser,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<LedgerEntryResponse> ledgerEntries = ledgerService.getAllLedgerEntries(userDetails.getUser(), ledgerUser);
        return ResponseEntity.status(HttpStatus.OK)
                .cacheControl(CacheControl.maxAge(30L, TimeUnit.SECONDS))
                .body(ledgerEntries);
    }
}
