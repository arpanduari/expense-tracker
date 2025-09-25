package dev.arpan.expensetracker.ledger;

import dev.arpan.expensetracker.exception.ForbiddenException;
import dev.arpan.expensetracker.exception.ResourceNotFoundException;
import dev.arpan.expensetracker.ledger.dto.*;
import dev.arpan.expensetracker.projection.ILedgerUserEntryDetails;
import dev.arpan.expensetracker.user.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public LedgerService(LedgerUserRepository ledgerUserRepository,
                         LedgerMapper ledgerMapper,
                         LedgerEntryRepository ledgerEntryRepository,
                         LedgerRepository ledgerRepository) {
        this.ledgerUserRepository = ledgerUserRepository;
        this.ledgerMapper = ledgerMapper;
        this.ledgerEntryRepository = ledgerEntryRepository;
        this.ledgerRepository = ledgerRepository;
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

    private <T> void setIfNotNull(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private void setIfNotNullAndNotBlank(Consumer<String> setter, String value) {
        if (value != null && !value.isBlank()) {
            setter.accept(value);
        }
    }

    private boolean notAuthorizedUser(Long ledgerUserId, Long userId) {
        return !ledgerUserRepository.existsByIdAndCreatedBy_Id(ledgerUserId, userId);
    }

    private boolean notAuthorizedLedgerByUser(Long ledgerUserId, Long userId) {
        return !ledgerEntryRepository.existsByLedgerUser_CreatedBy_IdAndId(userId, ledgerUserId);
    }
}
