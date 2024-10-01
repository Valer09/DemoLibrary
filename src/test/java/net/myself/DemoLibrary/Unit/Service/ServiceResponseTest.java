package net.myself.DemoLibrary.Unit.Service;

import net.myself.DemoLibrary.Service.ServiceResponse;
import net.myself.DemoLibrary.Service.ServiceResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceResponseTest
{
	@Test
	void createOkTest()
	{
		var ok = ServiceResponse.createOk("");
		Assertions.assertThat(ok.getResult()).isEqualTo(ServiceResult.OK);
	}
	@Test
	void createConflictTest()
	{
		var ok = ServiceResponse.createError(ServiceResult.CONFLICT, "");
		Assertions.assertThat(ok.getResult()).isEqualTo(ServiceResult.CONFLICT);
	}
	
	@Test
	void createNotFoundTest()
	{
		var ok = ServiceResponse.createError(ServiceResult.NOT_FOUND, "");
		Assertions.assertThat(ok.getResult()).isEqualTo(ServiceResult.NOT_FOUND);
	}
}
