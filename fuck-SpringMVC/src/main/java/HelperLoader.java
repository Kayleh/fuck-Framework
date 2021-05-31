/**
 * @Description: 到目前为止, 我们创建了ClassHelper, BeanHelper, IocHelper, ControllerHelper这四个Helper类,
 * 我们需要一个入口程序来加载他们(实际上是加载静态代码块),
 * 当然就算没有这个入口程序, 这些类也会被加载, 我们这里只是为了让加载更加集中.
 * @Author: Kayleh
 * @Date: 2021/5/30 15:43
 * @Version: 1.0
 */
public final class HelperLoader {
    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for (Class<?> aClass : classList) {
            ClassUtil.loadClass(aClass.getName());
        }
    }
}
