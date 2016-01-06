package annotations.checkpoint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import basic.EndpointObject;
import basic.ServerComparison;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
// @Repeatable(LineContainer.class)
/**
 * Only for ServerCheckpoint.java
 * 
 * @author Linus
 *
 */
public @interface Elements {

	Class<? extends EndpointObject> expObj();

	Class<? extends EndpointObject> actObj();

	Class<? extends ServerComparison> serverComparison();

}
