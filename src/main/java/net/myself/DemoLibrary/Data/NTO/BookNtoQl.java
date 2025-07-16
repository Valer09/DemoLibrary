package net.myself.DemoLibrary.Data.NTO;

import net.myself.DemoLibrary.Data.Entities.Book;

import java.time.LocalDate;

public record BookNtoQl(
        String isbn,
        String title,
        LocalDate publishedDate,
        String state,
        String authorIsni) {
    public static BookNtoQl fromBook(Book book)
    {
        return new BookNtoQl(book.getIsbn(), book.getTitle(), book.getPublishedDate(), book.getState(), book.getAuthor().getIsni());
    }
}
