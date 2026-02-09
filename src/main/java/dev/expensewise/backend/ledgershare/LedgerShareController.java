package dev.expensewise.backend.ledgershare;

import dev.expensewise.backend.config.security.CustomUserDetails;
import dev.expensewise.backend.constants.application.PageConstants;
import dev.expensewise.backend.ledger.dto.LedgerEntryResponse;
import dev.expensewise.backend.ledgershare.dto.LedgerShareRequest;
import dev.expensewise.backend.ledgershare.dto.LedgerShareResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author arpan
 * @since 12/22/25
 */
@Controller
@RequestMapping("${api.base}${api.version}/ledger/share")
public class LedgerShareController {
    private final LedgerShareService ledgerService;

    @Value("${app.android.scheme}")
    private String androidScheme;

    @Value("${app.android.package}")
    private String androidPackage;

    @Value("${app.frontend.path}")
    private String frontEndPath;

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

    @GetMapping("/user/{ledgerUserId}")
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

    @GetMapping("/public/link/{id}")
    public String handleUniversalLink(
            @PathVariable UUID id,
            @RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent,
            Model model) {

        if (!ledgerService.isValidShare(id)) {
            model.addAttribute("shareId", id);
            return "link-expired";
        }

        PlatformInfo platformInfo = detectPlatform(userAgent);

        model.addAttribute("shareId", id);
        model.addAttribute("isAndroid", platformInfo.isAndroid());
        model.addAttribute("isIos", platformInfo.isIos());
        model.addAttribute("androidScheme", androidScheme);
        model.addAttribute("androidPackage", androidPackage);
        model.addAttribute("frontendPath", frontEndPath);

        return "share-page";
    }

    private PlatformInfo detectPlatform(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) {
            return new PlatformInfo(false, false);
        }
        String ua = userAgent.toLowerCase();
        boolean isAndroid = ua.contains("android");
        boolean isIos = ua.contains("iphone") || ua.contains("ipad");
        return new PlatformInfo(isAndroid, isIos);
    }
}
