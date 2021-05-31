/**
 * @Description: 请求类中的方法和路径对应 @RequestMapping 注解里的方法和路径.
 * @Author: Kayleh
 * @Date: 2021/5/30 13:57
 * @Version: 1.0
 */
public class Request {
    /**
     * 请求方法
     */
    private String requestMethod;
    /**
     * 请求路径
     */
    private String requestPath;

    public Request(String requestMethod, String requestPath) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request)) return false;
        Request request = (Request) o;
        return request.getRequestPath().equals(this.requestPath) && request.getRequestMethod().equals(this.requestMethod);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + requestMethod.hashCode();
        result = 31 * result + requestPath.hashCode();
        return result;
    }
}
