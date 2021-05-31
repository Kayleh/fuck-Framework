import java.util.HashMap;
import java.util.Map;

/**
 * @Description: View类用于封装Controller方法的视图返回结果.
 * @Author: Kayleh
 * @Date: 2021/5/30 14:48
 * @Version: 1.0
 */
public class View {
    /**
     * 视图路径
     */
    private String path;
    /**
     * 模型数据
     */
    private Map<String, Object> model;

    public View(String path) {
        this.path = path;
        model = new HashMap<String, Object>();
    }

    public View addModel(String key, Object value) {
        model.put(key, value);
        return this;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
