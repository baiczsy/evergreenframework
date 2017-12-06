package org.evergreen.web.exception;

import org.evergreen.web.HttpStatus;

/**
 * Created by wangl on 2017/7/16.
 */
public class ParamConvertException extends ActionException{

    public ParamConvertException(String message){
        super(message);
    }

    public ParamConvertException(Throwable cause){
        super(cause);
    }

    public ParamConvertException(String message, Throwable cause){
        super(message, cause);
    }

    public ParamConvertException(String message, int responseStatus) {
        super(message, responseStatus);
    }

}
