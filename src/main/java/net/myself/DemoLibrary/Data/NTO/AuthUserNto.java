package net.myself.DemoLibrary.Data.NTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthUserNto(
	AuthUserMetadataNto user_metadata,
	boolean email_verified,
	String name,
	String email,
	List<AuthIdentitiesNto> identities){}
