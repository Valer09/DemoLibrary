package net.myself.DemoLibrary.Data.NTO;

import java.time.LocalDate;

public record BookUpdateNto(String isbn, String title, String authorIsni, LocalDate publishedDate){}
