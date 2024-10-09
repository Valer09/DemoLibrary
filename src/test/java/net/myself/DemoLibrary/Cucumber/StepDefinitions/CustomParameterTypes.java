package net.myself.DemoLibrary.Cucumber.StepDefinitions;

import io.cucumber.java.ParameterType;

public class CustomParameterTypes
{
	@ParameterType(".*")
	public String stringParam(String value) {
		return value;
	}
}
