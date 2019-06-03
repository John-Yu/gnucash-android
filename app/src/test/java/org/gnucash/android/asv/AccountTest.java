package org.gnucash.android.asv;

import org.gnucash.android.model.Account;
import org.gnucash.android.model.AccountType;
import org.gnucash.android.model.Money;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountTest {

    @Test
    public void testCreatingAccountShouldContainCorrectInformation() {
        //TODO AccountType naar mock, currency erbij gooien
        Account testAccount = mock(Account.class);
        //AccountType testAccountType = mock(AccountType.class);

        String testName = "Klaas";
        String testFullName = "Klaas van der Berg";
        String testDescription = "This is a test description";
        String testCurrency = "USD";
        AccountType testType = AccountType.CASH;

        when(testAccount.getName()).thenReturn(testName);
        when(testAccount.getFullName()).thenReturn(testFullName);
        when(testAccount.getDescription()).thenReturn(testDescription);
        when(testAccount.getCommodity().getCurrencyCode()).thenReturn(testCurrency);
        when(testAccount.getAccountType()).thenReturn(testType);

        assertNotNull(testAccount);
        assertEquals(testAccount.getName(), testName);
        assertEquals(testAccount.getFullName(), testFullName);
        assertEquals(testAccount.getDescription(), testDescription);
        assertEquals(testAccount.getAccountType(), testType);

    }

    @Test
    public void exampleTest2() {
        Account testAccount = mock(Account.class);

        assertNotNull(testAccount);
    }
}
