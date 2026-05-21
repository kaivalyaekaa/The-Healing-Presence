package in.thehealingpresence.enquiry.web;

import in.thehealingpresence.enquiry.domain.TherapyType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Spring MVC converter for form-bound {@link TherapyType} values.
 *
 * <p>Accepts either the enum constant name ({@code HYPNOTHERAPY}) or the
 * human-readable display label ({@code Hypnotherapy}) so the existing JSP
 * {@code <form:option value="Hypnotherapy">} attributes keep working without
 * change. Returns {@code null} for blank input so {@code @NotNull} validation
 * on the DTO surfaces a proper validation error instead of a 400-conversion
 * error.
 */
@Component
public class TherapyTypeConverter implements Converter<String, TherapyType> {

    @Override
    @Nullable
    public TherapyType convert(String source) {
        if (source == null || source.isBlank()) return null;
        // Throw IllegalArgumentException on truly unknown values — surfaces as a 400
        // with the offending value in the message, which is the right behaviour for
        // a tampered request body.
        return TherapyType.fromString(source).orElseThrow(() ->
                new IllegalArgumentException("Unknown therapy type: '" + source + "'"));
    }
}
