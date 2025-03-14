package ngo.nabarun.test.ngo_nabarun_test.helpers;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

public class ScenarioContext {
	private final Map<ContextKeys, Object> context = new HashMap<>();
	private WebDriver driver;

	public void set(ContextKeys key, Object value) {
		context.put(key, value);
	}

	public <T> T get(ContextKeys key, Class<T> type) {
		return type.cast(context.get(key));
	}

	public boolean containsKey(ContextKeys key) {
		return context.containsKey(key);
	}

	public void reset() {
		context.clear();
		driver = null;
	}

	public enum ContextKeys {
		Last_Window_Handle, Current_Window_Handle, Login_Option, DonationId

	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public WebDriver getDriver() {
		return this.driver;
	}
}
