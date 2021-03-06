package org.evergreen.web.params.converter;

import org.evergreen.web.ActionDefinition;
import org.evergreen.web.ActionMapper;
import org.evergreen.web.HttpStatus;
import org.evergreen.web.exception.ParamConvertException;
import org.evergreen.web.params.ParamInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class ParamConvertUtil {

    private static List<ParamsConvertHandler> handlers = new ArrayList<ParamsConvertHandler>();

    static {
        Iterator<ParamsConvertHandler> iterator = ServiceLoader.load(ParamsConvertHandler.class).iterator();
        while(iterator.hasNext()){
            handlers.add(iterator.next());
        }
    }

    /**
     * 类型转换
     * @param mapper
     * @return
     */
    public static Object[] convert(ActionMapper mapper) {
        ActionDefinition definition = mapper.getDefinition();
        Object[] params = new Object[definition.getParamInfo().size()];
        for (int i = 0; i < params.length; i++) {
            ParamInfo paramInfo = definition.getParamInfo().get(i);
            params[i] = doExecute(paramInfo);
            // 如果映射参数不成功,且参数类型是基本类型,则抛出异常信息
            if (params[i] == null && paramInfo.getParamType().isPrimitive()) {
                throw new ParamConvertException("Optional " + paramInfo.getParamType().getName() +
                        " parameter " + paramInfo.getParamName() +
                        " is present but cannot be translated into a null value due to being declared as a primitive type.");
            }
        }
        return params;
    }

    private static Object doExecute(ParamInfo paramInfo) {
        for (ParamsConvertHandler handler : handlers) {
            Object param = handler.execute(paramInfo);
            // 如果映射成功,则返回该参数,如果为null,
            // 则继续循环使用下一个命令执行映射
            if (param != null)
                return param;
        }
        return null;
    }
}
