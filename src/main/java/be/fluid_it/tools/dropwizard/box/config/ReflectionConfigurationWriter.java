package be.fluid_it.tools.dropwizard.box.config;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionConfigurationWriter implements ConfigurationWriter {
    private Object configuration;

    public ReflectionConfigurationWriter(Object configuration) {
        this.configuration = configuration;
    }

    public void setConfiguration(Object configuration) {
        this.configuration = configuration;
    }

    public void write(String[] path, Object inputValue) throws ConfigurationWriterException {
        Object conf = this.configuration;
        for (int i = 0; i < path.length; i++) {
            if (i == path.length - 1) {
                PropertyDescriptor propertyDescriptor;
                try {
                    propertyDescriptor = new PropertyDescriptor(path[i], conf.getClass());
                } catch (Exception e) {
                    throw new ConfigurationWriterException("Failed!", e);
                }

                Class<?> propertyType = propertyDescriptor.getPropertyType();
                if (propertyType.isPrimitive()) {
                    propertyType = ClassUtils.primitiveToWrapper(propertyType);
                }
                Object value = inputValue;
                if (inputValue instanceof String && propertyType != String.class) {
                    try {
                        Method valueOfMethod = propertyType.getMethod("valueOf", String.class);
                        value = valueOfMethod.invoke(null, inputValue);
                    } catch (NoSuchMethodException e) {
                        value = inputValue;
                    } catch (Exception e) {
                        throw new ConfigurationWriterException("Failed!", e);
                    }
                }

                try {
                    propertyDescriptor.getWriteMethod().invoke(conf, value);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new ConfigurationWriterException("Failed!", e);
                }

            } else {
                Method getterMethod;
                try {
                    getterMethod = conf.getClass().getMethod("get" + StringUtils.capitalize(path[i]));
                } catch (Exception e) {
                    throw new ConfigurationWriterException("Failed", e);
                }
                try {
                    conf = getterMethod.invoke(conf);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new ConfigurationWriterException("Failed", e);
                }
            }
        }
    }
}