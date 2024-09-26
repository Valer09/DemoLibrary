package net.myself.DemoLibrary.Controller;

import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Repository.IBookRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class BookControllerTest
{
	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	private IBookRepository bookRepository;
	
	@InjectMocks
	private BookController bookController;
	
	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
	}
	
	@Test
	void getAllBooks() throws Exception
	{
		Long bookId = 1L;
		List<Book> bookList = new ArrayList<>(Arrays.asList(
						new Book(bookId, "Test Book", "Author",  "123455", LocalDate.now()),
						new Book(bookId+1, "Test Book1", "Author1",  "123456", LocalDate.now()),
						new Book(bookId+2, "Test Book2", "Author2",  "123457", LocalDate.now()),
						new Book(bookId+3, "Test Book3", "Author3",  "123458", LocalDate.now())));
		
		when(bookRepository.findAll()).thenReturn(bookList);
		
		mockMvc.perform(get("/books")
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(4)))
						.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(bookId))
						.andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(bookId + 1))
						.andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(bookId + 2))
						.andExpect(MockMvcResultMatchers.jsonPath("$[3].id").value(bookId + 3))
						.andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Test Book"))
						.andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Test Book1"))
						.andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value("Test Book2"))
						.andExpect(MockMvcResultMatchers.jsonPath("$[3].title").value("Test Book3"))
						.andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value("Author"))
						.andExpect(MockMvcResultMatchers.jsonPath("$[1].author").value("Author1"))
						.andExpect(MockMvcResultMatchers.jsonPath("$[2].author").value("Author2"))
						.andExpect(MockMvcResultMatchers.jsonPath("$[3].author").value("Author3"))
						.andExpect(MockMvcResultMatchers.jsonPath("$[0].isbn").value("123455"))
						.andExpect(MockMvcResultMatchers.jsonPath("$[1].isbn").value("123456"))
						.andExpect(MockMvcResultMatchers.jsonPath("$[2].isbn").value("123457"))
						.andExpect(MockMvcResultMatchers.jsonPath("$[3].isbn").value("123458"));
	}
	
	@Disabled
	@Test
	void findByIsbn()
	{
	}
	
	@Disabled
	@Test
	void findByTitle()
	{
	}
	
	@Disabled
	@Test
	void searchByTitle()
	{
	}
	
	@Disabled
	@Test
	void addBook()
	{
	}
	
	@Disabled
	@Test
	void deleteBookByIsbn()
	{
	}
	
	@Disabled
	@Test
	void deleteBookById()
	{
	}
	
	@Disabled
	@Test
	void deleteBook()
	{
	}
	
	@Disabled
	@Test
	void updateBook()
	{
	}
}