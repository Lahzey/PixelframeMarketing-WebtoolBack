package ch.pixelframemarketing.webtool.api.interceptor;

import ch.pixelframemarketing.webtool.data.entity.User;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Secure {
    User.Role[] roles() default {};
}
