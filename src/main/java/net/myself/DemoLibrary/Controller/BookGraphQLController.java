package net.myself.DemoLibrary.Controller;

import net.myself.DemoLibrary.Data.NTO.BookNto;
import net.myself.DemoLibrary.Service.BookService;
import net.myself.DemoLibrary.Service.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BookGraphQLController
{
    @Autowired
    BookService bookService;

    @QueryMapping
    public List<BookNto> booksByAuthor(@Argument String isni) {
        var serviceResponse = bookService.findByAuthor(isni);
        if(serviceResponse.getResult().equals(ServiceResult.NOT_FOUND)) {
            return List.of();
        }
        return serviceResponse.get();
    }
}
