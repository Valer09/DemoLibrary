package net.myself.DemoLibrary.Data.NTO;

import net.myself.DemoLibrary.Data.Entities.Book;

public record BookUpdateNto(Book oldBook, Book newBook){}
