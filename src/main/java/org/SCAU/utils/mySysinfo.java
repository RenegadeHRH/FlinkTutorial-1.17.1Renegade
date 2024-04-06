package org.SCAU.utils;

import lombok.SneakyThrows;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class mySysinfo {
    public static void printMemInfo(){
        ExecutorService service =  Executors.newSingleThreadScheduledExecutor();
        final long[] bytesWritten = {0};
        final long[] bytesRead = {0};
        service.execute(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while(true) {
                    SystemInfo systemInfo = new SystemInfo();
                    OperatingSystem os = systemInfo.getOperatingSystem();
                    int processId = os.getProcessId();
                    OSProcess process = os.getProcess(processId);
                    long curBytesWritten = process.getBytesWritten();
                    long curBytesRead = process.getBytesRead();
                    if (bytesWritten[0] == 0 ){
                        bytesWritten[0] = curBytesWritten;
                    }

                    if (bytesRead[0] == 0 ){
                        bytesRead[0] = curBytesRead;
                    }

                    System.out.printf("pid %d 当前进程：占用内存 %s | 占用CPU(%%) %.2f | 写入(bytes) %d | 读取(bytes) %d%n \n", processId, FormatUtil.formatBytes(process.getResidentSetSize()), getProcessCpuLoad(),
                            curBytesWritten - bytesWritten[0],
                            curBytesRead - bytesRead[0]);
                    bytesWritten[0] = curBytesWritten;
                    bytesRead[0] = curBytesRead;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.printf("性能监控时出现异常：{} \n",e);
                    }
                }
            }
        });

    }
    public static double getProcessCpuLoad() throws Exception {

        MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
        ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
        AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });

        if (list.isEmpty()) {
            return Double.NaN;
        }

        Attribute att = (Attribute)list.get(0);
        Double value  = (Double)att.getValue();

        // usually takes a couple of seconds before we get real values
        if (value == -1.0) {
            return Double.NaN;
        }
        // returns a percentage value with 1 decimal point precision
        return ((int)(value * 1000) / 10.0);
    }

}
