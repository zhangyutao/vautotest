package annotations.containers.cmd;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import annotations.request.cmd.Line;

/**
 * Only for annotate {@link requests.CommandRequest} and also let {@link Line}
 * can be repeatable using.
 * 
 * @author zhangyutao
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface LineContainer {
	Line[] value();
}
