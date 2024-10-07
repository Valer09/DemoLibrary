package net.myself.DemoLibrary.Unit.Service;

import net.myself.DemoLibrary.Data.Entities.Author;
import net.myself.DemoLibrary.Data.NTO.AuthorNto;
import net.myself.DemoLibrary.Data.NTO.AuthorUpdateNto;
import net.myself.DemoLibrary.Data.Repository.IAuthorRepository;
import net.myself.DemoLibrary.Helper.AuthorService_RepositoryACT;
import net.myself.DemoLibrary.Helper.BookHelper;
import net.myself.DemoLibrary.Model.AuthorUpdate;
import net.myself.DemoLibrary.Service.AuthorService;
import net.myself.DemoLibrary.Service.ServiceResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class AuthorServiceTest
{
	@Autowired
	private AuthorService authorService;
	@MockBean
	private IAuthorRepository authorRepositoryMock;
	
	@Test
	void existsByIsni()
	{
		String isni = "isni";
		when(authorRepositoryMock.existsByIsni(isni)).thenReturn(true);
		Assertions.assertTrue(authorService.existsByIsni(isni));
	}
	
	@ParameterizedTest
	@MethodSource("net.myself.DemoLibrary.Helper.ServiceResultTestCase#addAuthorNto")
	@DisplayName("Test addAuthorNto with various scenarios")
	void addAuthorNto(ServiceResult testCase)
	{
		AuthorNto authorNto = AuthorNto.fromAuthor(BookHelper.getRandomAuthor());
		Author transientAuthor = Author.createTransientAuthor(authorNto);
		
		AuthorService_RepositoryACT.configureUpdateIsbnExpectations(testCase, authorNto, transientAuthor, authorRepositoryMock);
		
		var serviceResponse = authorService.addAuthorNto(authorNto);
		
		Assertions.assertEquals(testCase, serviceResponse.getResult());
		if(!testCase.equals(ServiceResult.OK)) verify(authorRepositoryMock, never()).save(any());
		else
		{
			AuthorNto authorNtoResult = serviceResponse.get();
			Assertions.assertEquals(authorNtoResult.isni(), authorNto.isni());
			Assertions.assertEquals(authorNtoResult.name(), authorNto.name());
			Assertions.assertEquals(authorNtoResult.lastName(), authorNto.lastName());
			Assertions.assertEquals(authorNtoResult.birth(), authorNto.birth());
			verify(authorRepositoryMock, times(1)).save(transientAuthor);
		}
	}
	
	@Test
	void getAllAuthorsNto()
	{
		List<Author> authorList = new ArrayList<>(Arrays.asList(BookHelper.getRandomAuthor(), BookHelper.getRandomAuthor(), BookHelper.getRandomAuthor()));
		when(authorRepositoryMock.findAll()).thenReturn(authorList);
		var serviceResponse = authorService.getAllAuthorsNto();
		
		verify(authorRepositoryMock, times(1)).findAll();
		
		for(int i = 0; i < authorList.size(); i++)
		{
			var authorNto = AuthorNto.fromAuthor(authorList.get(i));
			var authorNtoFromResponse = serviceResponse.get(i);
			Assertions.assertEquals(authorNto.isni(), authorNtoFromResponse.isni());
			Assertions.assertEquals(authorNto.name(), authorNtoFromResponse.name());
			Assertions.assertEquals(authorNto.lastName(), authorNtoFromResponse.lastName());
			Assertions.assertEquals(authorNto.birth(), authorNtoFromResponse.birth());
		}
	}
	
	@ParameterizedTest
	@MethodSource("net.myself.DemoLibrary.Helper.ServiceResultTestCase#findAuthorByIsniNto")
	@DisplayName("Test findAuthorByIsniNto with various scenarios")
	void findAuthorByIsniNto(ServiceResult testCase)
	{
		Author transientAuthor = Author.createTransientAuthor(AuthorNto.fromAuthor(BookHelper.getRandomAuthor()));
		
		AuthorService_RepositoryACT.configureFindAuthorByIsniNtoExpectations(testCase, transientAuthor, authorRepositoryMock);
		
		var serviceResponse = authorService.findAuthorByIsniNto(transientAuthor.getIsni());
		
		Assertions.assertEquals(testCase, serviceResponse.getResult());
		
		verify(authorRepositoryMock, times(1)).findByIsni(transientAuthor.getIsni());
		
		var result = serviceResponse.get();
		
		if(testCase.equals(ServiceResult.OK))
		{
			Assertions.assertEquals(result.isni(), transientAuthor.getIsni());
			Assertions.assertEquals(result.name(), transientAuthor.getName());
			Assertions.assertEquals(result.lastName(), transientAuthor.getLastname());
			Assertions.assertEquals(result.birth(), transientAuthor.getBirth());
		}
		else Assertions.assertNull(result);
		
	}
	
	@ParameterizedTest
	@MethodSource("net.myself.DemoLibrary.Helper.ServiceResultTestCase#findAuthorByIsni")
	@DisplayName("Test findAuthorByIsni with various scenarios")
	void findAuthorByIsni(ServiceResult testCase)
	{
		Author transientAuthor = Author.createTransientAuthor(AuthorNto.fromAuthor(BookHelper.getRandomAuthor()));
		
		AuthorService_RepositoryACT.configureFindAuthorByIsniExpectations(testCase, transientAuthor, authorRepositoryMock);
		
		var serviceResponse = authorService.findAuthorByIsni(transientAuthor.getIsni());
		
		Assertions.assertEquals(testCase, serviceResponse.getResult());
		
		verify(authorRepositoryMock, times(1)).findByIsni(transientAuthor.getIsni());
		
		var result = serviceResponse.get();
		
		if(testCase.equals(ServiceResult.OK))
		{
			Assertions.assertEquals(result.getIsni(), transientAuthor.getIsni());
			Assertions.assertEquals(result.getName(), transientAuthor.getName());
			Assertions.assertEquals(result.getLastname(), transientAuthor.getLastname());
			Assertions.assertEquals(result.getBirth(), transientAuthor.getBirth());
		}
		else Assertions.assertNull(result);
	}
	
	@ParameterizedTest
	@MethodSource("net.myself.DemoLibrary.Helper.ServiceResultTestCase#updateAuthorFromNto")
	@DisplayName("Test updateAuthorFromNto with various scenarios")
	void updateAuthorFromNto(ServiceResult testCase)
	{
		Author author = BookHelper.getRandomAuthor();
		Author authorCopy = new Author(author.getIsni(), author.getName(), author.getLastname(), author.getBirth());
		Author other = BookHelper.getRandomAuthor();
		author.update(new AuthorUpdate(other.getName(), other.getLastname(), other.getBirth()));
		
		AuthorService_RepositoryACT.configureUpdateAuthorFromNtoExpectations(testCase, authorCopy, author, authorRepositoryMock);
		
		var serviceResponse = authorService.updateAuthorFromNto(new AuthorUpdateNto(author.getIsni(), other.getName(), other.getLastname(), other.getBirth()));
		
		Assertions.assertEquals(testCase, serviceResponse.getResult());
		
		if(testCase.equals(ServiceResult.OK))
		{
			verify(authorRepositoryMock, times(1)).save(author);
			AuthorNto result = serviceResponse.get();
			Assertions.assertEquals(result.name(), other.getName());
			Assertions.assertEquals(result.lastName(), other.getLastname());
			Assertions.assertEquals(result.birth(), other.getBirth());
		}
		else
		{
			verify(authorRepositoryMock, never()).save(any());
			Assertions.assertNull(serviceResponse.get());
		}
	}
	
	@ParameterizedTest
	@MethodSource("net.myself.DemoLibrary.Helper.ServiceResultTestCase#updateIsni")
	@DisplayName("Test updateIsni with various scenarios")
	void updateIsni(ServiceResult testCase)
	{
		
		Author author = BookHelper.getRandomAuthor();
		Author editedAuthor = new Author(BookHelper.getRandomAuthor().getIsni(), author.getName(), author.getLastname(), author.getBirth());
		
		AuthorService_RepositoryACT.configureUpdateIsniExpectations(testCase, author, editedAuthor, authorRepositoryMock);
		
		var serviceResponse = authorService.updateIsni(author.getIsni(), editedAuthor.getIsni());
		
		Assertions.assertEquals(testCase, serviceResponse.getResult());
		
		if(testCase.equals(ServiceResult.OK) || testCase.equals(ServiceResult.SERVER_ERROR))
		{
			if(testCase.equals(ServiceResult.OK)) Assertions.assertEquals(serviceResponse.get(), 1);
			verify(authorRepositoryMock, times(1)).updateIsniById(author.getId(), editedAuthor.getIsni());
		}
		else verify(authorRepositoryMock, never()).updateIsniById(any(), any());
	}
	
	@Test
	void findByNameContainingIgnoreCaseNto()
	{
		List<Author> authorList = new ArrayList<>(Arrays.asList(
							new Author("a1","XY","test", LocalDate.now()),
						new Author("b2","test","XY", LocalDate.now()),
						new Author("b3","xy","test", LocalDate.now()),
						new Author("d4","test","xy", LocalDate.now()),
						new Author("e5","containsXY","test", LocalDate.now()),
						new Author("g6","test","containsXY", LocalDate.now()),
						new Author("h7","containsxy","test", LocalDate.now()),
						new Author("i8","test","containsxy", LocalDate.now()),
						new Author("l9","xycontains","test", LocalDate.now()),
						new Author("m10","conXYteins","test", LocalDate.now()),
						new Author("n11","test","conXYteins", LocalDate.now()),
						new Author("o12","conxyteins","test", LocalDate.now()),
						new Author("p13","test","conxyteins", LocalDate.now())
						));
		
		when(authorRepositoryMock.findByNameContainingIgnoreCaseOrLastnameContainingIgnoreCase("xy", "xy")).thenReturn(authorList);
		var serviceResponse = authorService.findByNameContainingIgnoreCaseNto("xy");
		
		verify(authorRepositoryMock, times(1)).findByNameContainingIgnoreCaseOrLastnameContainingIgnoreCase("xy", "xy");
		Assertions.assertNotNull(serviceResponse);
		for(int i = 0; i < serviceResponse.size(); i++)
		{
			AuthorNto authorNto = serviceResponse.get(i);
			Author author = authorList.get(i);
			Assertions.assertEquals(author.getName(), authorNto.name());
			Assertions.assertEquals(author.getIsni(), authorNto.isni());
			Assertions.assertEquals(author.getLastname(), authorNto.lastName());
			Assertions.assertEquals(author.getBirth(), authorNto.birth());
		}
	}
}