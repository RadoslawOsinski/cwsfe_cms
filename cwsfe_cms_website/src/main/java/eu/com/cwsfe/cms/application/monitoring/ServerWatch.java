package eu.com.cwsfe.cms.application.monitoring;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.time.ZonedDateTime;

/**
 * Created by Radosław Osiński
 */
@Component
public class ServerWatch {

    private ZonedDateTime startTime;
    private MemoryMXBean memoryMxBean;
    private OperatingSystemMXBean osBean;

    @PostConstruct
    public void initialize() {
        this.initializeStartTime();
        this.memoryMxBean = ManagementFactory.getMemoryMXBean();
        osBean = ManagementFactory.getOperatingSystemMXBean();
    }

    void initializeStartTime() {
        this.startTime = ZonedDateTime.now();
    }

    public ZonedDateTime getDateTime() {
        return this.startTime;
    }

    public double availableMemoryInMB() {
        long available = (this.memoryMxBean.getHeapMemoryUsage().getCommitted() - this.memoryMxBean.getHeapMemoryUsage().getUsed());
        return asMb(available);
    }

    public double usedMemoryInMb() {
        return asMb(this.memoryMxBean.getHeapMemoryUsage().getUsed());
    }

    public String getOSName() {
        return osBean.getName();
    }

    public String getOSVersion() {
        return osBean.getVersion();
    }

    public String getArchitecture() {
        return osBean.getArch();
    }

    public int getAvailableCPUs() {
        return osBean.getAvailableProcessors();
    }

    public double getSystemLoadAverage() {
        return osBean.getSystemLoadAverage();
    }

    double asMb(long bytes) {
        return bytes / (double) 1024 / (double) 1024;
    }

}
