package com.aquent.viewtools;

import org.apache.velocity.tools.view.context.ViewContext;
import org.apache.velocity.tools.view.servlet.ServletToolInfo;

/**
 * @author cfalzone
 *
 * Viewtool Info used to initialize the viewtool.
 */
public class GeoIPInfo extends ServletToolInfo {

    @Override
    public String getKey() {
        return "geoip";
    }

    @Override
    public String getScope() {
        return ViewContext.APPLICATION;
    }

    @Override
    public String getClassname() {
        return GeoIP.class.getName();
    }

    @Override
    public Object getInstance(Object initData) {

        GeoIP viewTool = new GeoIP();
        viewTool.init(initData);

        setScope(ViewContext.APPLICATION);

        return viewTool;
    }

}
