package com.vtmer.yisanbang.common.exception;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.exception.apiException.ApiException;
import com.vtmer.yisanbang.dto.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;


@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * 默认统一异常处理方法
     * @ExceptionHandler 注解用来配置需要拦截的异常类型, 也可以是自定义异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseMessage<ErrorDTO> runtimeExceptionHandler(Exception exception, HttpServletResponse response) {
        ErrorDTO errorDTO = new ErrorDTO();
        if(exception instanceof ApiException){//api异常
            logger.warn("api出现异常,异常信息为: {}", exception.getMessage());
            ApiException apiException = (ApiException)exception;
            errorDTO.setErrorCode(apiException.getErrorCode());
        }else{//未知异常
            logger.error("请求出现未知异常,异常信息为: {}", exception.getMessage());
            errorDTO.setErrorCode(0L);
        }
        errorDTO.setTip(exception.getMessage());
        return (ResponseMessage<ErrorDTO>) ResponseMessage.newErrorInstance(errorDTO,response.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseMessage handleBindException(MethodArgumentNotValidException ex) {
        /*
        // 返回所有参数效验错误信息
        StringBuilder errMsg = new StringBuilder();
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        for (ObjectError allError : allErrors) {
            errMsg.append(allError.getDefaultMessage()).append(",");
        }
        return ResponseMessage.newErrorInstance(errMsg.toString());
         */
        // 只返回一条参数校验异常信息
        FieldError fieldError = ex.getBindingResult().getFieldError();
        assert fieldError != null;
        logger.info("参数校验异常:{}({})", fieldError.getDefaultMessage(),fieldError.getField());
        return ResponseMessage.newErrorInstance(fieldError.getDefaultMessage());

    }

    @ExceptionHandler(BindException.class)
    public ResponseMessage handleBindException(BindException ex) {
        logger.info("参数校验异常:{}", ex.getMessage());
        return ResponseMessage.newErrorInstance(ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseMessage handleBindException(ConstraintViolationException ex) {
        logger.info("参数校验异常:{}", ex.getMessage());
        return ResponseMessage.newErrorInstance(ex.getMessage());
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseMessage handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logger.info("库存不足:{}", ex.getMessage());
        return ResponseMessage.newErrorInstance("商品库存不足，请稍后重试！");
    }
}
