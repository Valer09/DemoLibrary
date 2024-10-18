package net.myself.DemoLibrary.Cucumber.StepDefinitions;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static java.lang.ThreadLocal.withInitial;

@Component
public class TestContextCache
{
	private final ThreadLocal<Map<String, Object>> testContexts = withInitial(HashMap::new);
	
	public Object getProperty(String key) {
		return get(key);
	}
	
	public void setProperty(String key, Object value) {
		set(key, value);
	}
	
	public void reset() {
		testContexts.get().clear();
	}
	
	private Object get(String key) {
		return testContexts.get().get(key);
	}
	
	private void set(String name, Object object) {
		testContexts.get().put(name, object);
	}
}
