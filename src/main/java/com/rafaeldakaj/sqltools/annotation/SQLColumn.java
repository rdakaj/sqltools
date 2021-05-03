package com.rafaeldakaj.sqltools.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * This annotation is used to determine where the object belongs in the MySQL Schema.
 * @param value The SQL Column that this field belongs to
 * @param unique Determines whether or not the variable is unique (used for ID's)
 * @param searchable Determines the variable to be used when searching for an element in the database. This is to be assigned to usernames/ID's
 * @param credentialType If the resource is credential locked, use this to determine whether or not the field is a username, password, or ID
 */
public @interface SQLColumn {
    String value();
    boolean unique() default false;
    boolean searchable() default false;
}
