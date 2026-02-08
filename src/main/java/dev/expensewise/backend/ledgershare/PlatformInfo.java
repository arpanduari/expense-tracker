package dev.expensewise.backend.ledgershare;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author arpan
 * @since 2/3/26
 */
@Data
@AllArgsConstructor
public class PlatformInfo {
    private boolean android;
    private boolean ios;
}
