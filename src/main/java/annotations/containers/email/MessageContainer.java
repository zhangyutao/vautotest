package annotations.containers.email;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import annotations.request.email.Message;

/**
 * Only for annotate {@link requests.EmailRequest} and also let {@link Message}
 * can be repeatable using.
 * 
 * @author zhangyutao
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface MessageContainer {
	Message[] value();
}
