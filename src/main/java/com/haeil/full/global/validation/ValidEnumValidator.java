package com.haeil.full.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidEnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Class<? extends Enum<?>> enumClass;
    private boolean ignoreCase;

    @Override
    public void initialize(ValidEnum annotation) {
        this.enumClass = annotation.enumClass();
        this.ignoreCase = annotation.ignoreCase();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null 값은 @NotNull 등 다른 어노테이션에서 처리
        }

        Enum<?>[] enumConstants = enumClass.getEnumConstants();
        if (enumConstants == null) {
            return false;
        }

        for (Enum<?> enumConstant : enumConstants) {
            if (ignoreCase) {
                if (enumConstant.name().equalsIgnoreCase(value)) {
                    return true;
                }
            } else {
                if (enumConstant.name().equals(value)) {
                    return true;
                }
            }
        }

        return false;
    }
}
