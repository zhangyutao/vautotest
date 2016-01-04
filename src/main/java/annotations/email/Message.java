package annotations.email;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import annotations.email.containers.MessageContainer;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Repeatable(MessageContainer.class)
// use for DefaultEmailFormat.java only
public @interface Message {
	String from() default "";

	String to() default "";

	String subject() default "";

	String textBody() default "";

	String[] attachmentsPath() default {};
}
