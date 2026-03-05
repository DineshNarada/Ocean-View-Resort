package com.orrs.help;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HelpSystemTest {

    @Test
    void testDisplayHelp() {
        // Test that displayHelp doesn't throw exception
        assertDoesNotThrow(() -> HelpSystem.displayHelp());
    }

    @Test
    void testDisplayQuickTips() {
        // Test that displayQuickTips doesn't throw exception
        assertDoesNotThrow(() -> HelpSystem.displayQuickTips());
    }
}