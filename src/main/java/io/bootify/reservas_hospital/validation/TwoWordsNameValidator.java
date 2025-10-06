package io.bootify.reservas_hospital.validation;

import java.text.Normalizer;
import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TwoWordsNameValidator implements ConstraintValidator<TwoWordsName, String> {

    private static final Pattern WORD_PATTERN = Pattern.compile("^[\\p{L}][\\p{L}'-]*$");

    private int minEach;
    private int maxEach;

    @Override
    public void initialize(TwoWordsName ann) {
        this.minEach = ann.minEach();
        this.maxEach = ann.maxEach();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        if (value == null) {
            return true; // combine with @NotBlank/@NotNull when required
        }

        String trimmed = value.trim().replaceAll("\\s+", " ");
        if (!trimmed.contains(" ")) {
            return false;
        }

        String[] parts = trimmed.split(" ");
        if (parts.length != 2) {
            return false;
        }

        String p1 = parts[0];
        String p2 = parts[1];

        if (!WORD_PATTERN.matcher(p1).matches() || !WORD_PATTERN.matcher(p2).matches()) {
            return false;
        }

        if (p1.length() < minEach || p1.length() > maxEach) {
            return false;
        }
        if (p2.length() < minEach || p2.length() > maxEach) {
            return false;
        }

        String n1 = normalize(p1);
        String n2 = normalize(p2);
        return !n1.equals(n2);
    }

    private static String normalize(String s) {
        String normalized = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return normalized.toLowerCase();
    }
}
