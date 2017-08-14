package eu.com.cwsfe.cms.web.monitoring;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.time.ZonedDateTime;

/**
 * Created by Radosław Osiński
 */
@Component
class ServerWatch implements InitializingBean {

    private ZonedDateTime startTime;
    private MemoryMXBean memoryMxBean;
    private OperatingSystemMXBean osBean;

    @Override
    public void afterPropertiesSet() throws Exception {
        initialize();
    }

    public void initialize() {
        this.initializeStartTime();
        this.memoryMxBean = ManagementFactory.getMemoryMXBean();
        osBean = ManagementFactory.getOperatingSystemMXBean();
    }

    private void initializeStartTime() {
        this.startTime = ZonedDateTime.now();
    }

    public ZonedDateTime getDateTime() {
        return this.startTime;
    }

    public double availableMemoryInMB() {
        long available = this.memoryMxBean.getHeapMemoryUsage().getCommitted() - this.memoryMxBean.getHeapMemoryUsage().getUsed();
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

    private double asMb(long bytes) {
        return bytes / (double) 1024 / (double) 1024;
    }

}
