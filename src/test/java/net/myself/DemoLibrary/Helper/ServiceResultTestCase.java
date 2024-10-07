package net.myself.DemoLibrary.Helper;

import net.myself.DemoLibrary.Service.ServiceResult;
import org.springframework.http.HttpStatus;

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
}
