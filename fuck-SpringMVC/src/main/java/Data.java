/**
 * @Description: Data类用于封装Controller方法的JSON返回结果.
 * @Author: Kayleh
 * @Date: 2021/5/30 14:26
 * @Version: 1.0
 */
public class Data {

    /**
     * 模型数据
     */
    private Object model;

    public Data(Object model) {
        this.model = model;
    }

    public Object getModel() {
        return model;
    }
}
