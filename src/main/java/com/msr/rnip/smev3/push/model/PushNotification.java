package com.msr.rnip.smev3.push.model;

import com.msr.rnip.smev3.push.config.PushNotificationConfig;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;

/**
 * @author p4r53c
 * @created 05.08.2020
 */
@Entity(name = "PushNotification")
@Table(name = "SMEV_PUSH_NOTICES")
public class PushNotification {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "INTERNAL_ID", unique = true, length = 36)
    private String internalId;

    @Column(name = "REQUEST_ID", unique = false, length = 36, nullable = false)
    private String requestId;

    @Column(name = "RECEIVE_TIMESTAMP", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiveTimestamp;

    @Column(name = "SMEV_INFO_TIMESTAMP", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date smevInfoTimestamp;

    @Column(name = "QUEUE_NAME", nullable = false, length = 500)
    private String queueName;

    @Column(name = "QUEUE_SIZE", nullable = false)
    private BigInteger queueSize;

    public PushNotification() {
    };

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Date getReceiveTimestamp() {
        return receiveTimestamp;
    }

    public void setReceiveTimestamp(Date receiveTimestamp) {
        this.receiveTimestamp = receiveTimestamp;
    }

    public Date getSmevInfoTimestamp() {
        return smevInfoTimestamp;
    }

    public void setSmevInfoTimestamp(Date smevInfoTimestamp) {
        this.smevInfoTimestamp = smevInfoTimestamp;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public BigInteger getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(BigInteger queueSize) {
        this.queueSize = queueSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PushNotification that = (PushNotification) o;
        return Objects.equals(getInternalId(), that.getInternalId()) &&
                Objects.equals(getRequestId(), that.getRequestId()) &&
                Objects.equals(getReceiveTimestamp(), that.getReceiveTimestamp()) &&
                Objects.equals(getSmevInfoTimestamp(), that.getSmevInfoTimestamp()) &&
                Objects.equals(getQueueName(), that.getQueueName()) &&
                Objects.equals(getQueueSize(), that.getQueueSize());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInternalId(), getRequestId(), getReceiveTimestamp(), getSmevInfoTimestamp(), getQueueName(), getQueueSize());
    }
}
