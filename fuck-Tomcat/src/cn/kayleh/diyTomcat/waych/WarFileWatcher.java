package cn.kayleh.diyTomcat.waych;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.WatchUtil;
import cn.hutool.core.io.watch.Watcher;
import cn.kayleh.diyTomcat.catalina.Host;
import cn.kayleh.diyTomcat.util.Constant;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

/**
 * 监控 webapps 目录， 当发现新创建了 war 文件的时候，就调用 host 现成的 loadWar 方法即可。
 *
 * @Author: Wizard
 * @Date: 2020/6/25 21:11
 */
public class WarFileWatcher
{
    private WatchMonitor monitor;

    public WarFileWatcher(Host host)
    {
        this.monitor = WatchUtil.createAll(Constant.webappsFolder, 1, new Watcher()
        {
            private void dealWith(WatchEvent<?> event, Path currentPath)
            {
                synchronized (WarFileWatcher.class)
                {
                    String fileName = event.context().toString();
                    if (fileName.toLowerCase().endsWith(".war") && ENTRY_CREATE.equals(event.kind()))
                    {
                        File warFile = FileUtil.file(Constant.webappsFolder, fileName);
                        host.loadWar(warFile);
                    }
                }
            }

            @Override
            public void onCreate(WatchEvent<?> event, Path currentPath)
            {
                dealWith(event, currentPath);
            }

            @Override
            public void onModify(WatchEvent<?> event, Path currentPath)
            {
                dealWith(event, currentPath);

            }

            @Override
            public void onDelete(WatchEvent<?> event, Path currentPath)
            {
                dealWith(event, currentPath);
            }

            @Override
            public void onOverflow(WatchEvent<?> event, Path currentPath)
            {
                dealWith(event, currentPath);
            }

        });
    }

    public void start()
    {
        monitor.start();
    }

    public void stop()
    {
        monitor.interrupt();
    }

}