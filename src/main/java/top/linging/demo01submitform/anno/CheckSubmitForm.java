package top.linging.demo01submitform.anno;

import java.lang.annotation.*;

/**
 * 重复提交
 * @author Linging
 * @version 1.0.0
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckSubmitForm {

    int retrySecond() default 5;    //在时间间隔内不能重复提交，默认5s；内不能重复提交
}
