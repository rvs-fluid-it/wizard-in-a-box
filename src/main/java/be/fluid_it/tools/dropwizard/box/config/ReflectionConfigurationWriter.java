package be.fluid_it.tools.dropwizard.box.config;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

public class ReflectionConfigurationWriter implements ConfigurationWriter {
    private Object configuration;
    private ObjectMapper mapper;
    
    public ReflectionConfigurationWriter(Object configuration, ObjectMapper mapper) {
        this.configuration = configuration;
        this.mapper = mapper;
    }

    public void setConfiguration(Object configuration) {
        this.configuration = configuration;
    }

    public void setObjectMapper(ObjectMapper mapper) {
    	this.mapper = mapper;
    }
    
	@Override
    public void write(String[] path, Object inputValue) throws ConfigurationWriterException {
        Object conf = this.configuration;
        for (int i = 0; i < path.length; i++) {
            PropertyDescriptor propertyDescriptor;
            try {
                propertyDescriptor = getPropertyDescriptor(path[i], conf.getClass());
            } catch (Exception e) {
                throw new ConfigurationWriterException("Failed!", e);
            }
        	
            if (i == path.length - 1) {
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
                try {
                	conf = propertyDescriptor.getReadMethod().invoke(conf);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new ConfigurationWriterException("Failed", e);
                }
            }
        }
    }
	
	public PropertyDescriptor getPropertyDescriptor(String propertyName, Class<?> confClass) throws IntrospectionException {
		if (mapper != null) {
			JavaType configType = mapper.constructType(confClass);
			BeanDescription configDescr = mapper.getSerializationConfig().introspect(configType);
			List<BeanPropertyDefinition> configProps = configDescr.findProperties();
			for (BeanPropertyDefinition prop : configProps) {
				if (prop.getName().equals(propertyName)) {
					Method readMethod  = prop.hasGetter() ? prop.getGetter().getMember() : null;
					Method writeMethod = prop.hasSetter() ? prop.getSetter().getMember() : null;
					return new PropertyDescriptor(propertyName, readMethod, writeMethod);
				}
			}
		}
		return new PropertyDescriptor(propertyName, confClass);
	}    
}
