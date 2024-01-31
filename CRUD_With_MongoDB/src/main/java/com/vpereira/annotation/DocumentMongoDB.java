package com.vpereira.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>Descrição: </b>Esta é uma anotação que mapeia para um banco de dados
 * que essa classe representa uma tabela
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DocumentMongoDB {

    /**
     * <b>Descrição: </b>Nome da tabela
     * <br>
     * <b>Default: </b>Nome da Classe
     */
    String value() default "";
}
