package model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.Date;

public class AbstractModel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    @CreationTimestamp
    private Date creationTime;

    @UpdateTimestamp
    private Date updateTime;


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    /**
     * Gets the model version
     *
     * @return the model version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * Sets the model version
     *
     * @param version the model version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Gets the model creation time
     *
     * @return the model creation time
     */
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * Sets the model creation time
     *
     * @param creationTime the model creation time to set
     */
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * Gets the model update time
     *
     * @return the model update time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * Sets the model update time
     *
     * @param updateTime the model update time to set
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @see Object#toString()
     */


    public String toString() {
        return "Model{" +
                "id=" + id +
                ", creationTime=" + creationTime +
                ", updateTime=" + updateTime +
                '}';
    }
}