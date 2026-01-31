package dev.expensewise.backend.ledgershare;

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
public class LedgerShareService {
    private final LedgerUserRepository ledgerUserRepository;
    private final LedgerShareRepository ledgerShareRepository;
    private final LedgerRepository ledgerRepository;
    private final LedgerMapper ledgerMapper;

    @Value("${app.frontend.path}")
    private String frontendPath;

    public LedgerShareService(
            LedgerUserRepository ledgerUserRepository,
            LedgerShareRepository ledgerShareRepository,
            LedgerMapper ledgerMapper,
            LedgerRepository ledgerRepository) {
        this.ledgerUserRepository = ledgerUserRepository;
        this.ledgerShareRepository = ledgerShareRepository;
        this.ledgerMapper = ledgerMapper;
        this.ledgerRepository = ledgerRepository;
    }

    public LedgerShareResponse shareLedger(LedgerShareRequest ledgerShareRequest, User user) {
        LedgerUser ledgerUser = ledgerUserRepository
                .findById(ledgerShareRequest.ledgerUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ledger User", "ID", ledgerShareRequest.ledgerUserId() + " "));

        if (notAuthorizedUser(ledgerUser.getId(), user.getId())) {
            throw new ForbiddenException("You are not authorized to share this.");
        }

        List<LedgerShare> ledgerShares =
                ledgerShareRepository.findBySharedByIdAndLedgerUserId(user.getId(), ledgerUser.getId());

        Optional<LedgerShare> optionalLedgerShare = checkNonExpiry(ledgerShares);

        if (!ledgerShares.isEmpty() && optionalLedgerShare.isPresent() && ledgerShareRequest.expiryDuration() == null) {
            UUID id = optionalLedgerShare.get().getId();
            return new LedgerShareResponse(id, frontendPath + "/ledger/share/" + id);
        }

        if (ledgerShares.size() == 10) {
            throw new MaximumShareLimitReachedException("You have reached the maximum number of shares.");
        }

        Instant expiry = ledgerShareRequest.expiryDuration() == null
                ? null
                : Instant.now().plus(ledgerShareRequest.expiryDuration());

        LedgerShare ledgerShare = LedgerShare.builder()
                .sharedBy(user)
                .ledgerUser(ledgerUser)
                .expiresAt(expiry)
                .build();

        ledgerShare = ledgerShareRepository.save(ledgerShare);

        UUID id = ledgerShare.getId();
        return new LedgerShareResponse(id, frontendPath + "/ledger/share/" + id);
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
                        entry.getId(), entry.getExpiresAt().toString()))
                .toList();
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
