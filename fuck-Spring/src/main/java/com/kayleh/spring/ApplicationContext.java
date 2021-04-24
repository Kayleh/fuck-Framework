package com.kayleh.spring;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Kayleh
 * @Date: 2021/4/23 0:22
 */
public class ApplicationContext
{
    //配置类
    private Class configClass;

    //存放BeanDefinition的Map集合
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap();
    //单例池
    private Map<String, Object> singletonObjects = new ConcurrentHashMap();
    //存放beanPostProcessor
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<BeanPostProcessor>();

    //创建构造方法给属性赋值
    public ApplicationContext(Class configClass)
    {
        this.configClass = configClass;

        //扫描，得到class
        List<Class> classList = scan(configClass);

        //解析这些类----->BeanDefinition保存bean信息------>存到beanDefinitionMap
        //过滤
        for (Class clazz : classList)
        {
            if (clazz.isAnnotationPresent(Component.class))
            {
                //有没有实现beanPostProcessor
                if (BeanPostProcessor.class.isAssignableFrom(clazz))
                {
                    try
                    {
                        BeanPostProcessor instance = (BeanPostProcessor) clazz.getDeclaredConstructor().newInstance();
                        beanPostProcessorList.add(instance);
                    } catch (InstantiationException e)
                    {
                        e.printStackTrace();
                    } catch (IllegalAccessException e)
                    {
                        e.printStackTrace();
                    } catch (InvocationTargetException e)
                    {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e)
                    {
                        e.printStackTrace();
                    }
                }
                BeanDefinition beanDefinition = new BeanDefinition();
                Component componentAnnotation = (Component) clazz.getAnnotation(Component.class);
                String beanName = componentAnnotation.value();
                if (beanName.equals(""))
                {
                    beanName = clazz.getSimpleName();
                }


                if (clazz.isAnnotationPresent(Scope.class))
                {
                    Scope scopeAnnotation = (Scope) clazz.getAnnotation(Scope.class);
                    beanDefinition.setScope(scopeAnnotation.value());
                } else
                {
                    //单例
                    beanDefinition.setScope("singleton");
                }

                beanDefinition.setBeanClass(clazz);
                beanDefinitionMap.put(beanName, beanDefinition);
            }

        }
        //基于class去创建单例bean
        instanceSingletonBean();
    }

    //实例化单例bean
    private void instanceSingletonBean()
    {
        for (String beanName : beanDefinitionMap.keySet())
        {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton"))
            {
                //创建Bean,判断单例池里是否存在这个bean
                if (!singletonObjects.containsKey(beanName))
                {
                    //没有在单例池里面，创建bean对象
                    Object bean = doCreateBean(beanName, beanDefinition);
                    //放进单例池里面
                    singletonObjects.put(beanName, bean);
                }

            }
        }
    }

    //创建Bean
    public Object doCreateBean(String beanName, BeanDefinition beanDefinition)
    {
        try
        {
            //1.实例化
            Class beanClass = beanDefinition.getBeanClass();
            Object bean = beanClass.getDeclaredConstructor().newInstance();

            //2.属性填充  依赖注入
            //得到属性
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields)
            {
                if (field.isAnnotationPresent(Autowired.class))
                {
                    //属性赋值
                    //用属性的名字去得到一个bean
                    Object fieldValue = getBean(field.getName());
                    field.setAccessible(true);
                    /**
                     * set() ---> obj:赋值给哪一个实例,
                     *       --->value:赋什么值
                     */
                    field.set(bean, fieldValue);
                }
            }

            //3.Aware
            if (bean instanceof BeanNameAware)
            {
                ((BeanNameAware) bean).setBeanName(beanName);
            }


            //初始化之前
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList)
            {
                beanPostProcessor.postProcessorBeforeInitialization(bean, beanName);
            }

            //4.初始化
            if (bean instanceof InitializingBean)
            {
                ((InitializingBean) bean).afterPropertiesSet();
            }

            //初始化之后
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList)
            {
                bean = beanPostProcessor.postProcessorAfterInitialization(bean, beanName);
            }


            return bean;
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (InvocationTargetException e)
        {
            e.printStackTrace();
        } catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    //扫描方法
    private List<Class> scan(Class configClass)
    {
        List<Class> classList = new ArrayList<Class>();
        ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
        //得到扫描路径，
        //注意得到路径下的文件是java文件，需要扫描的是class文件
        String path = componentScanAnnotation.value();//com.kayleh.demo.service
        //替换'.'为'/'
        path = path.replace(".", "/");//com/kayleh/demo/service

        //利用app加载器得到target目录下的classes路径
        ClassLoader classLoader = ApplicationContext.class.getClassLoader();
        //需要扫描的class文件的所在位置
        URL resource = classLoader.getResource(path);
        File file = new File(resource.getFile());// 获取文件夹：com/kayleh/demo/service文件夹
        if (file.isDirectory())
        {
            //列出所有文件
            File[] files = file.listFiles();
            for (File f : files)
            {
                //获取加载器需要的路径格式com.kayleh.demo.service.User
                String loadPath = path.replace("/", ".") + "." + f.getName();
                loadPath = loadPath.substring(0, loadPath.indexOf(".class"));
                //System.out.println(loadPath);
                try
                {
                    //加载class
                    Class<?> clazz = classLoader.loadClass(loadPath);
                    //添加到list里面
                    classList.add(clazz);
                } catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return classList;
    }

    //获取bean
    public Object getBean(String beanName)
    {
        //beanName 根据BeanDefinition来找bean是单例的还是原型
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition.getScope().equals("prototype"))
        {
            //原型
            //创建bean
            return doCreateBean(beanName, beanDefinition);
        } else if (beanDefinition.getScope().equals("singleton"))
        {
            Object bean = singletonObjects.get(beanName);
            /**
             * 如果 先初始化实例化UserService，先去doCreateBean()创建UserService的bean，
             * 在属性填充调用getBean时，User这个Bean还没创建，还没在单例池里面。
             * 所以在这里要判断
             */
            if (bean == null)
            {
                Object bean1 = doCreateBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean1);
                return bean1;
            }
            //从单例池里面去拿bean
            return bean;
        }
        return null;
    }
}
