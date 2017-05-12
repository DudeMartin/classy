package org.classy;

import java.lang.annotation.*;

/**
 * Indicates that the annotated value is automatically generated during class
 * writing. The practical consequence of this is that direct mutation of data
 * with this annotation is discouraged, since any changes will be overwritten
 * when writing the class. Loosely speaking, annotated values can be interpreted
 * as read-only.
 *
 * @author Martin Tuskevicius
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Generated {

}