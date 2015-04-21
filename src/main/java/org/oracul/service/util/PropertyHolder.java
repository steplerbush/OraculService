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

}
