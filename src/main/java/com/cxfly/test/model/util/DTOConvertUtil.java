package com.cxfly.test.model.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.util.Assert;


@SuppressWarnings("unchecked")
public final class DTOConvertUtil {

    private static final Logger logger = LoggerFactory.getLogger(DTOConvertUtil.class);

    public static <T> T dto2bean(Object dto, Class<T> targetClass) {
        if (dto == null) {
            if (targetClass.isPrimitive()) {
                return (T) getPrimitiveDefaultValue(targetClass);
            }
            return null;
        }
        final Class<?> srcClass = dto.getClass();
        if (srcClass.equals(targetClass) || targetClass.isAssignableFrom(srcClass)) {
            return (T) dto;
        }
        return dto2bean(dto, targetClass, null, new HashMap<ClassDTOKey, Object>());
    }

    private static Object getPrimitiveDefaultValue(Class<?> clazz) {
        if (!clazz.isPrimitive()) {
            return null;
        }
        if (Short.TYPE.equals(clazz)) {
            return Short.valueOf((short) 0);
        }
        if (Integer.TYPE.equals(clazz)) {
            return Integer.valueOf(0);
        }
        if (Long.TYPE.equals(clazz)) {
            return Long.valueOf(0);
        }
        if (Boolean.TYPE.equals(clazz)) {
            return Boolean.valueOf(false);
        }
        if (Float.TYPE.equals(clazz)) {
            return Float.valueOf(0);
        }
        if (Double.TYPE.equals(clazz)) {
            return Double.valueOf(0);
        }
        if (Byte.TYPE.equals(clazz)) {
            return Byte.valueOf((byte) 0);
        }
        if (Character.TYPE.equals(clazz)) {
            return Character.valueOf('\0');
        }
        return null;
    }

    private static final class ClassDTOKey {

        Object   dto;
        Class<?> targetClass;

