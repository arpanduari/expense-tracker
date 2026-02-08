package dev.expensewise.backend.ledgershare;

import dev.expensewise.backend.config.properties.ApiProperties;
import dev.expensewise.backend.exception.ForbiddenException;
import dev.expensewise.backend.exception.MaximumShareLimitReachedException;
import dev.expensewise.backend.exception.ResourceExpiredException;
import dev.expensewise.backend.exception.ResourceNotFoundException;
import dev.expensewise.backend.ledger.LedgerMapper;
import dev.expensewise.backend.ledger.LedgerRepository;
import dev.expensewise.backend.ledger.LedgerUser;
import dev.expensewise.backend.ledger.LedgerUserRepository;
import dev.expensewise.backend.ledger.dto.LedgerEntryResponse;
import dev.expensewise.backend.ledgershare.dto.LedgerShareRequest;
import dev.expensewise.backend.ledgershare.dto.LedgerShareResponse;
import dev.expensewise.backend.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author arpan
 * @since 12/22/25
 */
@Service
@RequiredArgsConstructor
public class LedgerShareService {
    private final LedgerUserRepository ledgerUserRepository;
    private final LedgerShareRepository ledgerShareRepository;
    private final LedgerRepository ledgerRepository;
    private final LedgerMapper ledgerMapper;
    private final ApiProperties apiProperties;

    @Value("${app.frontend.path}")
    private String frontendPath;

    @Value("${app.backend.path}")
    private String backendPath;

    private static final String PUBLIC_LINK = "%s%s%s/ledger/share/public/link/%s";

    public LedgerShareResponse shareLedger(LedgerShareRequest shareRequest, User user) {
        LedgerUser ledgerUser = ledgerUserRepository
                .findById(shareRequest.ledgerUserId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Ledger User", "ID", shareRequest.ledgerUserId() + " "));

        if (notAuthorizedUser(ledgerUser.getId(), user.getId())) {
            throw new ForbiddenException("You are not authorized to share this.");
        }

        List<LedgerShare> ledgerShares =
                ledgerShareRepository.findBySharedByIdAndLedgerUserId(user.getId(), ledgerUser.getId());

        Optional<LedgerShare> optionalLedgerShare = checkNonExpiry(ledgerShares);

        if (!ledgerShares.isEmpty() && optionalLedgerShare.isPresent() && shareRequest.expiryDuration() == null) {
            UUID id = optionalLedgerShare.get().getId();
            String publicLink =
                    PUBLIC_LINK.formatted(backendPath, apiProperties.getBase(), apiProperties.getVersion(), id);
            return new LedgerShareResponse(id, publicLink);
        }

        if (ledgerShares.size() == 10) {
            throw new MaximumShareLimitReachedException("You have reached the maximum number of shares.");
        }

        Instant expiry =
                shareRequest.expiryDuration() == null ? null : Instant.now().plus(shareRequest.expiryDuration());

        LedgerShare ledgerShare = LedgerShare.builder()
                .sharedBy(user)
                .ledgerUser(ledgerUser)
                .expiresAt(expiry)
                .build();

        ledgerShare = ledgerShareRepository.save(ledgerShare);

        UUID id = ledgerShare.getId();
        String publicLink = PUBLIC_LINK.formatted(backendPath, apiProperties.getBase(), apiProperties.getVersion(), id);
        return new LedgerShareResponse(id, publicLink);
    }

    public Page<LedgerEntryResponse> getSharedLedger(UUID id, int page, int size) {
        LedgerShare ledgerShare = ledgerShareRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shared Entries", "id", id.toString()));

        if (ledgerShare.getExpiresAt() != null && ledgerShare.getExpiresAt().isBefore(Instant.now())) {
            throw new ResourceExpiredException("Shared Entries have expired.");
        }

        Pageable pageable = Pageable.ofSize(size).withPage(page);

        return ledgerRepository
                .findByCreatedByAndLedgerUserId(
                        ledgerShare.getSharedBy().getId(),
                        ledgerShare.getLedgerUser().getId(),
                        pageable)
                .map(ledgerMapper::toLedgerEntryResponse);
    }

    @Transactional
    public void deleteLedgerShare(UUID id, User user) {
        LedgerShare ledgerShare = ledgerShareRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shared Entries", "id", id.toString()));
        if (notAuthorizedUser(ledgerShare.getLedgerUser().getId(), user.getId())) {
            throw new ForbiddenException("You are not authorized to delete this.");
        }
        ledgerShareRepository.deleteById(id);
    }

    public List<LedgerShareResponse> getAllLedgerShares(User user, Long ledgerUserId) {
        return ledgerShareRepository.findBySharedByIdAndLedgerUserId(user.getId(), ledgerUserId).stream()
                .map(entry -> new LedgerShareResponse(
                        entry.getId(),
                        PUBLIC_LINK.formatted(
                                backendPath, apiProperties.getBase(), apiProperties.getVersion(), entry.getId())))
                .toList();
    }

    public boolean isValidShare(UUID id) {
        Optional<LedgerShare> ledgerShare = ledgerShareRepository.findById(id);
        if (ledgerShare.isEmpty()) {
            return false;
        }
        LedgerShare share = ledgerShare.get();

        return share.getExpiresAt() == null || !share.getExpiresAt().isBefore(Instant.now());
    }

    private boolean notAuthorizedUser(Long ledgerUserId, Long userId) {
        return !ledgerUserRepository.existsByIdAndCreatedById(ledgerUserId, userId);
    }

    private Optional<LedgerShare> checkNonExpiry(List<LedgerShare> ledgerShares) {
        for (LedgerShare ledgerShare : ledgerShares) {
            if (ledgerShare.getExpiresAt() == null) {
                return Optional.of(ledgerShare);
            }
        }
        return Optional.empty();
    }
}
