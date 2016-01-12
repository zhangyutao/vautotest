package annotations.request.scenario;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import basic.scenario.Scenario;
import basic.scenario.ScenarioIO;

/**
 * Only for annotate {@link requests.ScenarioRequest}
 * 
 * @author Linus
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Properties {

	Class<? extends Scenario> scenario();

	Class<? extends ScenarioIO>[] inputDatas();

	int iteration() default 1;

	boolean isConcurrent() default false;

	int timeout() default 180000;

}
