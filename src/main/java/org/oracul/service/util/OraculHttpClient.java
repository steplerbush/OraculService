package org.oracul.service.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * Created by Miramax on 09.01.2016.
 */
@Service
public class OraculHttpClient {

    private static final Logger LOG = Logger.getLogger(OraculHttpClient.class);

    private final String USER_AGENT = "Mozilla/5.0";

    public JSONObject sendGetJSONRequest(String requestURL/*, Map<String, String> params*/) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet getRequest = new HttpGet(requestURL);
            getRequest.addHeader("accept", "application/json");
            HttpResponse response = httpClient.execute(getRequest);
            HttpEntity entity = response.getEntity();
            LOG.debug(requestURL + " - getStatusCode: "
                    + response.getStatusLine().getStatusCode()
                    + " ; entity.getContentType(): " + entity.getContentType()
                    + " ; entity.getContent(): " + entity.getContent().toString());
            if (entity.getContentType().toString().contains("application/json")) {
                return new JSONObject(EntityUtils.toString(entity));
            }
        } catch (Exception e) {
            LOG.error("Error while processing request to remote service", e);
            throw new RuntimeException(e);
        }
        return null;
    }

    public File sendGetImageRequest(String requestURL/*, Map<String, String> params*/, String path) {
        File image = new File(path);
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet getRequest = new HttpGet(requestURL);
            HttpResponse response = httpClient.execute(getRequest);
            HttpEntity entity = response.getEntity();
            LOG.debug(requestURL + " - getStatusCode: "
                    + response.getStatusLine().getStatusCode()
                    + " ; entity.getContentType(): " + entity.getContentType());
            BufferedInputStream bis = new BufferedInputStream(entity.getContent());
            image.createNewFile();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(image));
            int inByte;
            while((inByte = bis.read()) != -1) bos.write(inByte);
            bis.close();
            bos.close();
        } catch (Exception e) {
            LOG.error("Error while processing request to remote service", e);
            throw new RuntimeException(e);
        }
        return image;
    }
}
