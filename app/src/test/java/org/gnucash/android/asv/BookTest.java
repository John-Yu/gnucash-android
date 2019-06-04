package org.gnucash.android.asv;

import org.gnucash.android.model.Book;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookTest {

    @Test
    public void testWhenChangingToNewBookTheNewBookShouldBeActive() {
        Book oldBook = mock(Book.class);
        Book newBook = mock(Book.class);

        //the old book currently activated
        oldBook.setActive(true);
        newBook.setActive(false);
        when(oldBook.isActive()).thenReturn(true);
        when(newBook.isActive()).thenReturn(false);
        assertTrue(oldBook.isActive());
        assertFalse(newBook.isActive());

        //the new book is activated
        oldBook.setActive(false);
        newBook.setActive(true);
        when(oldBook.isActive()).thenReturn(false);
        when(newBook.isActive()).thenReturn(true);
        assertFalse(oldBook.isActive());
        assertTrue(newBook.isActive());
    }
}
