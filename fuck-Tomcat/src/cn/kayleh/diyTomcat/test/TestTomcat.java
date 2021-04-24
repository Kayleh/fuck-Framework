package cn.kayleh.diyTomcat.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.http.HttpUtil;
import cn.kayleh.diyTomcat.util.MiniBrowser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Wizard
 * @Date: 2020/6/9 13:35
 */
public class TestTomcat {
    //预先定义端口和ip地址，方便修改。
    private static int port = 8888;
    private static String ip = "127.0.0.1";

    //在测试启动之前会先检查diytomcat是否已经启动了，如果未启动，就不用做测试了，并且给出提示信息。
    @BeforeClass
    public static void beforeClass() {
        //所有测试开始前看diy tomcat 是否已经启动了
        if (NetUtil.isUsableLocalPort(port)) {
            System.err.println("请先启动 位于端口: " + port + " 的diy tomcat，否则无法进行单元测试");
            System.exit(1);
        } else {
            System.out.println("检测到 diy tomcat已经启动，下面进行单元测试");
        }
    }


    //测试方法，用于访问： http://127.0.0.1:18080/, 并验证返回值是否是 “Hello DIY Tomcat from how2j.cn”，如果不是就会测试失败。
    @Test
    public void testHelloTomcat() {
        String html = getContentString("/");
        Assert.assertEquals(html, "Hello DIY Tomcat from Kayleh.cn");
    }


    @Test
    public void testaHtml() {
        String html = getContentString("/a.html");
        Assert.assertEquals(html, "Hello DIY Tomcat from a.html");
    }

    /**
     * 因为 Bootstrap 是单线程的，来一个请求，处理一个。 处理完毕之后，才能处理下一个。
     * 所以我们在单元测试里准备一个线程池，同时模仿3个同时访问 timeConsume.html，
     * 正是因为 Bootstrap 是单线程的，所以得一个一个地处理，导致3个同时访问，最后累计时间是 3秒以上。
     *
     * @throws InterruptedException
     */
    @Test
    public void testTimeConsumeHtml() throws InterruptedException {
        //准备一个线程池，里面有20根线程。
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(20, 20, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(10));
        //开始计时
        TimeInterval timeInterval = DateUtil.timer();

        //连续执行3个任务，可以简单地理解成3个任务同时开始
        for (int i = 0; i < 3; i++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    getContentString("/timeConsume.html");
                }
            });
        }
        //shutdown 尝试关闭线程池，但是如果 线程池里有任务在运行，就不会强制关闭，直到任务都结束了，才关闭.
        //awaitTermination 会给线程池1个小时的时间去执行，如果超过1个小时了也会返回，如果在一个小时内任务结束了，就会马上返回。
        //通过这两行代码就可以做到，当3个任务结束时候，才运行后续代码。
        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.HOURS);

        //获取经过了多长时间的毫秒数，并且断言它是超过3秒的。
        long duration = timeInterval.intervalMs();
        Assert.assertTrue(duration < 3000);

    }

