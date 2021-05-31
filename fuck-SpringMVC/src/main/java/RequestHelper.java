import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 前端控制器接收到HTTP请求后, 从HTTP中获取请求参数, 然后封装到Param对象中.
 * @Author: Kayleh
 * @Date: 2021/5/30 14:50
 * @Version: 1.0
 */
public class RequestHelper {
    /**
     * 获取请求参数
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static Param createParam(HttpServletRequest request) throws IOException {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Enumeration<String> paramNames = request.getParameterNames();
        //没有参数
        if (!paramNames.hasMoreElements())
            return null;
        //get和post参数都能获取到
        while (paramNames.hasMoreElements()) {
            String fieldName = paramNames.nextElement();
            String fieldValue = request.getParameter(fieldName);
            paramMap.put(fieldName, fieldValue);
        }
        return new Param(paramMap);
    }

}
