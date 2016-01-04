package annotations.httprequest;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import annotations.httprequest.containers.CookieContainer;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Repeatable(CookieContainer.class)
// This is used for HttpRequest.java only
public @interface Cookie {

	String key() default "";

	String value() default "";
}
