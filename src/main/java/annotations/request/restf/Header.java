package annotations.request.restf;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import annotations.containers.restf.HeaderContainer;

/**
 * Only for annotate {@link requests.RestfRequest}
 * 
 * @author zhangyutao
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Repeatable(HeaderContainer.class)
public @interface Header {
	String key() default "";

	String value() default "";
}
