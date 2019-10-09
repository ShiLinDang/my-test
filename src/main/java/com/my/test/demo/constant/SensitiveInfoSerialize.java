package com.my.test.demo.constant;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.my.test.demo.annotation.Sensitive;
import com.my.test.demo.annotation.SensitiveInfo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class SensitiveInfoSerialize extends JsonSerializer<String> implements ContextualSerializer {

    private SensitiveType type;

    @Override
    public void serialize(String value,
                          final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException {
        try {
            //获取controller方法
            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            ApplicationContext webApplicationContext = (WebApplicationContext) request.getServletContext().getAttribute(
                    WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            HandlerExecutionChain handler = webApplicationContext.getBean(HandlerMapping.class).getHandler(request);
            HandlerMethod method = (HandlerMethod) handler.getHandler();
            // 获取方法上的（Sensitive）注解
            Sensitive sensitive = method.getMethodAnnotation(Sensitive.class);
            if (sensitive == null) {
                // 如果未获取到方法上的注解，则获取类上的注解
                sensitive = method.getBeanType().getAnnotation(Sensitive.class);
            }
            // 替换
            if (sensitive != null && sensitive.value()) {
                // 如果类上或方法上存在@Sensitive注解，则执行脱敏操作
                value = value.replaceAll(this.type.getPattern(), this.type.getTargetChar());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        jsonGenerator.writeString(value);
    }

    /**
     * @param serializerProvider
     * @param beanProperty 被@SensitiveInfo注解标注的Dto的属性
     * @return
     * @throws JsonMappingException
     */
    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider,
                                              final BeanProperty beanProperty) throws JsonMappingException {
        // 为空直接跳过
        if (beanProperty != null) {
            // 非 String 类直接跳过
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                SensitiveInfo sensitiveInfo = beanProperty.getAnnotation(SensitiveInfo.class);
                if (null != beanProperty.getAnnotation(SensitiveInfo.class)) {
                    // 如果能得到注解，就将注解的 value 传入 SensitiveInfoSerialize
                    return new SensitiveInfoSerialize(sensitiveInfo.value());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(beanProperty);
    }
}
