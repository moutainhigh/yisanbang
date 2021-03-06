package com.vtmer.yisanbang.common.aop;

import com.vtmer.yisanbang.common.annotation.RequestLog;
import com.vtmer.yisanbang.common.util.JwtUtil;
import com.vtmer.yisanbang.domain.User;
import com.vtmer.yisanbang.shiro.JwtFilter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

@Aspect
@Component
public class SystemLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);
    private static final String UNKNOWN = "unknown";

    /**
     * 1，表示在哪个类的哪个方法进行切入。配置有切入点表达式。
     * 2，对有@SystemLog标记的方法,记录其执行参数及返回结果。
     */
    @Pointcut("execution(* com.vtmer.yisanbang..controller..*.*(..))&&@annotation(com.vtmer.yisanbang.common.annotation.RequestLog)")
    public void controllerAspect() {
    }

    /**
     * 配置controller环绕通知,使用在方法aspect()上注册的切入点
     */
    @Before("controllerAspect()")
    public void aroundMethod(JoinPoint point) throws Throwable {
        if (logger.isDebugEnabled()) {
            logger.info(">>>>>>>>>>>>>>>进入日志切面<<<<<<<<<<<<<<<<");
        }
        // 获取接口的路径地址
        String methodTarget = point.getTarget().getClass().getName() + "." + point.getSignature().getName() + "()";
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        RequestLog log = method.getAnnotation(RequestLog.class);
        String desc = log.operationDesc();
        String module = log.module();
        logger.info("前置通知>>>>>>>>>>>>>>>操作模块：[" + module + "],操作方法：[" + methodTarget + "]，操作行为：[" + desc + "]<<<<<<<<<<<<<<<<");
        //打印当前的请求路径
        logger.info("RequestMapping:[{}]",request.getRequestURI());
        //这里是从token中获取用户信息，打印当前的访问用户
        String token = request.getHeader(JwtUtil.TOKEN_HEADER);
        if (token != null) {
            Integer userIdByToken = JwtUtil.getUserIdByToken(token);
            logger.info("Current UserId is:[{}]",userIdByToken);
        }
        logger.info("RequestParam:{}", Arrays.toString(point.getArgs()));
    }

    /**
     * 获取参数
     * @param request
     * @param osNos
     * @return
     */
    public String getNo(HttpServletRequest request, String[] osNos) {
        String userAgent = request.getHeader("user-agent");
        String osNo = "";
        if (userAgent != null) {
            String str = userAgent.toUpperCase();
            for (int i = 0; i < osNos.length; i++) {
                if (str.indexOf(osNos[i]) > 0) {
                    String str1 = str.substring(str.indexOf(osNos[i]));
                    if (str1.indexOf(';') > 0) {
                        osNo = str1.substring(0, str1.indexOf(';'));
                    } else {
                        osNo = osNos[i];
                    }
                    break;
                }
            }
        }
        return osNo;
    }
}
