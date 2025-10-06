package io.bootify.reservas_hospital.validation;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = TwoWordsNameValidator.class)
public @interface TwoWordsName {
    String message() default "Debe contener exactamente dos nombres o apellidos, solo letras, un espacio entre ellos y no pueden ser iguales.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int minEach() default 2;   // minimum length per word
    int maxEach() default 30;  // maximum length per word
}
