package com.haige.gulimall.product.exception;

import com.haige.common.exception.BizCodeEnum;
import com.haige.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className: com.haige.gulimall.product.Vaexception-> lidParamExceptionHandler
 * @description:
 * @author: cqh
 * @createDate: 2021-11-11 14:27
 * @version: 1.0
 * @todo:
 */
@Slf4j
@RestControllerAdvice
public class ValidParamExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public R handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex){
        log.error("数据校验出现了异常 => {}",ex.getMessage());
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> errors = bindingResult.getFieldErrors();
        //初始化错误信息大小
        Map<String,String> errorMessage = new HashMap<>(errors.size());
        for (FieldError error: errors
        ) {
            errorMessage.put(error.getField(), error.getDefaultMessage());
        }

        return R.error(BizCodeEnum.VAILD_EXCEPTION.getCode(),BizCodeEnum.VAILD_EXCEPTION.getMessage()).put("error",errorMessage);
    }

    @ExceptionHandler(Throwable.class)
    public R handleException(Throwable throwable){
        log.error("出现了异常 => {}",throwable.getMessage());
        return R.error(BizCodeEnum.UNKNOW_EXCEPTION.getCode(),BizCodeEnum.UNKNOW_EXCEPTION.getMessage());
    }


}