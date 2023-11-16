package org.gg.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Arrays;

public class BeanUtil {
    //helper method which returns a string array of property names which are null
    public static String[] getNullPropertyNames(Object source) {
        BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = src.getPropertyDescriptors();

        return Arrays.stream(propertyDescriptors)
                .map(PropertyDescriptor::getName)
                .filter(name -> src.getPropertyValue(name) == null).distinct().toArray(String[]::new);
    }
}
