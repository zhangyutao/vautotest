package annotations.request.email;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import annotations.containers.email.MessageContainer;

/**
 * Only for annotate {@link requests.EmailRequest}
 * 
 * @author zhangyutao
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Repeatable(MessageContainer.class)
public @interface Message {
	String from() default "";

	String to() default "";

	String subject() default "";

	String textBody() default "";

	String[] attachmentsPath() default {};
}
