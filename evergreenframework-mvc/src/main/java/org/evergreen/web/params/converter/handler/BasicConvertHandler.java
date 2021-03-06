package org.evergreen.web.params.converter.handler;

import org.evergreen.web.exception.ParamConvertException;
import org.evergreen.web.params.ParamInfo;
import org.evergreen.web.params.converter.ParamsConvertHandler;
import org.evergreen.web.utils.beanutils.ConvertUtils;

public class BasicConvertHandler extends ParamsConvertHandler {

    public Object execute(ParamInfo paramInfo) {
        Object param = (paramInfo.getParamType().isArray()) ? getRequest()
                .getParameterValues(paramInfo.getParamName()) : getRequest()
                .getParameter(paramInfo.getParamName());
        if (param == null) {
            return null;
        }
        Object value = ConvertUtils
                .convert(param, paramInfo.getParamType());
        if (value == null && paramInfo.getParamType().isPrimitive()){
            throw new ParamConvertException(param + " can not be converted to "+paramInfo.getParamType().getName()+".");
        }
        if(value.getClass().equals(String.class) && !value.getClass().equals(paramInfo.getParamType())){
            throw new ParamConvertException(param + " can not be converted to "+paramInfo.getParamType().getName()+".");
        }
        return value;
    }
}
