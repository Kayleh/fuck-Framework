import java.util.Map;

/**
 * @Description: Param类用于封装Controller方法的参数.
 * @Author: Kayleh
 * @Date: 2021/5/30 14:46
 * @Version: 1.0
 */
public class Param {

    private Map<String, Object> paramMap;

    public Param() {
    }

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public boolean isEmpty() {
        return MapUtils.isEmpty(paramMap);
    }
}
