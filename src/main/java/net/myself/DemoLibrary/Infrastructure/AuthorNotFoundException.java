package net.myself.DemoLibrary.Infrastructure;

public class AuthorNotFoundException extends RuntimeException
{
    public AuthorNotFoundException(String message) {
        super(message);
    }
}
