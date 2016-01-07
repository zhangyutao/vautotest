package annotations.request.restf;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import annotations.containers.restf.CookieContainer;

/**
 * Only for annotate {@link requests.RestfRequest}
 * 
 * @author zhangyutao
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Repeatable(CookieContainer.class)
public @interface Cookie {

	String key() default "";

	String value() default "";
}
