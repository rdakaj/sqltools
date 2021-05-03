package com.rafaeldakaj.sqltools.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
/**
 * This annotation is used to determine what table a Class Type will go into in a MySQL Schema
 * @param value The name of the table the object belongs in
 */
public @interface SQLTable {
    String value();
}
