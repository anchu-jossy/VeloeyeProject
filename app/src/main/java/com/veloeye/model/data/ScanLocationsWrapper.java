package com.veloeye.model.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Florin on 15.01.2016.
 */
public class ScanLocationsWrapper implements Serializable {
    public List<ScanLocation> scans;

    public ScanLocationsWrapper(List<ScanLocation> scans) {
        this.scans = scans;
    }
}
