package net.myself.DemoLibrary.Service;

import org.apache.logging.log4j.util.Strings;

import java.io.Serializable;

public class ServiceResponse<T> implements Serializable
{
	private final T objectResult;
	private final ServiceResult result;
	private final String message;
	
	private ServiceResponse(T objectResult, ServiceResult result, String message)
	{
		this.objectResult = objectResult;
		this.result = result;
		this.message = message;
	}
	
	public static <T> ServiceResponse<T> createOk(T get)
	{
		return new ServiceResponse<>(get, ServiceResult.OK, Strings.EMPTY);
	}
	
	public static <T> ServiceResponse<T> createError(ServiceResult serviceResult, String error)
	{
		return new ServiceResponse<>(null, serviceResult, error);
	}
	
	public T get()
	{
		return objectResult;
	}
	
	public ServiceResult getResult()
	{
		return result;
	}
	public boolean isOk()
	{
		return result.equals(ServiceResult.OK);
	}
}
