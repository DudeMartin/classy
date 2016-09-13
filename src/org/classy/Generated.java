package org.classy;

import java.lang.annotation.*;

/**
 * Indicates that the annotated type is automatically generated during class
 * writing. The practical consequence of this is that direct mutation of
 * data whose type is annotated with this annotation is discouraged because
 * it will be ignored and overwritten by the class writer. Loosely speaking,
 * annotated types can be interpreted as read-only.
 *
 * @author Martin Tuskevicius
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Generated {

}