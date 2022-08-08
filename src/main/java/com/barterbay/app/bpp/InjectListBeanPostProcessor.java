package com.barterbay.app.bpp;

import com.barterbay.app.annotation.InjectList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class InjectListBeanPostProcessor implements BeanPostProcessor {

  private final ApplicationContext context;

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    final Set<Field> fields = ReflectionUtils.getAllFields(bean.getClass(),
      field -> field.isAnnotationPresent(InjectList.class));
    fields.forEach(field -> {
      final InjectList annotation = field.getAnnotation(InjectList.class);
      final List<Object> neededClassesForAnnotation = Arrays.stream(annotation.value())
        // line below convert bean name, make only first letter small
        .map(aClass -> Introspector.decapitalize(aClass.getSimpleName()))
        // searching beans by name, which created by the line above
        .map(context::getBean)
        .collect(Collectors.toList());
      field.setAccessible(true);
      try {
        field.set(bean, neededClassesForAnnotation);
      } catch (IllegalAccessException e) {
        log.error("Error while creating BPP for InjectList annotation", e);
        // Terminates the currently running Java Virtual Machine
        Runtime.getRuntime().exit(666);
      }
    });
    return bean;
  }
}
