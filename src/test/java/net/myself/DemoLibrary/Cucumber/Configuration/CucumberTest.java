package net.myself.DemoLibrary.Cucumber.Configuration;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite //equivalent of old @Cucumber
@IncludeEngines("cucumber")
@SelectClasspathResource("Features") //where features file are
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "net.myself.DemoLibrary.Cucumber.StepDefinitions") //where the glue classes are located
public class CucumberTest
{
}
