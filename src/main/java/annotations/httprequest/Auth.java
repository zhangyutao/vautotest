package annotations.httprequest;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
// This is used for HttpRequest.java only
public @interface Auth {
	String type() default "";

	String host() default "";

	String realm() default "";

	String username() default "";

	String password() default "";

	boolean preemptive() default false;
}
