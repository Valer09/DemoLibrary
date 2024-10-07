package net.myself.DemoLibrary.Helper;

import net.myself.DemoLibrary.Service.ServiceResult;

import java.util.stream.Stream;

public class ServiceResultTestCase
{
	public static Stream<ServiceResult> updateIsbn()
	{
		return Stream.of(
						ServiceResult.OK,
						ServiceResult.NOT_FOUND,
						ServiceResult.CONFLICT,
						ServiceResult.SERVER_ERROR
		);
	}
	
	public static Stream<ServiceResult> addAuthorNto()
	{
		return Stream.of(
						ServiceResult.OK,
						ServiceResult.CONFLICT
		);
	}
	
	public static Stream<ServiceResult> findAuthorByIsniNto()
	{
		return Stream.of(
						ServiceResult.OK,
						ServiceResult.NOT_FOUND
		);
	}
	
	public static Stream<ServiceResult> findAuthorByIsni()
	{
		return Stream.of(
						ServiceResult.OK,
						ServiceResult.NOT_FOUND
		);
	}
	
	public static Stream<ServiceResult> updateAuthorFromNto()
	{
		return Stream.of(
						ServiceResult.OK,
						ServiceResult.NOT_FOUND,
						ServiceResult.SERVER_ERROR
		);
	}
	
	public static Stream<ServiceResult> updateIsni()
	{
		return Stream.of(
						ServiceResult.OK,
						ServiceResult.NOT_FOUND,
						ServiceResult.CONFLICT,
						ServiceResult.SERVER_ERROR
		);
	}
}
