package ngo.nabarun.test.ngo_nabarun_test;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;;

@Suite
@IncludeEngines("cucumber")
@SelectPackages("features_ngo_nabarun")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "ngo.nabarun.test.ngo_nabarun_test.step_definations")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty,json:target/cucumber.json,html:target/cucumber.html")
public class RunNGONabarunTest {
}
