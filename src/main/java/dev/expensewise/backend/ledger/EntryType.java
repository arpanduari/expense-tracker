package dev.expensewise.backend.ledger;

/**
 * @author arpan
 * @since 9/22/25
 */
public enum EntryType {
    DEBIT(-1), CREDIT(1);
    private final int sign;

    EntryType(int sign) {
        this.sign = sign;
    }

    public int getSign() {
        return sign;
    }
}
