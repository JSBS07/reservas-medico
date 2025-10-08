package io.bootify.reservas_hospital.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class ValidNameValidator implements ConstraintValidator<ValidName, String> {

    private static final Pattern PERMITTED_CHARS = Pattern.compile("^[A-Za-z\\u00C1\\u00C9\\u00CD\\u00D3\\u00DA\\u00E1\\u00E9\\u00ED\\u00F3\\u00FA\\u00D1\\u00F1'\\-]+$");
    private static final Pattern VOWEL_PATTERN = Pattern.compile("[AEIOU\\u00C1\\u00C9\\u00CD\\u00D3\\u00DAaeiou\\u00E1\\u00E9\\u00ED\\u00F3\\u00FA]");

    private int minLength;
    private int maxLength;
    private int minWordLength;
    private int maxWordLength;
    private int maxWords;
    private int maxRepeatedChars;
    private java.util.Set<String> blockedWords = new java.util.HashSet<>();
    private boolean allowRepeatedWords;

    private static final Pattern LAUGH_PATTERN = Pattern.compile("(?i)(ja|je|ji|jo|ju){2,}");

    @Override
    public void initialize(ValidName constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
        this.minWordLength = constraintAnnotation.minWordLength();
        this.maxWordLength = constraintAnnotation.maxWordLength();
        this.maxWords = constraintAnnotation.maxWords();
        this.maxRepeatedChars = constraintAnnotation.maxRepeatedChars();
        this.allowRepeatedWords = constraintAnnotation.allowRepeatedWords();
        for (String word : constraintAnnotation.blockedWords()) {
            if (word != null) {
                String cleaned = normalizeForComparison(word);
                if (!cleaned.isEmpty()) {
                    blockedWords.add(cleaned);
                }
            }
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // @NotBlank/@NotNull should handle required values
        }

        String normalized = normalize(value);
        if (normalized.length() < minLength || normalized.length() > maxLength) {
            return false;
        }

        String[] words = normalized.split(" ");
        if (words.length > maxWords) {
            return false;
        }
        java.util.Set<String> seen = new java.util.HashSet<>();
        for (String word : words) {
            if (word.isBlank()) {
                return false;
            }
            String comparable = normalizeForComparison(word);
            if (!allowRepeatedWords) {
                if (!seen.add(comparable)) {
                    return false; // palabra repetida (no permitido)
                }
            }
            if (blockedWords.contains(comparable)) {
                return false;
            }
            if (word.length() < minWordLength || word.length() > maxWordLength) {
                return false;
            }
            if (!PERMITTED_CHARS.matcher(word).matches()) {
                return false;
            }
            if (!VOWEL_PATTERN.matcher(word).find()) {
                return false;
            }
            if (hasExcessiveRepeatedChars(word)) {
                return false;
            }
            if (isLaughLike(word)) { // evita "jaja", "jeje", etc.
                return false;
            }
        }

        return true;
    }

    private static String normalize(String value) {
        String trimmed = value.trim().replaceAll("\\s+", " ");
        return Normalizer.normalize(trimmed, Normalizer.Form.NFC);
    }

    private String normalizeForComparison(String input) {
        String base = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase();
        return base.replace("'", "");
    }

    private boolean hasExcessiveRepeatedChars(String word) {
        int count = 1;
        for (int i = 1; i < word.length(); i++) {
            if (Character.toLowerCase(word.charAt(i)) == Character.toLowerCase(word.charAt(i - 1))) {
                count++;
                if (count > maxRepeatedChars) {
                    return true;
                }
            } else {
                count = 1;
            }
        }
        return false;
    }

    private boolean isLaughLike(String word) {
        return LAUGH_PATTERN.matcher(word).matches();
    }
}
