package net.myself.DemoLibrary.Model;
import net.myself.DemoLibrary.Data.Entities.Author;

import java.time.LocalDate;

public record BookUpdate(String title, Author author, LocalDate publishedDate){}
