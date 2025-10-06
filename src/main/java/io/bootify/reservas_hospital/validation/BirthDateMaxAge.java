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
@Constraint(validatedBy = BirthDateMaxAgeValidator.class)
public @interface BirthDateMaxAge {
    String message() default "Fecha de nacimiento invalida. No puede ser futura ni mayor a {max} a\u00f1os.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int max() default 95; // years
}
