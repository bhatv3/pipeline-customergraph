package com.intuit.sbg.bolts;

/**
 * Created by vikasbhat on 2/25/18.
 */

import com.intuit.sbg.Keys;

import java.util.Properties;


public class BoltBuilder {

    public Properties configs = null;

    public BoltBuilder(Properties configs) {
        this.configs = configs;
    }

    public SinkTypeBolt buildSinkTypeBolt() {
        return new SinkTypeBolt();
    }

    public CustDataBolt buildCustDataBolt() {
        String custGraphApiUrl = configs.getProperty(Keys.CUST_GRAPH_API_URL);
        return new CustDataBolt(custGraphApiUrl);
    }

    public OtherDataBolt buildOtherDataBolt() {
        return new OtherDataBolt();
    }
}