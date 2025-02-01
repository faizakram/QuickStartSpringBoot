package com.app.utils;

import com.app.dto.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class ResponseUtil {

    private final MessageSource messageSource;


    /**
     * Creates a success response.
     *
     * @param responseCode   The response code for success.
     * @param responseObject The object to include in the response.
     * @return A StandardResponse object.
     */
    public <T> StandardResponse<T> createSuccessResponse(String responseCode, T responseObject, Object... args) {
        return new StandardResponse<>(StringUtils.uncapitalizeAsProperty(responseCode), getMessage("success.description." + responseCode, args), responseObject, true);
    }

    /**
     * Creates an error response.
     *
     * @param responseCode The response code for the error.
     * @param errorDetails Optional error details to include in the response.
     * @return A StandardResponse object.
     */
    public <T> StandardResponse<T> createErrorResponse(String responseCode, T errorDetails, Object... args) {
        return new StandardResponse<>(StringUtils.uncapitalizeAsProperty(responseCode), getMessage("error.description." + responseCode, args), errorDetails, false);
    }

    /**
     * Retrieves a message from the MessageSource based on the current locale.
     *
     * @param key The message key.
     * @return The localized message.
     */
    public String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    /**
     * Retrieves a message from the MessageSource based on the current locale.
     *
     * @return The localized message.
     */
    public String getMessage(MessageSourceResolvable resolvable) {
        return messageSource.getMessage(resolvable, LocaleContextHolder.getLocale());
    }

}
