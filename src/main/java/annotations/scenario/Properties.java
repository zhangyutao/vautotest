package annotations.scenario;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import basic.Scenario;
import basic.ScenarioIO;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })

/**
 * Only for ServerCheckpoint.java
 * 
 * @author Linus
 *
 */
public @interface Properties {

	Class<? extends Scenario> scenarioClass();
	Class<? extends ScenarioIO> scenarioInput();
	int iteration() default 1;
	boolean isConcurrent() default false; 
	int timeout() default 180000;
	
}
