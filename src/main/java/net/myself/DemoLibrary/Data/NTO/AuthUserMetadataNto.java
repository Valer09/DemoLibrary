package net.myself.DemoLibrary.Data.NTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthUserMetadataNto(
				String user_name,
				String user_lastname,
				String firstName,
				String lastName){}
