package com.cxfly.test.model.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class GenericsUtil {

    private static final Logger              logger = LoggerFactory.getLogger(GenericsUtil.class);
    private static Map<Class<?>, Class<?>[]> cache  = new ConcurrentHashMap<Class<?>, Class<?>[]>();

    private GenericsUtil() {
    }

    public static Class<?> getSuperClassGenricType(Class<?> clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    public static Class<?> getInterfaceGenricType(Class<?> clazz, final Class<?> interfaceClass) {
        return getInterfaceGenricType(clazz, interfaceClass, 0);
    }

    public static Class<?> getTypeParameterClass(final Class<?> clazz, final String name) {
        final TypeVariable<?>[] types = clazz.getTypeParameters();
        if (types != null && types.length > 0) {
            for (int i = 0; i < types.length; i++) {
                if (name.equals(types[i].getName())) {
                    final Type[] bounds;
                    if (types[i] instanceof WildcardType) {
                        bounds = ((WildcardType) types[i]).getLowerBounds();
                    } else {
                        bounds = types[i].getBounds();
                    }
                    if (bounds != null && bounds.length > 0) {
                        if (bounds[0] instanceof Class) {
                            return (Class<?>) bounds[0];
                        } else if (bounds[0] instanceof TypeVariable) {
                            return getTypeParameterClass(clazz,
                                    ((TypeVariable<?>) bounds[0]).getName());
                        }
                    }
                    return null;
                }
            }
        }
        return null;
    }

    public static Class<?> getInterfaceGenricType(final Class<?> clazz,
                                                  final Class<?> interfaceClass, final int typeIndex) {
        final Type[] interfaceTypes = clazz.getGenericInterfaces();
        if (interfaceTypes != null && interfaceTypes.length > 0) {
            for (int i = 0; i < interfaceTypes.length; i++) {
                final Type interfaceType = interfaceTypes[i];
                if (interfaceType instanceof ParameterizedType) {
                    if (interfaceClass
                            .isAssignableFrom((Class<?>) ((ParameterizedType) interfaceType)
                                    .getRawType())) {
                        final Type[] params = ((ParameterizedType) interfaceType)
                                .getActualTypeArguments();
                        final Class<?>[] types = new Class[params.length];
                        for (int j = 0; j < params.length; j++) {
                            if (params[i] instanceof Class) {
                                types[i] = (Class<?>) params[i];
                            } else if (params[i] instanceof WildcardType) {
                                final Type[] lowerBounds = ((WildcardType) params[i])
                                        .getLowerBounds();
                                if (lowerBounds != null && lowerBounds.length > 0) {
                                    if (lowerBounds[0] instanceof Class) {
                                        types[i] = (Class<?>) lowerBounds[0];
                                    }
                                }
                            } else if (params[i] instanceof TypeVariable) {
                                types[i] = getTypeParameterClass(clazz,
                                        ((TypeVariable<?>) params[i]).getName());
                            } else {
                                types[i] = Object.class;
                            }
                        }
                        if (typeIndex >= types.length || typeIndex < 0) {
                            return null;
                        }
                        return types[typeIndex];
                    }
                }
            }
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    public static Class<?>[] getSuperClassGenricTypes(Class<?> clazz) {
        Class<?>[] types = cache.get(clazz);
        if (types == null) {
            Type genType = null;
            Class<?> superClass = clazz;
            while (superClass != null) {
                genType = superClass.getGenericSuperclass();
                if (genType instanceof ParameterizedType) {
                    break;
                } else {
                    if (logger.isWarnEnabled()) {
                        logger.warn(superClass.getName()
                                + "'s generic superclass not ParameterizedType");
                    }
                    superClass = superClass.getSuperclass();
                }
            }
            if (!(genType instanceof ParameterizedType)) {
                if (logger.isWarnEnabled()) {
                    logger.warn(clazz.getName()
                            + "'s generic superclass not ParameterizedType. (check all)");
                }
                types = new Class[0];
            } else {
                final Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
                types = new Class[params.length];
                for (int i = 0; i < params.length; i++) {
                    if (params[i] instanceof Class) {
                        types[i] = (Class<?>) params[i];
                    } else if (params[i] instanceof WildcardType) {
                        final Type[] lowerBounds = ((WildcardType) params[i]).getLowerBounds();
                        if (lowerBounds != null && lowerBounds.length > 0) {
                            if (lowerBounds[0] instanceof Class) {
                                types[i] = (Class<?>) lowerBounds[0];
                            }
                        }
                    } else if (params[i] instanceof TypeVariable) {
                        types[i] = getTypeParameterClass(superClass,
                                ((TypeVariable) params[i]).getName());
                    } else {
                        if (logger.isWarnEnabled()) {
                            logger.warn(clazz.getSimpleName()
                                    + " not set the actual class on superclass generic cached parameter");
                        }
                        types[i] = Object.class;
                    }
                }
            }
            cache.put(clazz, types);
        }
        return types;
    }

    public static Class<?> getSuperClassGenricType(Class<?> clazz, final int index) {
        Class<?>[] types = getSuperClassGenricTypes(clazz);
        if (index >= types.length || index < 0) {
            if (logger.isWarnEnabled()) {
                logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName()
                        + "'s cached Parameterized Type: " + types.length);
            }
            return Object.class;
        }
        return types[index];
    }

    public static Class<?> getMethodGenericReturnType(Method method, int index) {
        Type returnType = method.getGenericReturnType();
        return getGenericType(returnType, index);
    }

    public static Class<?> getGenericType(Type type, int index) {
        if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) type;
            Type[] typeArguments = ptype.getActualTypeArguments();
            if (index >= typeArguments.length || index < 0) {
                throw new RuntimeException("index "
                        + (index < 0 ? " must large then 0" : "out of arguments count"));
            }
            return (Class<?>) typeArguments[index];
        }
        return null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Class<?>[] getGenericType(Type type) {
        if (type instanceof ParameterizedType) {
            // 仅支持两成嵌套
            // Map<String,List<Bean>>
            // Map<String,Map<String,String>>
            ParameterizedType ptype = (ParameterizedType) type;
            Type[] typeArguments = ptype.getActualTypeArguments();
            List types = new ArrayList();
            for (int i = 0; i < typeArguments.length; i++) {
                if (typeArguments[i] instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) typeArguments[i];
                    types.add(parameterizedType.getRawType());
                    for (Type t : parameterizedType.getActualTypeArguments()) {
                        types.add(t);
                    }
                } else {
                    types.add(typeArguments[i]);
                }
            }
            Class<?>[] ret = new Class<?>[types.size()];
            for (int i = 0; i < ret.length; i++) {
                ret[i] = (Class<?>) types.get(i);
            }
            return ret;
        }
        return null;
    }

    // public static Class<?>[] getGenericType(Type type) {
    // if (type instanceof ParameterizedType) {
    // ParameterizedType ptype = (ParameterizedType) type;
    // Type[] typeArguments = ptype.getActualTypeArguments();
    // Class<?>[] types = new Class<?>[typeArguments.length];
    // System.arraycopy(typeArguments, 0, types, 0, types.length);
    // return types;
    // }
    // return null;
    // }

    public static void main(String[] args) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        A bean = new A();
        getGenericType(PropertyUtils.getPropertyDescriptor(bean, "hello").getWriteMethod()
                .getGenericParameterTypes()[0]);
        getGenericType(PropertyUtils.getPropertyDescriptor(bean, "str").getWriteMethod()
                .getGenericParameterTypes()[0]);
    }

    public static class A {

        private List<String> str;

        // private Map<String, List<String>> hello;

        /**
         * @param hello the hello to set
         */
        // public void setHello(Map<String, List<String>> hello) {
        // this.hello = hello;
        // }
        //
        // /**
        // * @return the hello
        // */
        // public Map<String, List<String>> getHello() {
        // return hello;
        // }

        /**
         * @param str the str to set
         */
        public void setStr(List<String> str) {
            this.str = str;
        }

        /**
         * @return the str
         */
        public List<String> getStr() {
            return str;
        }
    }
}
