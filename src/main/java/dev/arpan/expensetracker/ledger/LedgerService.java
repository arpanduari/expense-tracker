package dev.arpan.expensetracker.ledger;

import dev.arpan.expensetracker.exception.ForbiddenException;
import dev.arpan.expensetracker.exception.ResourceNotFoundException;
import dev.arpan.expensetracker.ledger.dto.*;
import dev.arpan.expensetracker.projection.ILedgerUserEntryDetails;
import dev.arpan.expensetracker.user.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author arpan
 * @since 9/22/25
 */
@Service
public class LedgerService {
    private final LedgerUserRepository ledgerUserRepository;
    private final LedgerMapper ledgerMapper;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final LedgerRepository ledgerRepository;
    private final LedgerShareRepository ledgerShareRepository;

    @Value("${app.frontend.path}")
    private String frontendPath;

    public LedgerService(LedgerUserRepository ledgerUserRepository,
                         LedgerMapper ledgerMapper,
                         LedgerEntryRepository ledgerEntryRepository,
                         LedgerRepository ledgerRepository,
                         LedgerShareRepository ledgerShareRepository) {
        this.ledgerUserRepository = ledgerUserRepository;
        this.ledgerMapper = ledgerMapper;
        this.ledgerEntryRepository = ledgerEntryRepository;
        this.ledgerRepository = ledgerRepository;
        this.ledgerShareRepository = ledgerShareRepository;
    }

    public LedgerUserResponse createLedgerUser(User user, LedgerUserRequest ledgerUserRequest) {
        LedgerUser ledgerUser = ledgerMapper.toLedgerUser(ledgerUserRequest);
        ledgerUser.setCreatedBy(user);
        ledgerUser = ledgerUserRepository.save(ledgerUser);
        return ledgerMapper.toLedgerUserResponse(ledgerUser);
    }

