package org.gnucash.android.asv;

import org.gnucash.android.model.Account;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountTest {

    @Test
    public void exampleTest() {
        Account testAccount = mock(Account.class);

        when(testAccount.getName()).thenReturn("Klaas");

        assertEquals(testAccount.getName(), "Klaas");
    }
}
