package com.barterbay.app.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Custom annotation which will inject array of values into your object
 * Chain of responsibility implementation
 * BPP for this annotation -> {@link com.barterbay.app.bpp.InjectListBeanPostProcessor}
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface InjectList {
  Class<?>[] value();
}
