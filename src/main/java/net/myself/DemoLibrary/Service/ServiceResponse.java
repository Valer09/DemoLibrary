package net.myself.DemoLibrary.Service;

import org.apache.logging.log4j.util.Strings;

public class ServiceResponse<T>
{
	
	private ServiceResponse(T objectResult, ServiceResult result, String message)
	{
		this.objectResult = objectResult;
		this.result = result;
		this.message = message;
	}
	private T objectResult;
	private ServiceResult result;
	private String message;
	
	public static <T> ServiceResponse<T> createOk(T get)
	{
		return new ServiceResponse<T>(get, ServiceResult.OK, Strings.EMPTY);
	}
	
	public static <T> ServiceResponse<T> createError(ServiceResult serviceResult, String error)
	{
		return new ServiceResponse<T>(null, serviceResult, error);
	}
	
	public T get()
	{
		return objectResult;
	}
	
	public ServiceResult getResult()
	{
		return result;
	}
	
	public String getMessage()
	{
		return message;
	}
}
