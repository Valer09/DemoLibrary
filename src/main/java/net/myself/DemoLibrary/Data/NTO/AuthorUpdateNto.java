package net.myself.DemoLibrary.Data.NTO;
import jakarta.validation.constraints.Size;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
public record AuthorUpdateNto(
				@Size(min = 16, max = 16)
				String isni,
				@Size(min = 2, max = 20)
				String name,
				@Size(min = 2, max = 20)
				String lastName,
				LocalDate birth){}
