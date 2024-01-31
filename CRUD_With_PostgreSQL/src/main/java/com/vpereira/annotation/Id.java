package com.vpereira.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>Descrição: </b>Esta é uma anotação que mapeia para uma tabela de banco de dados que
 * esté atributo é uma PRIMERY KEY
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
}
