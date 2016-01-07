package annotations.containers.restf;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import annotations.request.restf.Header;

/**
 * Only for annotate {@link requests.RestfRequest} and also let {@link Header}
 * can be repeatable using.
 * 
 * @author zhangyutao
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface HeaderContainer {
	Header[] value();
}
