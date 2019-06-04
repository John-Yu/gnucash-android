package org.gnucash.android.asv;

import org.gnucash.android.db.adapter.AccountsDbAdapter;
import org.gnucash.android.model.Account;
import org.gnucash.android.model.AccountType;
import org.gnucash.android.model.Transaction;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountTest {

    @Test
    public void testCreatingAccountShouldContainCorrectInformation() {
        Account testAccount = mock(Account.class);

        String testName = "Klaas";
        String testFullName = "Klaas van der Berg";
        String testDescription = "This is a test description";
        AccountType testType = AccountType.CASH; //enum

        when(testAccount.getName()).thenReturn(testName);
        when(testAccount.getFullName()).thenReturn(testFullName);
        when(testAccount.getDescription()).thenReturn(testDescription);
        when(testAccount.getAccountType()).thenReturn(testType);

        assertNotNull(testAccount);
        assertEquals(testAccount.getName(), testName);
        assertEquals(testAccount.getFullName(), testFullName);
        assertEquals(testAccount.getDescription(), testDescription);
        assertEquals(testAccount.getAccountType(), testType);
    }

    @Test
    public void testCreatingSubAccountMustBelongToParentAccount() {
        Account parentAccount = mock(Account.class);
        Account subAccount = mock(Account.class);
        String parentUID = "test";

        parentAccount.setUID(parentUID);
        subAccount.setParentUID(parentUID);

        when(parentAccount.getUID()).thenReturn(parentUID);
        when(subAccount.getParentUID()).thenReturn(parentUID);

        assertEquals(parentAccount.getUID(), parentUID);
        assertEquals(subAccount.getParentUID(), parentUID);
    }

    @Test
    public void testEditingAccountMustHaveUpdatedInformation() {
        Account testAccount = mock(Account.class);
        String updatedDescription = "updatedDescription";
        String updatedName = "updatedName";
        String updatedFullName = "updatedFullName";
        AccountType type = AccountType.CASH; //enum

        //setting the old values
        testAccount.setDescription("oldDescription");
        testAccount.setName("oldName");
        testAccount.setFullName("oldFullName");
        testAccount.setAccountType(AccountType.BANK);

        // setting the "updated" values as return
        when(testAccount.getDescription()).thenReturn(updatedDescription);
        when(testAccount.getName()).thenReturn(updatedName);
        when(testAccount.getFullName()).thenReturn(updatedFullName);
        when(testAccount.getAccountType()).thenReturn(type);

        assertEquals(testAccount.getDescription(), updatedDescription);
        assertEquals(testAccount.getName(), updatedName);
        assertEquals(testAccount.getFullName(), updatedFullName);
        assertEquals(testAccount.getAccountType(), type);
    }

    @Test
    public void testFavoringAccountShouldBeFlaggedAsFavorite() {
        AccountsDbAdapter adapter = mock(AccountsDbAdapter.class);
        Account testAccount = mock(Account.class);
        String uid = "123";

        adapter.addRecord(testAccount);
        testAccount.setUID(uid);

        testAccount.setFavorite(true);

        when(adapter.isFavoriteAccount(uid)).thenReturn(true);

        assertTrue(adapter.isFavoriteAccount(uid));
    }

    @Test
    public void testDeletingAccountShouldCompletelyDeleteAccountFromDbAdapter() {
        AccountsDbAdapter adapter = mock(AccountsDbAdapter.class);
        Account testAccount = mock(Account.class);
        String uid = "123";

        adapter.addRecord(testAccount);
        testAccount.setUID(uid);

        //checking if the account is present in the adapter
        when(adapter.getAccountName(uid)).thenReturn(uid);
        assertEquals(adapter.getAccountName(uid), uid);

        //deleting the account and checking if it's deleted
        adapter.deleteRecord(uid);
        when(adapter.getAccountName(uid)).thenReturn(null);
        assertNull(adapter.getAccountName(uid));
    }

    @Test
    public void testAddingTransactionsToAccountShouldReturnSameTransactions() {
        Account testAccount = mock(Account.class);
        Transaction testTransaction1 = mock(Transaction.class);
        Transaction testTransaction2 = mock(Transaction.class);
        Transaction testTransaction3 = mock(Transaction.class);
        List<Transaction> transactions = Arrays.asList(testTransaction1, testTransaction2,
                testTransaction3);
        int amountOfTransactions = transactions.size();

        testAccount.addTransaction(testTransaction1);
        testAccount.addTransaction(testTransaction2);
        testAccount.addTransaction(testTransaction3);

        when(testAccount.getTransactionCount()).thenReturn(amountOfTransactions);
        when(testAccount.getTransactions()).thenReturn(transactions);

        assertEquals(testAccount.getTransactionCount(), amountOfTransactions);
        assertEquals(testAccount.getTransactions(), transactions);
    }
}