        public ClassDTOKey(Object dto, Class<?> targetClass) {
            this.dto = dto;
            this.targetClass = targetClass;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(dto) + targetClass.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ClassDTOKey)) {
                return false;
            }
            final ClassDTOKey other = (ClassDTOKey) obj;
            return dto == other.dto && targetClass.equals(other.targetClass);
        }
    }

    @SuppressWarnings("rawtypes")
    private static <T> T dto2bean(Object dto, Class<T> targetClass, Class<?>[] genericTypes,
                                  Map<ClassDTOKey, Object> convertMap) {
        if (dto == null) {
            if (targetClass.isPrimitive()) {
                return (T) getPrimitiveDefaultValue(targetClass);
            }
            return null;
        }
        final Class<?> srcClass = dto.getClass();
        if (srcClass.equals(targetClass) || targetClass.isAssignableFrom(srcClass)) {
            if (genericTypes == null) {
                return (T) dto;
            }
        }
        final ClassDTOKey key = new ClassDTOKey(dto, targetClass);
        if (convertMap.containsKey(key)) {
            return (T) convertMap.get(key);
        }
        Object result = null;
        // normal type
        if (Short.TYPE.equals(targetClass) || Short.class.equals(targetClass)) {
            if (Number.class.isAssignableFrom(srcClass)) {
                result = ((Number) dto).shortValue();
            } else {
                result = Short.valueOf(dto.toString());
            }
        } else if (Integer.TYPE.equals(targetClass) || Integer.class.equals(targetClass)) {
            if (Number.class.isAssignableFrom(srcClass)) {
                result = ((Number) dto).intValue();
            } else {
                result = Integer.valueOf(dto.toString());
            }
        } else if (Long.TYPE.equals(targetClass) || Long.class.equals(targetClass)) {
            if (Number.class.isAssignableFrom(srcClass)) {
                result = ((Number) dto).longValue();
            } else {
                result = Long.valueOf(dto.toString());
            }
        } else if (Boolean.TYPE.equals(targetClass) || Boolean.class.equals(targetClass)) {
            if (Boolean.class.isAssignableFrom(srcClass)) {
                result = (dto);
            } else {
                result = Boolean.valueOf(dto.toString());
            }
        } else if (Float.TYPE.equals(targetClass) || Float.class.equals(targetClass)) {
            if (Number.class.isAssignableFrom(srcClass)) {
                result = ((Number) dto).floatValue();
            } else {
                result = Float.valueOf(dto.toString());
            }
        } else if (Double.TYPE.equals(targetClass) || Double.class.equals(targetClass)) {
            if (Number.class.isAssignableFrom(srcClass)) {
                result = ((Number) dto).doubleValue();
            } else {
                result = Double.valueOf(dto.toString());
            }
        } else if (Byte.TYPE.equals(targetClass) || Byte.class.equals(targetClass)) {
            if (Number.class.isAssignableFrom(srcClass)) {
                result = ((Number) dto).byteValue();
            } else {
                result = Byte.valueOf(dto.toString());
            }
        } else if (Character.TYPE.equals(targetClass) || Character.class.equals(targetClass)) {
            if (Character.class.isAssignableFrom(srcClass)) {
                result = (dto);
            } else {
                result = Character.valueOf(dto.toString().charAt(0));
            }
        } else if (Date.class.isAssignableFrom(targetClass)
                && Date.class.isAssignableFrom(srcClass)) {
            result = new Date(((Date) dto).getTime());
        } else if (StringBuilder.class.isAssignableFrom(targetClass)) {
            result = new StringBuilder(dto.toString());
        } else if (StringBuffer.class.isAssignableFrom(targetClass)) {
            result = new StringBuffer(dto.toString());
            // array
        } else if (targetClass.isArray() && srcClass.isArray()) {
            final Class<?> componentType = targetClass.getComponentType();
            final Class<?> componentType2 = srcClass.getComponentType();
            if (componentType.isAssignableFrom(componentType2)) {
                result = dto;
            } else {
                result = Array.newInstance(componentType, Array.getLength(dto));
                convertMap.put(key, result);
                for (int i = 0; i < Array.getLength(dto); i++) {
                    Array.set(result, i,
                            dto2bean(Array.get(dto, i), componentType, null, convertMap));
                }
                return (T) result;
            }
        } else if (Collection.class.isAssignableFrom(targetClass)
                && Collection.class.isAssignableFrom(srcClass)) {
            try {
                result = newInstance(targetClass);
                Class<?> itemType = null;
                if (genericTypes != null && genericTypes.length == 1) {
                    itemType = genericTypes[0];
                }
                for (final Object item : (Collection<?>) dto) {
                    if (itemType != null) {
                        ((Collection) result).add(dto2bean(item, itemType, null, convertMap));
                    } else {
                        ((Collection) result).add(item);
                    }
                }
            } catch (final InstantiationException e) {
                if (logger.isWarnEnabled()) {
                    logger.warn("new instance error, class:" + targetClass, e);
                }
            } catch (final IllegalAccessException e) {
                if (logger.isWarnEnabled()) {
                    logger.warn("new instance error, class:" + targetClass, e);
                }
            }
        } else if (Map.class.isAssignableFrom(targetClass) && Map.class.isAssignableFrom(srcClass)) {
            try {
                result = newInstance(targetClass);
                Class<?> valueType = null;
                Class<?>[] genericType = null;
                if (genericTypes != null && genericTypes.length == 2) {
                    valueType = genericTypes[1];
                }
                if (genericTypes != null && genericTypes.length == 3) {
                    valueType = genericTypes[1];
                    genericType = new Class<?>[] { genericTypes[2] };
                }

                for (final Map.Entry<?, ?> entry : ((Map<?, ?>) dto).entrySet()) {
                    if (valueType != null) {
                        ((Map) result).put(entry.getKey(),
                                dto2bean(entry.getValue(), valueType, genericType, convertMap));
                    } else {
                        ((Map) result).put(entry.getKey(), entry.getValue());
                    }
                }
            } catch (final InstantiationException e) {
                if (logger.isWarnEnabled()) {
                    logger.warn("new instance error, class:" + targetClass, e);
                }
            } catch (final IllegalAccessException e) {
                if (logger.isWarnEnabled()) {
                    logger.warn("new instance error, class:" + targetClass, e);
                }
            }
        } else {
            if (Map.class.isAssignableFrom(srcClass)) {
                try {
                    result = newInstance(targetClass);
                    convertMap.put(key, result);
                    for (final Map.Entry<?, ?> entry : ((Map<?, ?>) dto).entrySet()) {
                        final String name = entry.getKey().toString();
                        if (PropertyUtils.isWriteable(result, name)) {
                            try {
                                Class<?>[] genericParamTypes = GenericsUtil
                                        .getGenericType(PropertyUtils
                                                .getPropertyDescriptor(result, name)
                                                .getWriteMethod().getGenericParameterTypes()[0]);
                                final Class<?> propertyType = PropertyUtils.getPropertyDescriptor(
                                        result, name).getPropertyType();
                                PropertyUtils.setProperty(
                                        result,
                                        name,
                                        dto2bean(entry.getValue(), propertyType, genericParamTypes,
                                                convertMap));
                            } catch (final InvocationTargetException e) {
                                if (logger.isWarnEnabled()) {
                                    logger.warn(
                                            "write property error, class:" + targetClass.getName()
                                                    + " property:" + name, e);
                                }
                            } catch (final NoSuchMethodException e) {
                                if (logger.isWarnEnabled()) {
                                    logger.warn(
                                            "write property error, class:" + targetClass.getName()
                                                    + " property:" + name, e);
                                }
                            }
                        }
                    }
                    return (T) result;
                } catch (final InstantiationException e) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("new instance error, class:" + targetClass, e);
                    }
                } catch (final IllegalAccessException e) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("new instance error, class:" + targetClass, e);
                    }
                }
            }
        }
        convertMap.put(key, result);
        return (T) result;
    }

    public static Object bean2dto(Object bean) {
        if (bean == null) {
            return null;
        }
        final Class<?> clazz = bean.getClass();
        if (isNotConvertType(clazz)) {
            return bean;
        }
        return convert2dto(bean, clazz, new HashMap<Object, Object>(), StringUtils.EMPTY, null);
    }

    public static Object bean2dto(Object bean, FieldFilter fieldFilter) {
        if (bean == null) {
            return null;
        }
        final Class<?> clazz = bean.getClass();
        if (isNotConvertType(clazz)) {
            return bean;
        }
        return convert2dto(bean, clazz, new HashMap<Object, Object>(), StringUtils.EMPTY,
                fieldFilter);
    }

    private static Object bean2dto(Object bean, Map<Object, Object> convertMap, String path,
                                   FieldFilter fieldFilter) {
        if (bean == null) {
            return null;
        }
        final Class<?> clazz = bean.getClass();
        if (isNotConvertType(clazz)) {
            return bean;
        }
        return convert2dto(bean, clazz, convertMap, path, fieldFilter);
    }

    public static boolean isNotConvertType(Class<?> clazz) {
        return clazz.isPrimitive() || Short.class.equals(clazz) || Integer.class.equals(clazz)
                || Long.class.equals(clazz) || Boolean.class.equals(clazz)
                || Float.class.equals(clazz) || Double.class.equals(clazz)
                || Byte.class.equals(clazz) || Character.class.equals(clazz)
                || String.class.equals(clazz) || Date.class.equals(clazz);
    }

    @SuppressWarnings("rawtypes")
    private static Object newInstance(Class<?> clazz) throws InstantiationException,
            IllegalAccessException {
        Assert.notNull(clazz, "Class must not be null");
        if (!clazz.isInterface()) {
            return clazz.newInstance();
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            if (List.class.equals(clazz)) {
                return new ArrayList();
            } else if (Set.class.isAssignableFrom(clazz)) {
                return new LinkedHashSet();
            } else if (Queue.class.isAssignableFrom(clazz)) {
                return new PriorityQueue();
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            return new LinkedHashMap();
        }
        throw new BeanInstantiationException(clazz, "Specified class is an interface");
    }

    private static Class<?> getDTOClass(Class<?> beanClass) {
        if (isNotConvertType(beanClass)) {
            return beanClass;
        }
        if (Date.class.isAssignableFrom(beanClass)) {
            return Date.class;
        } else if (StringBuilder.class.isAssignableFrom(beanClass)
                || StringBuffer.class.isAssignableFrom(beanClass)) {
            return String.class;
        } else if (beanClass.isArray()) {
            final Class<?> componentType = beanClass.getComponentType();
            if (isNotConvertType(componentType)) {
                return beanClass;
            } else {
                return Array.newInstance(getDTOClass(componentType), 0).getClass();
            }
        } else if (Collection.class.isAssignableFrom(beanClass)) {
            if (Set.class.isAssignableFrom(beanClass)) {
                if (LinkedHashSet.class.equals(beanClass)) {
                    return LinkedHashSet.class;
                } else if (beanClass.equals(TreeSet.class)) {
                    return TreeSet.class;
                } else {
                    return HashSet.class;
                }
            } else if (Queue.class.isAssignableFrom(beanClass)) {
                if (LinkedBlockingQueue.class.equals(beanClass)) {
                    return LinkedBlockingQueue.class;
                } else if (ArrayBlockingQueue.class.equals(beanClass)) {
                    return ArrayBlockingQueue.class;
                } else if (PriorityBlockingQueue.class.equals(beanClass)) {
                    return PriorityBlockingQueue.class;
                } else {
                    return PriorityQueue.class;
                }
            } else if (LinkedList.class.equals(beanClass)) {
                return LinkedList.class;
            } else if (Vector.class.equals(beanClass)) {
                return Vector.class;
            } else {
                return ArrayList.class;
            }
        } else if (Map.class.isAssignableFrom(beanClass)) {
            if (LinkedHashMap.class.equals(beanClass)) {
                return LinkedHashMap.class;
            } else if (TreeMap.class.equals(beanClass)) {
                return TreeMap.class;
            } else if (Hashtable.class.equals(beanClass)) {
                return Hashtable.class;
            } else {
                return HashMap.class;
            }
        } else {
            return Object.class;
        }
    }

    private static Object convert2dto(Object bean, Class<?> beanClass,
                                      Map<Object, Object> convertMap, String path,
                                      FieldFilter fieldFilter) {
        if (convertMap.containsKey(bean)) {
            return convertMap.get(bean);
        }
        final Object result;
        if (Date.class.isAssignableFrom(beanClass)) {
            result = new Date(((Date) bean).getTime());
        } else if (StringBuilder.class.isAssignableFrom(beanClass)
                || StringBuffer.class.isAssignableFrom(beanClass)) {
            result = bean.toString();
        } else if (beanClass.isArray()) {
            final Class<?> componentType = beanClass.getComponentType();
            if (isNotConvertType(componentType)) {
                result = bean;
            } else {
                if (Object.class.equals(componentType)) {
                    result = new Object[Array.getLength(bean)];
                } else {
                    final Class<?> dtoType = getDTOClass(componentType);
                    result = Array.newInstance(dtoType, Array.getLength(bean));
                }
                convertMap.put(bean, result);
                for (int i = Array.getLength(bean) - 1; i >= 0; i--) {
                    // Array.set(result, i, bean2dto(Array.get(bean, i), convertMap, path + "[]", fieldFilter));
                    Array.set(result, i,
                            bean2dto(Array.get(bean, i), convertMap, path, fieldFilter));
                }
                return result;
            }
        } else if (bean instanceof Collection) {
            return collection2dto((Collection<?>) bean, beanClass, convertMap, path, fieldFilter);
        } else if (bean instanceof Map) {
            return map2dto((Map<?, ?>) bean, beanClass, convertMap, path, fieldFilter);
        } else {
            return bean2DynamicDTO(bean, beanClass, convertMap, path, fieldFilter);
        }
        convertMap.put(bean, result);
        return result;
    }

    @SuppressWarnings("rawtypes")
    private static Map bean2DynamicDTO(Object bean, Class<?> beanClass,
                                       Map<Object, Object> convertMap, String path,
                                       FieldFilter fieldFilter) {
        final LinkedHashMap result = new LinkedHashMap();
        convertMap.put(bean, result);
        final PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(beanClass);
        for (final PropertyDescriptor pd : pds) {
            final String name = pd.getName();
            if ("class".equals(name)) {
                continue;
            }
            if (fieldFilter == null || fieldFilter.isFieldEnable(name, path)) {
                ;
                if (PropertyUtils.isReadable(bean, name)) {
                    Object value = null;
                    try {
                        value = PropertyUtils.getSimpleProperty(bean, name);
                    } catch (final IllegalAccessException e) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("get property error, class:" + beanClass.getName()
                                    + " property:" + name, e);
                        }
                    } catch (final InvocationTargetException e) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("get property error, class:" + beanClass.getName()
                                    + " property:" + name, e);
                        }
                    } catch (final NoSuchMethodException e) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("get property error, class:" + beanClass.getName()
                                    + " property:" + name, e);
                        }
                    }
                    value = bean2dto(value, convertMap, pathJoin(path, name), fieldFilter);
                    result.put(name, value);
                }
            }
        }
        return result;
    }

    private static Map<?, ?> map2dto(Map<?, ?> bean, Class<?> beanClass,
                                     Map<Object, Object> convertMap, String path,
                                     FieldFilter fieldFilter) {
        final Map<Object, Object> result;
        if (LinkedHashMap.class.equals(beanClass)) {
            result = new LinkedHashMap<Object, Object>();
        } else if (TreeMap.class.equals(beanClass)) {
            result = new TreeMap<Object, Object>();
        } else if (Hashtable.class.equals(beanClass)) {
            result = new Hashtable<Object, Object>();
        } else {
            result = new HashMap<Object, Object>();
        }
        convertMap.put(bean, result);
        for (final Map.Entry<?, ?> entry : ((Map<?, ?>) bean).entrySet()) {
            final Object key = bean2dto(entry.getKey(), convertMap, StringUtils.EMPTY, null);
            if (fieldFilter == null || fieldFilter.isFieldEnable(String.valueOf(key), path)) {
                ;
                final Object value = bean2dto(entry.getValue(), convertMap,
                        pathJoin(path, String.valueOf(key)), fieldFilter);
                result.put(key, value);
            }
        }
        return result;
    }

    private static String pathJoin(String prefPath, String fieldName) {
        if (StringUtils.isNotBlank(prefPath)) {
            return prefPath + '.' + fieldName;
        }
        return fieldName;
    }

    private static Collection<?> collection2dto(Collection<?> bean, Class<?> beanClass,
                                                Map<Object, Object> convertMap, String path,
                                                FieldFilter fieldFilter) {
        final Collection<Object> result;
        if (bean instanceof Set) {
            if (LinkedHashSet.class.equals(beanClass)) {
                result = new LinkedHashSet<Object>();
            } else if (TreeSet.class.equals(beanClass)) {
                result = new TreeSet<Object>();
            } else {
                result = new HashSet<Object>();
            }
        } else if (bean instanceof Queue) {
            if (LinkedBlockingQueue.class.equals(beanClass)) {
                result = new LinkedBlockingQueue<Object>(bean.size());
            } else if (ArrayBlockingQueue.class.equals(beanClass)) {
                result = new ArrayBlockingQueue<Object>(bean.size());
            } else if (PriorityBlockingQueue.class.equals(beanClass)) {
                result = new PriorityBlockingQueue<Object>(bean.size());
            } else {
                result = new PriorityQueue<Object>(bean.size());
            }
        } else if (LinkedList.class.equals(beanClass)) {
            result = new LinkedList<Object>();
        } else if (Vector.class.equals(beanClass)) {
            result = new Vector<Object>(bean.size());
        } else {
            result = new ArrayList<Object>(bean.size());
        }
        convertMap.put(bean, result);
        final String nowPath = path;// + "[]";
        for (final Object value : bean) {
            result.add(bean2dto(value, convertMap, nowPath, fieldFilter));
        }
        return result;
    }

    public static interface FieldFilter {

        boolean isFieldEnable(String fieldName, String prefPath);
    }

    public static class DefaultFieldFilter implements FieldFilter {

        private final Collection<String> enableFields;

        public DefaultFieldFilter(Collection<String> enableFields) {
            this.enableFields = enableFields;
        }

        public DefaultFieldFilter(String[] enableFields) {
            this.enableFields = new ArrayList<String>();
            Collections.addAll(this.enableFields, enableFields);
        }

        public boolean isFieldEnable(String fieldName, String prefPath) {
            for (final String enableField : enableFields) {
                if (prefPath == null || prefPath.length() == 0) {
                    if (enableField.length() == fieldName.length()) {
                        if (enableField.equals(fieldName)) {
                            return true;
                        }
                    } else if (enableField.length() > prefPath.length()) {
                        if (enableField.startsWith(fieldName)
                                && enableField.charAt(fieldName.length()) == '.') {
                            return true;
                        }
                    }
                    continue;
                }
                if (enableField.length() == prefPath.length()) {
                    if (enableField.equals(prefPath)) {
                        return true;
                    }
                } else if (enableField.length() < prefPath.length()) {
                    if (prefPath.startsWith(enableField)
                            && prefPath.charAt(enableField.length()) == '.') {
                        return true;
                    }
                } else if (enableField.startsWith(prefPath)
                        && enableField.charAt(prefPath.length()) == '.') {
                    if (enableField.startsWith(fieldName, prefPath.length() + 1)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
