package org.oracul.service.worker;

import org.apache.log4j.Logger;
import org.oracul.service.builder.PredictionBuilder2D;
import org.oracul.service.util.PredictionQueue;
import org.oracul.service.util.PredictionStatusHolder;
import org.springframework.beans.factory.annotation.Autowired;

public class PredictionTask2D extends PredictionTask {

	int core = 1;

	@Autowired
	private PredictionStatusHolder statusHolder;
	
	@Override
	public void run() {

		executeOracul("dir2d", "com2d");
		System.out.println("store is null" + (store == null) );
		store.putResult(getId(), null);

		//statusHolder.removeStatus(getId());
		executor.unload(core);
	}

	public Integer getLoad() {
		return 1;
	}
}
