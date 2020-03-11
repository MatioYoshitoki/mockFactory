package com.mock.core.pojo;

/**
 * Created by matioyoshitoki on 2020/2/5.
 */
public class ManifestSummaryPo {

    private String manifestID;
    private String manifestType;
    private String manifestName;
    private String port;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getManifestName() {
        return manifestName;
    }

    public void setManifestName(String manifestName) {
        this.manifestName = manifestName;
    }

    public String getManifestID() {
        return manifestID;
    }

    public void setManifestID(String manifestID) {
        this.manifestID = manifestID;
    }

    public String getManifestType() {
        return manifestType;
    }

    public void setManifestType(String manifestType) {
        this.manifestType = manifestType;
    }
}
