package net.myself.DemoLibrary.Controller;

import net.myself.DemoLibrary.Data.NTO.AuthorNto;
import net.myself.DemoLibrary.Data.NTO.BookNto;
import net.myself.DemoLibrary.Data.NTO.BookNtoQl;
import net.myself.DemoLibrary.Infrastructure.AuthorNotFoundException;
import net.myself.DemoLibrary.Service.AuthorService;
import net.myself.DemoLibrary.Service.BookService;
import net.myself.DemoLibrary.Service.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BookGraphQLController
{
    @Autowired
    BookService bookService;

    @Autowired
    AuthorService authorService;

    //NOTE: THIS LOGIC LEADS TO N+1 PROBLEM. THIS IS JUST AN EXPERIMENTAION. THE REAL SOLUTION SHOULD USE DATALOADER
    @QueryMapping
    public List<BookNtoQl> booksByAuthor(@Argument String isni) {
        var serviceResponse = bookService.findByAuthorGql(isni);
        if(serviceResponse.getResult().equals(ServiceResult.NOT_FOUND))
            throw new AuthorNotFoundException("Author not found");

        return serviceResponse.get();
    }

    //NOTE: THIS LOGIC LEADS TO N+1 PROBLEM. THIS IS JUST AN EXPERIMENTAION. THE REAL SOLUTION SHOULD USE DATALOADER
    @SchemaMapping
    public AuthorNto authorNto(BookNtoQl book) {
        var serviceResponse = authorService.findAuthorByIsniNto(book.authorIsni());
        if(serviceResponse.getResult().equals(ServiceResult.NOT_FOUND))
            throw new AuthorNotFoundException("Author not found");

        return serviceResponse.get();
    }
}
