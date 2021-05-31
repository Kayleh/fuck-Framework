import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 映射处理器
 * ControllerHelper助手类定义了一个"请求-处理器" 的映射 REQUEST_MAP, REQUEST_MAP 就相当于Spring MVC里的映射处理器, 接收到请求后返回对应的处理器.
 * REQUEST_MAP 映射处理器的实现逻辑如下:
 * 首先通过 ClassHelper 工具类获取到应用中所有Controller的Class对象, 然后遍历Controller及其所有方法, 将所有带 @RequestMapping 注解的方法封装为处理器, 将 @RequestMapping 注解里的请求路径和请求方法封装成请求对象, 然后存入 REQUEST_MAP 中.
 * @Author: Kayleh
 * @Date: 2021/5/30 14:26
 * @Version: 1.0
 */
public final class ControllerHelper {
    private static final Map<Request, Handler> REQUEST_MAP = new HashMap<Request, Handler>();

    static {
        //遍历所有的Controller类
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (CollectionUtils.isNotEmpty(controllerClassSet)) {
            for (Class<?> controllerClass : controllerClassSet) {
                //暴力反射获取所有方法
                Method[] methods = controllerClass.getDeclaredMethods();
                //遍历方法
                if (ArrayUtils.isNotEmpty(methods)) {
                    for (Method method : methods) {
                        //判断是否带有RequestMapping注解
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                            //请求路径
                            String requestPath = requestMapping.value();
                            //请求方法
                            String requestMethod = requestMapping.method().name();

                            //封装请求和处理器
                            Request request = new Request(requestMethod, requestPath);
                            Handler handler = new Handler(controllerClass, method);
                            REQUEST_MAP.put(request, handler);
                        }
                    }
                }
            }
        }
    }

    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return REQUEST_MAP.get(request);
    }
}
