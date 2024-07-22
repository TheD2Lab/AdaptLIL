/**
 * Annotation class used to ignore auto conversion to WEKA attributes.
 */
package adaptlil.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

public @interface IgnoreWekaAttribute {

}