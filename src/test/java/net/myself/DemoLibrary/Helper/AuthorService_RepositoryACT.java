package net.myself.DemoLibrary.Helper;
import net.myself.DemoLibrary.Data.Entities.Author;
import net.myself.DemoLibrary.Data.NTO.AuthorNto;
import net.myself.DemoLibrary.Data.Repository.IAuthorRepository;
import net.myself.DemoLibrary.Service.ServiceResult;

import java.util.Optional;

import static org.mockito.Mockito.when;

public class AuthorService_RepositoryACT
{
	public static void configureUpdateIsbnExpectations(ServiceResult serviceResult, AuthorNto author, Author transientAuthor, IAuthorRepository authorRepositoryMock)
	{
		if(serviceResult.equals(ServiceResult.OK))
		{
			when(authorRepositoryMock.existsByIsni(author.isni())).thenReturn(false);
			when(authorRepositoryMock.save(transientAuthor)).thenReturn(transientAuthor);
		}
		else
			when(authorRepositoryMock.existsByIsni(author.isni())).thenReturn(true);
	}
	
	public static void configureFindAuthorByIsniNtoExpectations(ServiceResult testCase, Author author, IAuthorRepository authorRepositoryMock)
	{
		if(testCase.equals(ServiceResult.OK))
			when(authorRepositoryMock.findByIsni(author.getIsni())).thenReturn(Optional.of(author));
		else
			when(authorRepositoryMock.findByIsni(author.getIsni())).thenReturn(Optional.empty());
	}
	
	public static void configureFindAuthorByIsniExpectations(ServiceResult testCase, Author author, IAuthorRepository authorRepositoryMock)
	{
		if(testCase.equals(ServiceResult.OK))
			when(authorRepositoryMock.findByIsni(author.getIsni())).thenReturn(Optional.of(author));
		else
			when(authorRepositoryMock.findByIsni(author.getIsni())).thenReturn(Optional.empty());
	}
	public static void configureUpdateAuthorFromNtoExpectations(ServiceResult testCase, Author authorCopy, Author author, IAuthorRepository authorRepositoryMock)
	{
		if(testCase.equals(ServiceResult.NOT_FOUND))
			when(authorRepositoryMock.existsByIsni(authorCopy.getIsni())).thenReturn(false);
		else if(testCase.equals(ServiceResult.SERVER_ERROR))
		{
			when(authorRepositoryMock.existsByIsni(authorCopy.getIsni())).thenReturn(true);
			when(authorRepositoryMock.findByIsni(authorCopy.getIsni())).thenReturn(Optional.empty());
		}
		else
		{
			when(authorRepositoryMock.existsByIsni(authorCopy.getIsni())).thenReturn(true);
			when(authorRepositoryMock.findByIsni(authorCopy.getIsni())).thenReturn(Optional.of(authorCopy));
			when(authorRepositoryMock.save(author)).thenReturn(author);
		}
	}
	
	public static void configureUpdateIsniExpectations(ServiceResult testCase, Author author, Author authorEdited, IAuthorRepository authorRepositoryMock)
	{
		if(testCase.equals(ServiceResult.NOT_FOUND))
			when(authorRepositoryMock.existsByIsni(author.getIsni())).thenReturn(false);
		else if(testCase.equals(ServiceResult.CONFLICT))
		{
			when(authorRepositoryMock.existsByIsni(author.getIsni())).thenReturn(true);
			when(authorRepositoryMock.existsByIsni(authorEdited.getIsni())).thenReturn(true);
		}
		else if(testCase.equals(ServiceResult.SERVER_ERROR))
		{
			when(authorRepositoryMock.existsByIsni(author.getIsni())).thenReturn(true);
			when(authorRepositoryMock.existsByIsni(authorEdited.getIsni())).thenReturn(false);
			when(authorRepositoryMock.findByIsni(author.getIsni())).thenReturn(Optional.of(author));
			when(authorRepositoryMock.updateIsniById(author.getId(), authorEdited.getIsni())).thenReturn(-1);
		}
		else
		{
			when(authorRepositoryMock.existsByIsni(author.getIsni())).thenReturn(true);
			when(authorRepositoryMock.existsByIsni(authorEdited.getIsni())).thenReturn(false);
			when(authorRepositoryMock.findByIsni(author.getIsni())).thenReturn(Optional.of(author));
			when(authorRepositoryMock.updateIsniById(author.getId(), authorEdited.getIsni())).thenReturn(1);
		}
	}
}
