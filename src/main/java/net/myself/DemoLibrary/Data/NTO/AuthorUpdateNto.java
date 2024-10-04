package net.myself.DemoLibrary.Data.NTO;

import java.time.LocalDate;

public record AuthorUpdateNto(String isni, String name, String lastName, LocalDate birth){}
