package org.oracul.service.builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.oracul.service.dto.PeriodicalPrediction;
import org.oracul.service.dto.Prediction2D;
import org.oracul.service.service.PeriodicalPredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PeriodicalPredictionBuilder extends PredictionBuilder {
	private static final Logger LOGGER = Logger.getLogger(PeriodicalPredictionBuilder.class);

	@Value("${oracul.periodical.singleton-results}")
	private String periodicalPath;

	@Autowired
	public PeriodicalPredictionBuilder(@Value("${2d.size.u}") Integer uDimension,
			@Value("${2d.size.v}") Integer vDimension) {
		super(uDimension, vDimension);
	}

	@Autowired
	private PeriodicalPredictionService predictionRepository;

	public PeriodicalPrediction buildPrediction(Object object) {
		PeriodicalPrediction prediction = (PeriodicalPrediction) object;
		File root = new File(periodicalPath + "/" + prediction.getId() + "/");
		File[] dirs = root.listFiles();

		List<Prediction2D> data = new ArrayList<>();
		for (File dir : dirs) {
			File uValuesFile = new File(dir.getAbsolutePath() + "/" + "u.out");
			File vValuesFile = new File(dir.getAbsolutePath() + "/" + "v.out");
			double[] u = parseDoubles(uValuesFile);
			double[] v = parseDoubles(vValuesFile);
			Prediction2D prediction2d = new Prediction2D(u, v, getuDimension(), getvDimension());
			prediction2d.setPeriodicalPrediction(prediction);
			try {
				FileUtils.deleteDirectory(dir);
			} catch (IOException e) {
				e.printStackTrace();
			}
			data.add(prediction2d);
		}
		try {
			FileUtils.deleteDirectory(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
		prediction.setPredictions(data);
		return prediction;
	}
}
