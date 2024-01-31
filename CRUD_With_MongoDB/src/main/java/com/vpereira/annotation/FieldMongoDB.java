package com.vpereira.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>Descrição: </b>Esta é uma anotação que mapeia para uma
 * tabela de banco de dados que esté atributo é uma coluna
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldMongoDB {
    /**
     * <b>Descrição: </b>Nome da coluna
     * <br>
     * <b>Default: </b>Empyt
     */
    String value() default "";
    /**
     * <b>Descrição: </b>Mapeia nome do Metodo Set desse Atributo
     * <br>
     * <b>Default: </b>set+NomeAtributo
     */
    String set() default "";
    /**
     * <b>Descrição: </b>Mapeia nome do Metodo Get desse Atributo
     * <br>
     * <b>Default: </b>get+NomeAtributo
     */
    String get() default "";
}
