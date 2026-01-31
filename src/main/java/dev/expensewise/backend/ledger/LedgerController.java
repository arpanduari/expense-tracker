package dev.expensewise.backend.ledger;

import dev.expensewise.backend.ledger.dto.*;
import dev.expensewise.backend.config.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author arpan
 * @since 9/22/25
 */
@RestController
@RequestMapping("${api.base}${api.version}/ledger")
@Tag(name = "Ledger Management", description = "Operations related to Ledger")
public class LedgerController {
    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @Operation(
            summary = "Create a new Ledger user",
            description = "Creates a new Ledger user and return the created user",
            responses = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Ledger User created successfully",
                        content = @Content(schema = @Schema(implementation = LedgerUserResponse.class))),
                @ApiResponse(responseCode = "400", description = "Invalid request payload"),
                @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            })
    @PostMapping("/contact")
    public ResponseEntity<LedgerUserResponse> createLedgerUser(
            @RequestBody LedgerUserRequest ledgerUserRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
        LedgerUserResponse ledgerUserResponse =
                ledgerService.createLedgerUser(userDetails.getUser(), ledgerUserRequest);
        return ResponseEntity.ok(ledgerUserResponse);
    }

    @Operation(
            summary = "Update existing Ledger user",
            description = "Updates a existing ledger user and return the updated user",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Ledger user updated successfully",
                        content = @Content(schema = @Schema(implementation = LedgerUserResponse.class)))
            })
    @PatchMapping("/contact/{id:\\d+}")
    public ResponseEntity<LedgerUserResponse> updateLedgerUser(
            @PathVariable Long id,
            @RequestBody LedgerUserRequest ledgerUserRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        LedgerUserResponse ledgerUserResponse =
                ledgerService.updateLedgerUser(userDetails.getUser(), id, ledgerUserRequest);
        return ResponseEntity.ok(ledgerUserResponse);
    }

    @DeleteMapping("/contact/{id:\\d+}")
    public ResponseEntity<Void> deleteLedgerUser(
            @PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        ledgerService.deleteLedgerUser(userDetails.getUser(), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/entry")
    public ResponseEntity<LedgerEntryResponse> createLedgerEntry(
            @RequestBody @Valid LedgerEntryRequest ledgerEntryRequest) {
        LedgerEntryResponse ledgerEntryResponse = ledgerService.createLedgerEntry(ledgerEntryRequest);
        return ResponseEntity.ok(ledgerEntryResponse);
    }

    @GetMapping("/entry/{id:\\d+}")
    public ResponseEntity<LedgerEntryResponse> getLedgerEntry(
            @PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        LedgerEntryResponse ledgerEntryResponse = ledgerService.getLedgerEntry(userDetails.getUser(), id);
        return ResponseEntity.ok(ledgerEntryResponse);
    }

    @PatchMapping("/entry/{id:\\d+}")
    public ResponseEntity<LedgerEntryResponse> updateLedgerEntry(
            @PathVariable Long id,
            @RequestBody @Valid LedgerEntryRequest ledgerEntryRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        LedgerEntryResponse ledgerEntryResponse =
                ledgerService.updateLedgerEntry(id, ledgerEntryRequest, userDetails.getUser());
        return ResponseEntity.ok(ledgerEntryResponse);
    }

    @DeleteMapping("/entry/{id:\\d+}")
    public ResponseEntity<Void> deleteLedgerEntry(
            @PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        ledgerService.deleteLedgerEntry(userDetails.getUser(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/contacts")
    public ResponseEntity<List<LedgerUserEntryResponse>> getAllContacts(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<LedgerUserEntryResponse> contacts = ledgerService.getAllContacts(userDetails.getUser());
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/contacts/{ledgerUser:\\d+}/entries")
    public ResponseEntity<List<LedgerEntryResponse>> getUserTransactions(
            @PathVariable Long ledgerUser, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<LedgerEntryResponse> ledgerEntries = ledgerService.getAllLedgerEntries(userDetails.getUser(), ledgerUser);
        return ResponseEntity.ok(ledgerEntries);
    }
}
