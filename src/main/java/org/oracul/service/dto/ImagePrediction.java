package org.oracul.service.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Miramax on 09.01.2016.
 */
@Entity
@Table(name = "IMAGE_PREDICTION")
public class ImagePrediction {

    @Id
    private UUID id;

    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "request_date")
    private Date date;

    public ImagePrediction() {
    }

    public ImagePrediction(UUID id, Integer statusId) {
        this.setId(id);
        setDate(new Date());
        this.setStatusId(statusId);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
