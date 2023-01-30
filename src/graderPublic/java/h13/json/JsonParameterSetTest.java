package h13.json;

import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
//    @CartesianArgumentsSource(JsonArgumentSetProvider.class)
@ArgumentsSource(JsonArgumentSetProvider.class)
public @interface JsonParameterSetTest {

    /**
     * The path to the JSON file containing the test data.
     * @return the path to the JSON file containing the test data.
     */
    String value();

    /**
     * The name of the field in the test class that contains a map of custom converters.
     * @return the name of the field in the test class that contains a map of custom converters.
     */
    String customConverters() default "";

}
