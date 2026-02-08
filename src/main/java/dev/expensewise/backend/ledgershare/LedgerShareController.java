package dev.expensewise.backend.ledgershare;

import dev.expensewise.backend.config.security.CustomUserDetails;
import dev.expensewise.backend.constants.application.PageConstants;
import dev.expensewise.backend.ledger.dto.LedgerEntryResponse;
import dev.expensewise.backend.ledgershare.dto.LedgerShareRequest;
import dev.expensewise.backend.ledgershare.dto.LedgerShareResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

/**
 * @author arpan
 * @since 12/22/25
 */
@Slf4j
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
    public ModelAndView handleUniversalLink(
            @PathVariable UUID id, @RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent) {

        if (!ledgerService.isValidShare(id)) {
            ModelAndView mv = new ModelAndView("link-expired");
            mv.addObject("shareId", id);
            return mv;
        }

        PlatformInfo platformInfo = detectPlatform(userAgent);

        ModelAndView mv = new ModelAndView("share-page");
        mv.addObject("shareId", id);
        mv.addObject("isAndroid", platformInfo.isAndroid());
        mv.addObject("isIos", platformInfo.isIos());
        mv.addObject("androidScheme", androidScheme);
        mv.addObject("androidPackage", androidPackage);
        mv.addObject("frontendPath", frontEndPath);
        return mv;
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
