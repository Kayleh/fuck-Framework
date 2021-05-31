import java.lang.reflect.Method;

/**
 * @Description: Handler类为一个处理器, 封装了Controller的Class对象和Method方法.
 * @Author: Kayleh
 * @Date: 2021/5/30 14:21
 * @Version: 1.0
 */
public class Handler {
    /**
     * Controller类
     */
    private Class<?> controllerClass;
    private Method controllerMethod;

    public Handler(Class<?> controllerClass, Method controllerMethod) {
        this.controllerClass = controllerClass;
        this.controllerMethod = controllerMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getControllerMethod() {
        return controllerMethod;
    }
}