//    @Test
//    public void testaIndex() {
//        String html = getContentString("/a.html");
//        Assert.assertEquals(html, "Hello DIY Tomcat from index.html@a");
//    }
//
//    @Test
//    public void testbIndex() {
//        String html = getContentString("/b/index.html");
//        Assert.assertEquals(html, "Hello DIY Tomcat from index.html@b");
//    }

    @Test
    public void testaIndex() {
        String html = getContentString("/a");
        Assert.assertEquals(html, "Hello DIY Tomcat from index.html@a");
    }

    @Test
    public void testbIndex() {
        String html = getContentString("/b/");
        Assert.assertEquals(html, "Hello DIY Tomcat from index.html@b");
    }

    @Test
    public void test404() {
        //访问某个不存在的 html , 然后断言 返回的 http 响应里包含 HTTP/1.1 404 Not Found,
        // 毕竟返回的整个 http 响应那么长，不好用 equals 来比较，只要包含关键的头信息，就算测试通过啦
        String response = getHttpString("/not_exist.html");
        containAssert(response, "HTTP/1.1 404 Not Found");
    }

    @Test
    public void test500() {
        String response = getHttpString("/500.html");
        containAssert(response, "HTTP/1.1 500 Internal Server Error");
    }

    @Test
    public void testaTxt() {
        String response = getHttpString("/a.txt");
        containAssert(response, "Content-Type: text/plain");
    }

    @Test
    public void testPNG() {
        byte[] bytes = getContentBytes("/logo.png");
        int pngFileLength = 1672;
        Assert.assertEquals(pngFileLength, bytes.length);
    }

    @Test
    public void testPDF() {
        String uri = "/etf.pdf";
        String url = StrUtil.format("http://{}:{}{}", ip, port, uri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpUtil.download(url, baos, true);
        int pdfFileLength = 3590775;
        Assert.assertEquals(pdfFileLength, baos.toByteArray().length);
    }

    @Test
    public void testHello() {
        String html = getContentString("/j2ee/hello");
        Assert.assertEquals(html, "Hello DIY Tomcat from HelloServlet");
    }

    @Test
    public void testJavawebHello() {
        String html = getContentString("/javaee/hello");
        containAssert(html, "Hello DIY Tomcat from HelloServlet@javaweb");
    }

    //判断是否为单例
    @Test
    public void testJavawebHelloSingleton() {
        String html1 = getContentString("/javaee/hello");
        String html2 = getContentString("/javaee/hello");
        Assert.assertEquals(html1, html2);
    }

    //增加 get 方式的测试
    @Test
    public void testgetParam() {
        String uri = "/javaee/param";
        String url = StrUtil.format("http://{}:{}{}", ip, port, uri);
        Map<String, Object> params = new HashMap<>();
        params.put("name", "meepo");
        String html = MiniBrowser.getContentString(url, params, true);
        Assert.assertEquals(html, "get name:meepo");
    }

    //增加 post 方式的测试
    @Test
    public void testpostParam() {
        String uri = "/javaee/param";
        String url = StrUtil.format("http://{}:{}{}", ip, port, uri);
        Map<String, Object> params = new HashMap<>();
        params.put("name", "meepo");
        String html = MiniBrowser.getContentString(url, params, false);
        Assert.assertEquals(html, "post name:meepo");
    }

    @Test
    public void testheader() {
        String html = getContentString("/javaee/header");
        Assert.assertEquals(html, "kayleh mini brower / java1.8");
    }

    @Test
    public void testsetCookie() {
        String html = getHttpString("/javaee/setCookie");
        containAssert(html, "Set-Cookie: name=Gareen(cookie); Expires=");
    }

    @Test
    public void testgetCookie() throws IOException {
        String url = StrUtil.format("http://{}:{}{}", ip, port, "/javaee/getCookie");
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestProperty("Cookie", "name=Gareen(cookie)");
        conn.connect();
        InputStream is = conn.getInputStream();
        String html = IoUtil.read(is, "utf-8");
        containAssert(html, "name:Gareen(cookie)");
    }

    //先通过访问 setSession，设置 name_in_session, 并且得到 jsessionid,
    //然后 把 jsessionid 作为 Cookie 的值提交到 getSession，就获取了session 中的数据了。
    @Test
    public void testSession() throws IOException {
        String jsessionid = getContentString("/javaee/setSession");
        if (null != jsessionid)
            jsessionid = jsessionid.trim();
        String url = StrUtil.format("http://{}:{}{}", ip, port, "/javaee/getSession");
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestProperty("Cookie", "JSESSIONID=" + jsessionid);
        conn.connect();
        InputStream is = conn.getInputStream();
        String html = IoUtil.read(is, "utf-8");
        containAssert(html, "Gareen(session)");
    }

    @Test
    public void testGzip() {
        byte[] gzipContent = getContentBytes("/", true);
        byte[] unGzipContent = ZipUtil.unGzip(gzipContent);
        String html = new String(unGzipContent);
        Assert.assertEquals(html, "Hello DIY Tomcat from Kayleh.cn");
    }

    @Test
    public void testJsp() {
        String html = getContentString("/javaee/");
        Assert.assertEquals(html, "hello jsp@javaweb");
    }

    @Test
    public void testClientJump() {
        String http_servlet = getHttpString("/javaee/jump1");
        containAssert(http_servlet, "HTTP/1.1 302 Found");
        String http_jsp = getHttpString("/javaee/jump1.jsp");
        containAssert(http_jsp, "HTTP/1.1 302 Found");
    }

    @Test
    public void testServerJump() {
        String http_servlet = getHttpString("/javaee/jump2");
        containAssert(http_servlet, "Hello DIY Tomcat from HelloServlet");
    }

    @Test
    public void testServerJumpWithAttributes() {
        String http_servlet = getHttpString("/javaee/jump2");
        containAssert(http_servlet, "Hello DIY Tomcat from HelloServlet@javaweb, the name is gareen");
    }

    @Test
    public void testJavaweb0Hello() {
        String html = getContentString("/javaweb0/hello");
        containAssert(html,"Hello DIY Tomcat from HelloServlet@javaweb");
    }
    @Test
    public void testJavaweb1Hello() {
        String html = getContentString("/javaweb1/hello");
        containAssert(html,"Hello DIY Tomcat from HelloServlet@javaweb");
    }





    //增加一个 containAssert 断言，来判断html 里是否包含某段字符串的断言
    private void containAssert(String html, String string) {
        boolean match = StrUtil.containsAny(html, string);
        Assert.assertTrue(match);
    }

    //增加一个 getHttpString 方法来获取 Http 响应
    public String getHttpString(String uri) {
        String url = StrUtil.format("http://{}:{}{}", ip, port, uri);
        String http = MiniBrowser.getHttpString(url);
        return http;
    }

    //准备一个工具方法，用来获取网页返回。
    public String getContentString(String uri) {
        String url = StrUtil.format("http://{}:{}{}", ip, port, uri);
        String content = MiniBrowser.getContentString(url);
        return content;
    }

    private byte[] getContentBytes(String uri) {
        return getContentBytes(uri, false);
    }

    private byte[] getContentBytes(String uri, boolean gzip) {
        String url = StrUtil.format("http://{}:{}{}", ip, port, uri);
        return MiniBrowser.getContentBytes(url, gzip);
    }


}
