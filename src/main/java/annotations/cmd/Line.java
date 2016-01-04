package annotations.cmd;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import annotations.cmd.containers.LineContainer;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Repeatable(LineContainer.class)
/**
 * Only for Command.java
 * 
 * @author Linus
 *
 */
public @interface Line {
	String value() default "";
}
