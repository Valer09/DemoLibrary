package net.myself.DemoLibrary.Data.NTO;

public class TokenNto
{
	String access_token;
	String token_type;
	
	public TokenNto(String access_token, String token_type)
	{
		this.access_token = access_token;
		this.token_type = token_type;
	}
}
