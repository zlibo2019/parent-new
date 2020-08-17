package com.weds.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Logs {
	String value() default "";

	boolean valid() default true;

	public enum Position {
		Controller, Service, DAO
	};

	Position logsPostion() default Position.Controller;
}
