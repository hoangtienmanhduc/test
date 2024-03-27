/**
 * 
 */
package vn.techres.order.online.configuration.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.stream.IntStream;

public class ExistInValidator implements ConstraintValidator<ExistIn, Integer> {

	private int[] values;

	@Override
	public void initialize(ExistIn constraintAnnotation) {
		this.values = constraintAnnotation.values();
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if (IntStream.of(this.values).anyMatch(e -> e == value)) {
			return true;
		}
		return false;
	}

}