    public LedgerEntryResponse createLedgerEntry(LedgerEntryRequest entryRequest) {
        LedgerUser ledgerUser = ledgerUserRepository.findById(entryRequest.ledgerUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Ledger User", "ID", entryRequest.ledgerUserId() + " "));
        LedgerEntry newEntry = ledgerMapper.toLedgerEntry(entryRequest);
        newEntry.setLedgerUser(ledgerUser);
        newEntry = ledgerEntryRepository.save(newEntry);
        return ledgerMapper.toLedgerEntryResponse(newEntry);
    }

    public List<LedgerUserEntryResponse> getAllContacts(User user) {
        List<ILedgerUserEntryDetails> contacts = ledgerRepository.findLedgerUserEntryDetailsByUserId(user.getId());
        return contacts.stream()
                .map(ledgerMapper::toLedgerUserEntryResponse)
                .toList();
    }

    public List<LedgerEntryResponse> getAllLedgerEntries(User user, Long ledgerUserId) {
        if (notAuthorizedUser(ledgerUserId, user.getId())) {
            throw new ForbiddenException("You are not authorized to access this.");
        }
        return ledgerEntryRepository.findByLedgerUser_IdOrderByUpdatedAtDescCreatedDateDesc(ledgerUserId)
                .stream()
                .map(ledgerMapper::toLedgerEntryResponse)
                .toList();
    }

    @Transactional
    public void deleteLedgerEntry(User user, Long ledgerEntryId) {
        if (notAuthorizedUser(ledgerEntryId, user.getId())) {
            throw new ForbiddenException("You are not authorized to delete this.");
        }
        ledgerEntryRepository.deleteById(ledgerEntryId);
    }

    @Transactional
    public void deleteLedgerUser(User user, Long ledgerUserId) {
        if (notAuthorizedUser(ledgerUserId, user.getId())) {
            throw new ForbiddenException("You are not authorized to delete this.");
        }
        ledgerUserRepository.deleteById(ledgerUserId);
    }

    public LedgerUserResponse updateLedgerUser(User user, Long ledgerUserId, LedgerUserRequest ledgerUserRequest) {
        if (notAuthorizedUser(ledgerUserId, user.getId())) {
            throw new ForbiddenException("You are not authorized to update this.");
        }
        LedgerUser ledgerUser = ledgerUserRepository.findById(ledgerUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Ledger User", "ID", ledgerUserId + " "));
        setIfNotNullAndNotBlank(ledgerUser::setName, ledgerUserRequest.name());
        setIfNotNullAndNotBlank(ledgerUser::setEmail, ledgerUserRequest.email());
        ledgerUser = ledgerUserRepository.save(ledgerUser);
        return ledgerMapper.toLedgerUserResponse(ledgerUser);
    }

    public LedgerEntryResponse getLedgerEntry(User user, Long ledgerEntryId) {
        if (notAuthorizedLedgerByUser(ledgerEntryId, user.getId())) {
            throw new ForbiddenException("You are not authorized to access this.");
        }
        return ledgerEntryRepository.findById(ledgerEntryId)
                .map(ledgerMapper::toLedgerEntryResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Ledger Entry", "ID", ledgerEntryId + " "));
    }

    public LedgerEntryResponse updateLedgerEntry(Long ledgerId, LedgerEntryRequest ledgerEntryRequest, User user) {
        LedgerEntry ledgerEntry = ledgerEntryRepository.findById(ledgerId)
                .orElseThrow(() -> new ResourceNotFoundException("Ledger Entry", "id", String.valueOf(ledgerId)));
        setIfNotNullAndNotBlank(ledgerEntry::setDescription, ledgerEntryRequest.description());
        setIfNotNull(ledgerEntry::setType, ledgerEntryRequest.type());
        setIfNotNull(ledgerEntry::setAmount, ledgerEntryRequest.amount());
        ledgerEntry = ledgerEntryRepository.save(ledgerEntry);
        return ledgerMapper.toLedgerEntryResponse(ledgerEntry);
    }

    public LedgerShareResponse shareLedger(LedgerShareRequest ledgerShareRequest, User user) {
        LedgerUser ledgerUser = ledgerUserRepository.findById(ledgerShareRequest.ledgerUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Ledger User", "ID", ledgerShareRequest.ledgerUserId() + " "));

        if (notAuthorizedUser(ledgerUser.getId(), user.getId())) {
            throw new ForbiddenException("You are not authorized to share this.");
        }

        Optional<LedgerShare> ledgerShares = ledgerShareRepository.findBySharedByIdAndLedgerUserId(user.getId(), ledgerUser.getId());

        if (ledgerShares.isPresent()) {
            return new LedgerShareResponse(frontendPath + "/ledger/share/" + ledgerShares.get().getToken());
        }
        LedgerShare ledgerShare = LedgerShare.builder()
                .sharedBy(user)
                .ledgerUser(ledgerUser)
                .token(UUID.randomUUID().toString())
                .build();
        ledgerShare = ledgerShareRepository.save(ledgerShare);
        return new LedgerShareResponse(frontendPath + "/ledger/share/" + ledgerShare.getToken());
    }

    public List<LedgerEntryResponse> getSharedLedger(UUID token) {
        LedgerShare ledgerShare = ledgerShareRepository.findByToken(token.toString())
                .orElseThrow(() -> new ResourceNotFoundException("Shared Entries", "token", token.toString()));
        return getAllLedgerEntries(ledgerShare.getSharedBy(), ledgerShare.getLedgerUser().getId());
    }


    private <T> void setIfNotNull(Consumer<T> setter, T value) {
        Optional.ofNullable(value).ifPresent(setter);
    }

    private void setIfNotNullAndNotBlank(Consumer<String> setter, String value) {
        Optional.ofNullable(value)
                .filter(v -> !v.isBlank())
                .ifPresent(setter);
    }

    private boolean notAuthorizedUser(Long ledgerUserId, Long userId) {
        return !ledgerUserRepository.existsByIdAndCreatedBy_Id(ledgerUserId, userId);
    }

    private boolean notAuthorizedLedgerByUser(Long ledgerUserId, Long userId) {
        return !ledgerEntryRepository.existsByLedgerUser_CreatedBy_IdAndId(userId, ledgerUserId);
    }
}
