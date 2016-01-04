package annotations.httprequest.containers;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import annotations.httprequest.Cookie;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })

public @interface CookieContainer {
	Cookie[] value();
}
