package annotations.request.e2evalidation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import basic.Comparison;
import basic.e2evalidation.EndpointObject;

/**
 * Only for annotate {@link requests.E2EValidationRequest}
 * 
 * @author zhangyutao
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Items {

	Class<? extends EndpointObject> expObj();

	Class<? extends EndpointObject> actObj();

	Class<? extends Comparison> comparison();

}
