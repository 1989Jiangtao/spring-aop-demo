//package com.cjt.demo.springaopdemo.annotation;
//
//import cn.hutool.json.JSONUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springblade.business.aspect.annotation.SensitiveField;
//import org.springblade.business.enums.SensitiveType;
//import org.springblade.business.util.DesensitizeUtil;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Field;
//import java.math.BigDecimal;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * 数据脱敏AOP切面
// * 拦截带@Sensitive注解的方法，自动脱敏返回值中的敏感字段
// */
//@Slf4j
//@Aspect
//@Component
//public class SensitiveAspect {
//
//    // 递归深度限制（避免无限递归）
//    private static final int MAX_RECURSION_DEPTH = 10;
//    // 已脱敏对象缓存（避免重复处理同一对象）
//    private final ThreadLocal<Set<Object>> desensitizedCache = ThreadLocal.withInitial(ConcurrentHashMap::newKeySet);
//    // 字段缓存（优化性能）
//    private final Map<Class<?>, Field[]> fieldCache = new ConcurrentHashMap<>();
//
//    /**
//	 * 切入点：拦截所有带@Sensitive注解的方法
//	 */
//    @Pointcut("@annotation(org.springblade.business.aspect.annotation.Sensitive)")
//    public void sensitivePointcut() {
//    }
//
//    /**
//	 * 返回通知：方法执行成功后，对返回值进行脱敏
//	 */
//    @AfterReturning(value = "sensitivePointcut()", returning = "result")
//    public void desensitize(JoinPoint joinPoint, Object result) {
//        log.info("脱敏切面:进入脱敏方法，返回值类型：{}", result.getClass().getName());
//
//        try {
//            log.info("脱敏切面:开始脱敏，原始数据：{}", JSONUtil.toJsonStr(result));
//            // 核心脱敏逻辑（初始化递归深度为0，清空缓存）
//            desensitizeObject(result, 0);
//            log.info("脱敏切面:脱敏完成，脱敏后数据：{}", JSONUtil.toJsonStr(result));
//        } catch (Exception e) {
//            log.error("脱敏切面:脱敏失败", e);
//        } finally {
//            // 清空线程缓存，避免内存泄漏
//            desensitizedCache.get().clear();
//            desensitizedCache.remove();
//        }
//        log.info("脱敏切面:脱敏完成，正在退出脱敏方法");
//    }
//
//    /**
//	 * 递归处理对象脱敏（增加递归深度限制,避免无限递归）
//	 *
//	 * @param obj   待脱敏对象
//	 * @param depth 当前递归深度
//	 */
//    private void desensitizeObject(Object obj, int depth) {
//        // 终止条件1：对象为空
//        if (obj == null) {
//            return;
//        }
//        // 终止条件2：递归深度超过限制
//        if (depth >= MAX_RECURSION_DEPTH) {
//            log.warn("【脱敏切面】递归深度超过{}，终止脱敏：{}", MAX_RECURSION_DEPTH, obj.getClass().getName());
//            return;
//        }
//        // 终止条件3：对象已脱敏（避免重复处理）
//        if (desensitizedCache.get().contains(obj)) {
//            return;
//        }
//        // 标记对象为已脱敏
//        desensitizedCache.get().add(obj);
//
//        // 适配R<T>返回格式：先获取data属性
//        if (obj.getClass().getSimpleName().equals("R")) {
//            try {
//                Field dataField = obj.getClass().getDeclaredField("data");
//                dataField.setAccessible(true);
//                Object data = dataField.get(obj);
//                // 递归处理data，深度+1
//                desensitizeObject(data, depth + 1);
//                return;
//            } catch (NoSuchFieldException | IllegalAccessException e) {
//                log.warn("【脱敏切面】未找到R对象的data字段，直接脱敏原对象");
//            }
//        }
//
//        // 场景1：集合（List/Set）→ 遍历元素脱敏
//        if (obj instanceof Collection<?> collection) {
//            for (Object item : collection) {
//                desensitizeObject(item, depth + 1);
//            }
//            return;
//        }
//
//        // 场景2：数组 → 遍历元素脱敏
//        if (obj.getClass().isArray()) {
//            Object[] array = (Object[]) obj;
//			for (Object item : array) {
//				desensitizeObject(item, depth + 1);
//			}
//			return;
//		}
//
//		// 场景3：单个业务对象 → 脱敏字段
//		desensitizeSingleObject(obj, depth + 1);
//	}
//
//	/**
//	 * 处理单个对象的脱敏（仅处理业务实体字段）
//	 */
//	private void desensitizeSingleObject(Object obj, int depth) {
//		if (obj == null) {
//			return;
//		}
//
//		Class<?> clazz = obj.getClass();
//		// 排除基础类型/String/Date（Date交给序列化器处理）
//		if (clazz.isPrimitive() || obj instanceof String || obj instanceof Date) {
//			return;
//		}
//
//		try {
//			Field[] fields = getCachedFields(clazz);
//			for (Field field : fields) {
//				field.setAccessible(true);
//				Object fieldValue = field.get(obj);
//				Class<?> fieldType = field.getType();
//
//				// 核心：跳过Date类型字段（无论是否有注解）
//				if (fieldType == Date.class) {
//					continue;
//				}
//
//				// 1. 无脱敏注解的字段：按原有逻辑过滤
//				if (!field.isAnnotationPresent(SensitiveField.class)) {
//					if (fieldValue != null && !isExcludeType(fieldType)) {
//						desensitizeObject(fieldValue, depth + 1);
//					}
//					continue;
//				}
//
//				// 2. 仅处理字符串类型的注解字段
//				if (fieldValue instanceof String strValue) {
//					SensitiveField annotation = field.getAnnotation(SensitiveField.class);
//					SensitiveType type = annotation.type();
//					int prefixLen = annotation.prefixLen();
//					int suffixLen = annotation.suffixLen();
//					String desensitizedStr = getDesensitizedString(strValue, type, prefixLen, suffixLen);
//					field.set(obj, desensitizedStr);
//				}
//			}
//		} catch (IllegalAccessException e) {
//			log.error("【脱敏切面】反射处理字段失败", e);
//		}
//	}
//
//	/**
//	 * 仅处理字符串类型的脱敏（移除类型转回逻辑，避免赋值异常）
//	 */
//	private String getDesensitizedString(String fieldValue, SensitiveType type, int prefixLen, int suffixLen) {
//		if (fieldValue.isBlank()) {
//			return fieldValue;
//		}
//
//		return switch (type) {
//			case PHONE -> DesensitizeUtil.desensitizePhone(fieldValue);
//			case ID_CARD -> DesensitizeUtil.desensitizeIdCard(fieldValue);
//			case NAME -> DesensitizeUtil.desensitizeName(fieldValue);
//			case PASSWORD -> DesensitizeUtil.desensitizePassword(fieldValue);
//			case CUSTOM -> DesensitizeUtil.desensitizeCustom(fieldValue, prefixLen, suffixLen);
//			case ADDRESS -> DesensitizeUtil.desensitizeAddress(fieldValue);
//			case AMOUNT -> DesensitizeUtil.desensitizeAmount(fieldValue);
//			case TIME -> DesensitizeUtil.desensitizeTime(fieldValue);
//			case ORDER_NO -> DesensitizeUtil.desensitizeOrderNo(fieldValue);
//			default -> fieldValue;
//		};
//	}
//
//	/**
//	 * 缓存获取类的所有字段（包括父类）
//	 */
//	private Field[] getCachedFields(Class<?> clazz) {
//		if (fieldCache.containsKey(clazz)) {
//			return fieldCache.get(clazz);
//		}
//
//		List<Field> fieldList = new ArrayList<>();
//		Class<?> currentClazz = clazz;
//		// 遍历所有父类（直到Object），不限制TenantEntity
//		while (currentClazz != null && currentClazz != Object.class) {
//			fieldList.addAll(Arrays.asList(currentClazz.getDeclaredFields()));
//			currentClazz = currentClazz.getSuperclass();
//		}
//
//		Field[] fields = fieldList.toArray(new Field[0]);
//		fieldCache.put(clazz, fields);
//		return fields;
//	}
//
//	/**
//	 * 判断是否为需要排除的类型（核心：避免递归处理框架/基础类型）
//	 */
//	private boolean isExcludeType(Class<?> clazz) {
//		// 基础类型包装类
//		Set<Class<?>> basicTypes = Set.of(Integer.class, Long.class, Double.class, Float.class,
//			Boolean.class, Byte.class, Short.class, Character.class);
//		if (basicTypes.contains(clazz)) {
//			return true;
//		}
//		// 核心：明确排除Date/数值类型
//		if (clazz == Date.class || clazz == BigDecimal.class || Number.class.isAssignableFrom(clazz)) {
//			return true;
//		}
//		// 框架类型排除
//		String className = clazz.getName();
//		return className.startsWith("java.util.") && !className.startsWith("java.util.List") && !className.startsWith("java.util.Set") ||
//			className.startsWith("org.springblade.") && !className.startsWith("org.springblade.business.entity") ||
//			className.startsWith("com.baomidou.mybatisplus.") ||
//			className.startsWith("jakarta.") ||
//			className.startsWith("org.springframework.");
//	}
//}