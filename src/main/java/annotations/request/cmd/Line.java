package annotations.request.cmd;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import annotations.containers.cmd.LineContainer;

/**
 * Only for annotate {@link requests.CommandRequest}
 * 
 * @author zhangyutao
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Repeatable(LineContainer.class)
public @interface Line {
	String value() default "";
}
