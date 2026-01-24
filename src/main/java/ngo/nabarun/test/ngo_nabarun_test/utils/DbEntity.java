package ngo.nabarun.test.ngo_nabarun_test.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DbEntity {
    String value();
}
