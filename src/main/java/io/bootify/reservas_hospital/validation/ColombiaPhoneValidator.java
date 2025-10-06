package io.bootify.reservas_hospital.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ColombiaPhoneValidator implements ConstraintValidator<ColombiaPhone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // combine with @NotBlank if the field is required
        }

        // Remove blanks, dashes and parentheses that are commonly used when typing phone numbers
        String digits = value.replaceAll("[\\s\\-()]", "");

        // Optional +57 prefix
        if (digits.startsWith("+57")) {
            digits = digits.substring(3);
        }

        // Mobile numbers: 10 digits starting with 3
        if (digits.matches("^3\\d{9}$")) {
            return true;
        }

        // Landline (new plan): 60 + [1-8] + 7 digits (total 10)
        if (digits.matches("^60[1-8]\\d{7}$")) {
            return true;
        }

        return false;
    }
}
