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
@Constraint(validatedBy = ColombiaPhoneValidator.class)
public @interface ColombiaPhone {
    String message() default "Telefono invalido. Use movil de 10 digitos (3XXXXXXXXX) o fijo 60X + 7 digitos (60[1-8]XXXXXXX). Se permite prefijo +57 opcional.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
