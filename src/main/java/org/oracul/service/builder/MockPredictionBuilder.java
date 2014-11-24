package org.oracul.service.builder;

import org.apache.commons.io.FileUtils;
import org.oracul.service.dto.Prediction2D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.Arrays;

@Component
public class MockPredictionBuilder {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private ResourceLoader resourceLoader;

	@Value("60")
	private int uDimension;
	@Value("61")
	private int vDimension;

	private File uValuesPath;

	private File vValuesPath;

	public Prediction2D build2DPrediction() {

		try {
			uValuesPath = resourceLoader.getResource("classpath:mockData/u.out").getFile();
			vValuesPath = resourceLoader.getResource("classpath:mockData/u.out").getFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		double[] u = parseDoubles(uValuesPath);
		double[] v = parseDoubles(vValuesPath);
		return new Prediction2D(u, v, uDimension, vDimension);
	}

	private double[] parseDoubles(File file) {
		try {
			String content = FileUtils.readFileToString(file);
			return Arrays.stream(content.split("\n")).mapToDouble(Double::valueOf).toArray();
		} catch (IOException e) {
			throw new RuntimeException("can't parse file " + file.getName(), e);
		}
	}


	public void setuDimension(int uDimension) {
		this.uDimension = uDimension;
	}

	public void setvDimension(int vDimension) {
		this.vDimension = vDimension;
	}

	public int getuDimension() {
		return uDimension;
	}

	public int getvDimension() {
		return vDimension;
	}

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public File getuValuesPath() {
		return uValuesPath;
	}

	public void setuValuesPath(File uValuesPath) {
		this.uValuesPath = uValuesPath;
	}

	public File getvValuesPath() {
		return vValuesPath;
	}

	public void setvValuesPath(File vValuesPath) {
		this.vValuesPath = vValuesPath;
	}
}


