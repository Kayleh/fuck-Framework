package cn.kayleh.diyTomcat.catalina;

import cn.hutool.log.LogFactory;
import cn.kayleh.diyTomcat.http.Request;
import cn.kayleh.diyTomcat.http.Response;
import cn.kayleh.diyTomcat.util.ThreadPoolUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: Wizard
 * @Date: 2020/6/18 14:31
 */
public class Connector implements Runnable
{
    int port;
    private Service service;

    private String compression;
    private int compressionMinSize;
    private String noCompressionUserAgents;
    private String CompressableMimeType;

    public Connector(Service service)
    {
        this.service = service;
    }

    @Override
    public void run()
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(port);
            //在端口8888上启动ServerSocket。服务端和浏览器通信是通过Socket进行通信的，所以这里需要启动一个 ServerSocket。
            //套了一层循环，处理掉一个Socket链接请求之后，再处理下一个链接请求。
            while (true)
            {
                //表示收到一个浏览器客户端的请求
                Socket accept = serverSocket.accept();
                Runnable runnable = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            Request request = new Request(accept, Connector.this);
                            Response response = new Response();
                            HttpProcessor httpProcessor = new HttpProcessor();
                            httpProcessor.execute(accept, request, response);
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        } finally
                        {
                            if (!accept.isClosed())
                                try
                                {
                                    accept.close();
                                } catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                        }
                    }
                };
                ThreadPoolUtil.run(runnable);
            }
        } catch (IOException e)
        {
            LogFactory.get().error(e);
            e.printStackTrace();
        }
    }

    public void init()
    {
        LogFactory.get().info("Initializing ProtocolHandler [http-bio-{}]", port);
    }

    public void start()
    {
        LogFactory.get().info("Starting ProtocolHandler [http-bio-{}]", port);
        new Thread(this).start();
    }

    public Service getService()
    {
        return service;
    }

    public void setPort(int port)
    {
        this.port = port;
    }


    public String getCompression()
    {
        return compression;
    }

    public void setCompression(String compression)
    {
        this.compression = compression;
    }

    public int getCompressionMinSize()
    {
        return compressionMinSize;
    }

    public void setCompressionMinSize(int compressionMinSize)
    {
        this.compressionMinSize = compressionMinSize;
    }

    public String getNoCompressionUserAgents()
    {
        return noCompressionUserAgents;
    }

    public void setNoCompressionUserAgents(String noCompressionUserAgents)
    {
        this.noCompressionUserAgents = noCompressionUserAgents;
    }

    public String getCompressableMimeType()
    {
        return CompressableMimeType;
    }

    public void setCompressableMimeType(String compressableMimeType)
    {
        this.CompressableMimeType = compressableMimeType;
    }
}
