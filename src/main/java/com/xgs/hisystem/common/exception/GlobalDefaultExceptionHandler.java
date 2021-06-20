package com.xgs.hisystem.common.exception;

import com.xgs.hisystem.common.enums.ResponseErrorEnum;
import com.xgs.hisystem.pojo.bo.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * @author XueGuiSheng
 * @date 2020/4/6
 * @description:
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public BaseResponse<?> handleException(HttpServletRequest req, Exception ex) {
        //参数校验失败异常
        if (ex instanceof MethodArgumentNotValidException) {
            String message = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors()
                    .stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList()).get(0);
            logger.error("参数校验失败! uri:{},错误信息:{}", req.getRequestURI(), message);
            return BaseResponse.error(message);
        }
        //业务异常
        if (ex instanceof BusinessException) {
            logger.error("业务异常！uri:{},错误信息:{}", req.getRequestURI(), ex.getMessage());
            ResponseErrorEnum responseErrorEnum = ((BusinessException) ex).getErrorEnum();
            if (responseErrorEnum == null) {
                return BaseResponse.error(ex.getMessage());
            }
            return BaseResponse.error(responseErrorEnum.getMsg());
        }
        logger.error("系统错误！uri:{},错误信息:{}", req.getRequestURI(), ex);
        return BaseResponse.error("未知错误");
    }
}
