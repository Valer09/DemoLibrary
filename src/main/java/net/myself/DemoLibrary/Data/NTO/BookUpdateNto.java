package net.myself.DemoLibrary.Data.NTO;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;

public record BookUpdateNto(
				@NotNull
				@Size(min = 13, max = 13)
				String isbn,
				@Size(min = 2, max = 41)
				String title,
				@NotNull
				@Size(min = 16, max = 16)
				String authorIsni,
				@PastOrPresent
				LocalDate publishedDate){}
