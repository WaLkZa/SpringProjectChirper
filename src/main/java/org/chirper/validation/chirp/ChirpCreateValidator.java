package org.chirper.validation.chirp;

import org.chirper.domain.models.binding.ChirpCreateBindingModel;
import org.chirper.validation.ValidationConstants;
import org.chirper.validation.annotation.Validator;
import org.springframework.validation.Errors;

@Validator
public class ChirpCreateValidator implements org.springframework.validation.Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return ChirpCreateBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ChirpCreateBindingModel chirpCreateBindingModel = (ChirpCreateBindingModel) o;

        int contentLength = chirpCreateBindingModel.getContent().length();

        if (contentLength < 5 || contentLength > 150) {
            errors.rejectValue(
                    "content",
                    ValidationConstants.CHIRP_CONTENT_LENGTH,
                    ValidationConstants.CHIRP_CONTENT_LENGTH
            );
        }
    }
}
