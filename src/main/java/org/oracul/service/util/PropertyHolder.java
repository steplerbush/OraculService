package org.oracul.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyHolder {

    
    @Value("${cores.2d}")
    private Integer core2d;
    @Value("${cores.3d}")
    private Integer core3d;

    @Value("${oracul.execute.dir.2d}")
    private String executeOraculDir2D;
    @Value("${oracul.execute.dir.3d}")
    private String executeOraculDir3D;

    @Value("${oracul.execute.command.2d}")
    private String executeOraculCommand2D;
    @Value("${oracul.execute.command.3d}")
    private String executeOraculCommand3D;
    
    @Value("${oracul.periodical.execute.dir}")
	private String executeOraculDirPeriodical;
	@Value("${oracul.periodical.execute.command}")
	private String executeOraculCommandPeriodical;

    @Value("${gpu.oracul.server.address}")
    private String gpuServerAddress;
    @Value("${gpu.oracul.root.suburl}")
    private String gpuRootSubURL;
    @Value("${gpu.oracul.order.suburl}")
    private String gpuOrderSubURL;
    @Value("${gpu.oracul.isready.suburl}")
    private String gpuIsReadySubURL;
    @Value("${gpu.oracul.getimage.suburl}")
    private String gpuGetImageSubURL;
    @Value("${gpu.oracul.release.suburl}")
    private String gpuReleaseSubURL;
    @Value("${gpu.oracul.image.results}")
    private String gpuImageResultsFolder;
    @Value("${gpu.oracul.image.results.format}")
    private String gpuImageResultsFormat;

    public String getExecuteOraculDirPeriodical() {
		return executeOraculDirPeriodical;
	}

	public String getExecuteOraculCommandPeriodical() {
		return executeOraculCommandPeriodical;
	}

	public Integer getCore2d() {
        return core2d;
    }

    public Integer getCore3d() {
        return core3d;
    }

    public String getExecuteOraculDir2D() {
        return executeOraculDir2D;
    }

    public String getExecuteOraculDir3D() {
        return executeOraculDir3D;
    }

    public String getExecuteOraculCommand2D() {
        return executeOraculCommand2D;
    }

    public String getExecuteOraculCommand3D() {
        return executeOraculCommand3D;
    }

    public String getGpuServerAddress() {
        return gpuServerAddress;
    }

    public String getGpuRootSubURL() {
        return gpuRootSubURL;
    }

    public String getGpuOrderSubURL() {
        return gpuOrderSubURL;
    }

    public String getGpuIsReadySubURL() {
        return gpuIsReadySubURL;
    }

    public String getGpuGetImageSubURL() {
        return gpuGetImageSubURL;
    }

    public String getGpuReleaseSubURL() {
        return gpuReleaseSubURL;
    }

    public String getGpuImageResultsFolder() {
        return gpuImageResultsFolder;
    }

    public String getGpuImageResultsFormat() {
        return gpuImageResultsFormat;
    }
}
