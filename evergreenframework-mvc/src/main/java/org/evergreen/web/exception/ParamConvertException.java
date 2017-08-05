package org.evergreen.web.exception;

import org.evergreen.web.HttpStatus;

/**
 * Created by wangl on 2017/7/16.
 */
public class ParamConvertException extends ActionException{

    public ParamConvertException(Object value, String type) {
        super(value + " can not be converted to "+type+".", HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
