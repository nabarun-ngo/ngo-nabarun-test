package ngo.nabarun.test.ngo_nabarun_test.helpers;

import java.util.HashMap;
import java.util.Map;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Page;

public class ScenarioContext {
	private final Map<ContextKeys, Object> context = new HashMap<>();
	private final Map<String, Object> customValues = new HashMap<>();
	private Page page;
	private APIRequestContext requestContext;

	public void set(ContextKeys key, Object value) {
		context.put(key, value);
	}

	public <T> T get(ContextKeys key, Class<T> type) {
		return type.cast(context.get(key));
	}

	public void setCustomValue(String key, Object value) {
		customValues.put(key, value);
	}

	public <T> T getCustomValue(String key, Class<T> type) {
		return type.cast(customValues.get(key));
	}

	public Map<String, Object> getCustomValues() {
		return customValues;
	}

	public boolean containsKey(ContextKeys key) {
		return context.containsKey(key);
	}

	public boolean containsCustomKey(String key) {
		return customValues.containsKey(key);
	}

	public void reset() {
		context.clear();
		customValues.clear();
		page = null;
		requestContext = null;
	}

	public enum ContextKeys {
		Login_Option, Login_Id,
		Login_Id_Type

	}

	public void setPage(Page page) {
		this.page = page;
		ngo.nabarun.test.ngo_nabarun_test.utils.StepState.setPage(page);
	}

	public Page getPage() {
		return this.page;
	}

	public void setRequestContext(APIRequestContext requestContext) {
		this.requestContext = requestContext;
	}

	public APIRequestContext getRequestContext() {
		return this.requestContext;
	}
}
