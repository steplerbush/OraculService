package org.oracul.service.controller;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.oracul.service.dto.ImagePrediction;
import org.oracul.service.dto.Prediction2D;
import org.oracul.service.dto.Prediction3D;
import org.oracul.service.dto.PredictionStatus;
import org.oracul.service.executor.PredictionExecutor;
import org.oracul.service.service.Prediction2DService;
import org.oracul.service.service.Prediction3DService;
import org.oracul.service.task.PredictionTaskCreator;
import org.oracul.service.util.IntegrationFacade;
import org.oracul.service.util.PredictionsStatusesHolder;
import org.oracul.service.util.exception.PredictionNotFoundException;
import org.oracul.service.util.exception.QueueOverflowException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/prediction")
public class SingletonPrognosisController {

	private static final Logger LOGGER = Logger.getLogger(SingletonPrognosisController.class);

	@Autowired
	private PredictionTaskCreator predictionTaskCreator;

	@Autowired
	private PredictionExecutor executor;

	@Autowired
	private Prediction2DService prediction2dRepository;

	@Autowired
	private Prediction3DService prediction3dRepository;

	@Autowired
	private PredictionsStatusesHolder statusHolder;

	@Autowired
	private IntegrationFacade facade;

	@RequestMapping(value = "/order/2d", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Object orderPrediction2D(HttpServletRequest request) throws InterruptedException {
		if (facade.getQueue().size() >= facade.getQueue().getMaxSize()) {
			LOGGER.debug("Queue is overloaded. Task is rejected.");
			throw new QueueOverflowException();
		}
		Long taskID = predictionTaskCreator.createPrediction(PredictionTaskCreator.PredictionType.TASK_2D,
				new String[] { "some params for 2d" });
		return request.getRequestURL().substring(0, request.getRequestURL().indexOf("order")) + "2d/" + taskID;
	}

	@RequestMapping(value = "/order/3d", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Object orderPrediction3D(HttpServletRequest request) throws InterruptedException {
		if (facade.getQueue().size() >= facade.getQueue().getMaxSize() - facade.getProperty().getCore3d()) {
			LOGGER.debug("Queue is overloaded. Task is rejected.");
			throw new QueueOverflowException();
		}
		Long taskID = predictionTaskCreator.createPrediction(PredictionTaskCreator.PredictionType.TASK_3D,
				new String[] { "some params for 3d" });
		return request.getRequestURL().substring(0, request.getRequestURL().indexOf("order")) + "3d/" + taskID;
	}

	@RequestMapping(value = "/2d/{id}", method = RequestMethod.GET)
	public Object getPrediction2D(@PathVariable("id") Long id) {
		Prediction2D prediction = prediction2dRepository.findById(id);
		if (prediction != null && prediction.getU() != null) {
			return prediction;
		} else {
			return statusHolder.checkStatus(id);
		}
	}

	@RequestMapping(value = "/3d/{id}", method = RequestMethod.GET)
	public Object getPrediction3D(@PathVariable("id") Long id) {
		Prediction3D prediction = prediction3dRepository.findById(id);
		if (prediction != null && prediction.getLevels().size() != 0) {
			return prediction;
		} else {
			return statusHolder.checkStatus(id);
		}
	}

	@RequestMapping(value = "/order/image", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Map<String, Object> orderFile() throws InterruptedException {
		JSONObject jsonObject = facade.getOraculHttpClient().sendGetJSONRequest(facade.getProperty().getGpuServerAddress()
				+ facade.getProperty().getGpuRootSubURL() + facade.getProperty().getGpuOrderSubURL());
		UUID id = UUID.fromString(jsonObject.getString("id"));
		Long wait = jsonObject.getLong("wait");
		LOGGER.debug("Received id=" + id + ", time to wait is = " + wait);
		//ImagePrediction imagePrediction = new ImagePrediction(id,PredictionStatus.PENDING.ordinal());
		//facade.getImagePredictionRepository().createPrediction(imagePrediction);
		Map<String, Object> returnParams = new HashMap<>();
		returnParams.put("id", id);
		returnParams.put("wait", wait);
		return returnParams;
	}

	//Возвращать на лайфрей по ордеру гуид и время ожидания. там в джаваскрипте єто отобразить и сделать таймер
	//после которого автоматически виполнится дополнительний запрос на isready. и так в цикле. пока не будет результата.
	// как только картинка будет готова - появится кнопка для загрузки картинки.
	//разделить запрос на несколько.
	@RequestMapping(value = "/get/image/{id}", method = RequestMethod.GET, produces = "image/jpg")
	public @ResponseBody byte[] getFile(@PathVariable("id") UUID id) throws InterruptedException {
		File image = null;
		try {
			JSONObject jsonObject;
			boolean receivedLocal = true;
			image = new File(facade.getProperty().getGpuImageResultsFolder() + id + facade.getProperty().getGpuImageResultsFormat());
			if (!image.exists()) {
				LOGGER.debug("Image "+ id + " does not exist on local machine. trying to request in from remote service");
				image = getFileFromRemote(id);
				LOGGER.debug(image.getAbsolutePath());
				receivedLocal = false;
			}
			byte[] imageout = new byte[0];
			if (image.exists()) {
				imageout = sendImageToOutput(image);
			} else {
				LOGGER.error("Image " + id + "didnot created in filesystem");
			}
			//--end of third request - returned image
			if (!receivedLocal) {
				jsonObject = facade.getOraculHttpClient().sendGetJSONRequest(facade.getProperty().getGpuServerAddress()
						+ facade.getProperty().getGpuRootSubURL()
						+ facade.getProperty().getGpuReleaseSubURL()
						+ id);
				if (!"SUCCESS".equals(jsonObject.getString("status"))) {
					LOGGER.error("Image file " + id + " was not deleted from remote service!");
				}
			}
			return imageout;
		} catch(Exception e) {
			LOGGER.error("Error while processing image request", e);
			if (image != null) {
				Path fp = image.toPath();
				try {
					Files.delete(fp);
				} catch (IOException e1) {
					LOGGER.error("bugged image was not removed - error",e1);
				}
			}
			throw new PredictionNotFoundException();
		}
	}

	private byte[] sendImageToOutput(File image) {
		byte[] imageout;
		try (InputStream is = new FileInputStream(image);
			 ByteArrayOutputStream bao = new ByteArrayOutputStream()) {
            BufferedImage img = ImageIO.read(is);
            //ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", bao);
            imageout = bao.toByteArray();
            bao.flush();
            bao.close();
        } catch (IOException e) {
            LOGGER.error("Error while making image response", e);
            throw new RuntimeException(e);
        }
		return imageout;
	}

	private File getFileFromRemote(UUID id) throws InterruptedException {
		JSONObject jsonObject;
		File image;
		Long wait;
		int numberOfAttemts = 0;
		while (numberOfAttemts < 3) {
            jsonObject = facade.getOraculHttpClient().sendGetJSONRequest(facade.getProperty().getGpuServerAddress()
                    + facade.getProperty().getGpuRootSubURL() + facade.getProperty().getGpuIsReadySubURL() + id);
            String status = jsonObject.getString("status");
            if ("NOT_CREATED".equals(status)) {
                LOGGER.debug("NOT_CREATED: image " + id + " has not been created yet");
                Thread.sleep(300);
            } else if ("READY".equals(status)) {
                LOGGER.debug("READY: image " + id + " has been created");
                break;
            } else if ("IN_PROCESSING".equals(status) || "IN_QUEUE".equals(status) || "READY_FOR_QUEUE".equals(status)) {
                wait = jsonObject.getLong("wait");
                LOGGER.debug(status + ": image " + id + " waiting " + wait);
                Thread.sleep(wait);
            }
            numberOfAttemts++;
            if (numberOfAttemts == 5) {
                LOGGER.error("Order did not fulfilled after 5 attempts. Deleting order from db");
                //imagePrediction.setStatusId(PredictionStatus.FAILED.ordinal());
                //facade.getImagePredictionRepository().deletePrediction(id);
                throw new RuntimeException("Order did not fulfilled after 5 attempts. Deleting order from db");
            }
        }

		//-- end of second request
		//request file from remote
		image = facade.getOraculHttpClient().sendGetImageRequest(facade.getProperty().getGpuServerAddress()
                        + facade.getProperty().getGpuRootSubURL()
                        + facade.getProperty().getGpuGetImageSubURL()
                        + id,
                facade.getProperty().getGpuImageResultsFolder()
                        + id
                        + facade.getProperty().getGpuImageResultsFormat());
		return image;
	}
}
