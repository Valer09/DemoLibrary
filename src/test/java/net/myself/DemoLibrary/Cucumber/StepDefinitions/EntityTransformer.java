package net.myself.DemoLibrary.Cucumber.StepDefinitions;

import io.cucumber.java.DataTableType;
import net.myself.DemoLibrary.Data.NTO.AuthorNto;
import net.myself.DemoLibrary.Service.ServiceResponse;
import net.myself.DemoLibrary.Service.ServiceResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class EntityTransformer
{
	public static ServiceResponse getServiceResponse(String serviceOutcome, Object obj, String message)
	{
		var serviceResult = ServiceResult.valueOf(serviceOutcome);
		if(serviceResult.equals(ServiceResult.OK)) return ServiceResponse.createOk(obj);
		return ServiceResponse.createError(serviceResult, message);
	}
	
	@DataTableType
	public AuthorNto authorNtoEntityTransformer(Map<String, String> row)
	{
		String name = row.get("Name");
		String lastName = row.get("LastName");
		
		return new AuthorNto(row.get("Isni"), name, lastName, name+ " " +lastName, LocalDate.parse(row.get("Birth"), DateTimeFormatter.ofPattern("mm/DD/yyyy")));
	}
}
