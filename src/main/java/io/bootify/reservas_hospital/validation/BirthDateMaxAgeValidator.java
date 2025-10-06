package io.bootify.reservas_hospital.validation;

import java.time.LocalDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BirthDateMaxAgeValidator implements ConstraintValidator<BirthDateMaxAge, LocalDate> {

    private int max;

    @Override
    public void initialize(BirthDateMaxAge ann) {
        this.max = ann.max();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext ctx) {
        if (value == null) {
            return true; // combine with @NotNull when the field must be present
        }

        LocalDate today = LocalDate.now();
        if (value.isAfter(today)) {
            return false;
        }

        LocalDate oldestAllowed = today.minusYears(max);
        // Reject if the birth date is before the oldest allowed date (older than max years)
        return !value.isBefore(oldestAllowed);
    }
}
