package io.bootify.reservas_hospital.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidNameValidator.class)
public @interface ValidName {

    String message() default "Ingrese un nombre valido: solo letras, hasta cuatro palabras, sin repeticiones y cada palabra con vocal.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int minLength() default 2;

    int maxLength() default 60;

    int minWordLength() default 2;

    int maxWordLength() default 30;

    int maxWords() default 4;

    String[] blockedWords() default {"paciente", "anonimo", "test", "prueba"};

    int maxRepeatedChars() default 3;
}
