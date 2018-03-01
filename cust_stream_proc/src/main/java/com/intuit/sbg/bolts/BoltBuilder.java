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

    public CustGraphNeo4JBolt buildCustDataBolt() {
        String custGraphApiUrl = configs.getProperty(Keys.CUSTGRAPHNEO4J_BOLT_URL);
        return new CustGraphNeo4JBolt(custGraphApiUrl);
    }

    public ErrorBolt buildErrorDataBolt() {
        return new ErrorBolt();
    }

    public DataNormalizeBolt buildDataNormalizeBolt() {
        return new DataNormalizeBolt();
    }
}