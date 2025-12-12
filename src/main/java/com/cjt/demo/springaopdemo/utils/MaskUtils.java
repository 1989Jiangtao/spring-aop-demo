package com.cjt.demo.springaopdemo.utils;

import org.springframework.util.StringUtils;
/*****************************************************
 * @package   com.cjt.demo.springaopdemo.utils
 * @class     MaskUtils
 * @author    caojiantao
 * @datetime  2025/12/10 17:10
 * @describe  脱敏正则表达式：参考：https://blog.csdn.net/hxj413977035/article/details/126814044
 ****************************************************/
public class MaskUtils {

    private MaskUtils(){}

    /**
     * 手机号掩码处理 15312345567 -->  153****5567
     *
     * @param mobile 手机号
     * @return 处理后的手机号
     */
    public static String maskMobile(String mobile) {
        if (!StringUtils.hasText(mobile)) {
            return "";
        }
        return mobile.replaceAll("(1\\d{2})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 姓名掩码处理  王语嫣  -->  王**
     *
     * @param name 姓名
     * @return 处理后的姓名
     */
    public static String maskName(String name) {
        if (!StringUtils.hasText(name)) {
            return "";
        }
        return name.replaceAll("([\u4e00-\u9fa5\\s])([\u4e00-\u9fa5.\\s]*)", "$1**");
    }

    /**
     * 身份证号掩码处理 31033219990909432X -->  3103**********432X
     *
     * @param idCardNo 身份证号码
     * @return 处理后的身份证号码
     */
    public static String maskIdCardNo(String idCardNo) {
        if (!StringUtils.hasText(idCardNo)) {
            return "";
        }

                // 处理 15 位身份证
        return idCardNo.replaceAll("([1-9]\\d{3})(\\d{7})(\\d{4})", "$1**********$3").
                // 处理 18 位身份证
                replaceAll("([1-9]\\d{3})(\\d{10}(\\d{3}[Xx]))", "$1**********$3");
    }

    /**
     * 电子邮箱掩码处理 10000@qq.com -->  100***@qq.com
     *
     * @param email 电子邮箱
     * @return 处理后的电子邮箱
     */
    public static String maskEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return "";
        }
        return email.replaceAll("([a-zA-Z0-9_\\-.]{1,3})[a-zA-Z0-9_\\-.]*@([a-zA-Z0-9_\\-.]+)\\.([a-zA-Z]{2,5})", "$1***@$2.$3");
    }
}
