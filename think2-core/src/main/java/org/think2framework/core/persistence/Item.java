package org.think2framework.core.persistence;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhoubin on 16/9/12. 参数选项
 */
@Target({ METHOD, FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = Items.class)
public @interface Item {

	String key(); // 实际值

	String value(); // 显示名称

}
