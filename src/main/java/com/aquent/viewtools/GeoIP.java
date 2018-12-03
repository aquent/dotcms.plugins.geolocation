package com.aquent.viewtools;

import java.io.File;
import java.net.InetAddress;

import org.apache.velocity.tools.view.tools.ViewTool;

import com.dotmarketing.beans.Host;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.plugin.business.PluginAPI;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;
import com.liferay.util.FileUtil;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;

/**
 * @author cfalzone
 *
 * This viewtool provides a method to interface with the MaxMind GeoIP2 City database.
 * You need to specify the maxmind.dbFileName property in your plugin.properties.
 * The plugin assumes you are storing the dbFile in the assets directory.
 *
 * A Free Version of the MaxMind GeoLite2 City Database can be grabbed from:
 * http://dev.maxmind.com/geoip/geoip2/geolite2/
 *
 */
public class GeoIP implements ViewTool {
    private DatabaseReader cityLookup;
    private boolean inited = false;
    PluginAPI pluginAPI = APILocator.getPluginAPI();

    /**
     * Sets up the viewtool.  This viewtool should be application scoped.
     *
     * @param initData Initialization data passed in from velocity.
     */
    @Override
    public void init(Object initData) {
        Logger.info(this, "MaxMind GeoIP Viewtool Starting Up");

        // Get the default host
        Host defaultHost;
        try {
            defaultHost = APILocator.getHostAPI().findDefaultHost(APILocator.getUserAPI().getSystemUser(), false);
        } catch (Exception e) {
            Logger.error(this, "Unable to get the default host", e);
            return;
        }

        Logger.debug(this, "Default Host = " + defaultHost.getHostname());

        // Get the dbFileName from the defualt Host
        String dbFileName = defaultHost.getStringProperty("maxmindDbFilename");
        if (!UtilMethods.isSet(dbFileName)) {
            dbFileName = "GeoLite2-City.mmdb";
        }

        // Get the assets path
        String dbPath = Config.getStringProperty("ASSET_REAL_PATH",
                                                 FileUtil.getRealPath(Config.getStringProperty("ASSET_PATH", null)))
                                                 + File.separator + dbFileName;

        Logger.debug(this, "DB Path = " + dbPath);

        // Setup the DatabaseReader
        try {
            File database = new File(dbPath);
            cityLookup = new DatabaseReader.Builder(database).build();
        } catch (Exception e) {
            Logger.error(this, "Unable to get a LookupService", e);
            return;
        }

        // A flag to let the viewtool know we are good to go
        inited = true;
        Logger.info(this, "MaxMind GeoIP Viewtool Started");
    }

    /**
     * This method takes the ip and returns the MaxMind CityResponse object.
     *
     * @param ip the ip to lookup
     * @return the corresponding MaxMind CityResponse object or null if the viewtool is not inited.
     */
    public CityResponse getLocation(String ip) {
        // Get the Location from the LookupService
        CityResponse response = null;
        if (inited) {
            try {
                InetAddress ipAddress = InetAddress.getByName(ip);
                response = cityLookup.city(ipAddress);
            } catch (Exception e) {
                Logger.error(this, "Could not get location from ip address: " + ip, e);
            }
        } else {
            Logger.info(this, "Attempt to Call getLocation and not inited");
        }

        return response;
    }

    /**
     * Helper method to get a distance between 2 MaxMind CityReponse Objects.
     *
     * @param loc The first CityReponse object.
     * @param loc2 The second CityReponse object.
     * @return the distance between loc and loc2 in miles.
     */
    public double distance(CityResponse loc, CityResponse loc2) {
        return distance(loc.getLocation().getLatitude(), loc.getLocation().getLongitude(),
                        loc2.getLocation().getLatitude(), loc2.getLocation().getLongitude());
    }

    /**
     * Helper method to get a distance from a MaxMind CityReponse Object and known latitude and longitude.
     *
     * @param loc the MaxMind CityReponse object to compare against
     * @param lat the latitude of the second location
     * @param lon the longitude of the second location
     * @return the distance in miles between loc and the location represented by lat,long in miles.
     */
    public double distance(CityResponse loc, double lat, double lon) {
        return distance(loc.getLocation().getLatitude(), loc.getLocation().getLongitude(), lat, lon);
    }

    /**
     * Helper method to get a distance between known latitudes and longitudes.
     *
     * @param lat the latitude of the first location
     * @param lon the longitude of the first location
     * @param lat2 the latitude of the second location
     * @param lon2 the longitude of the second location
     * @return the distance in miles between the two locations
     */
    public double distance(double lat, double lon, double lat2, double lon2) {
        return distance(lat, lon, lat2, lon2, 'M');
    }

    /**
     * Computes a distance between two locations.
     *
     * @param lat1 The first latitude.
     * @param lon1 The first longitude.
     * @param lat2 The second latitude.
     * @param lon2 The second longitiude.
     * @param unit 'M' for miles, 'K' for Kilometers, 'N' for nautical miles.
     * @return The distance between the two locations in the specified unit.
     */
    public double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                        * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /**
     * This function converts decimal degrees to radians.
     *
     * @param deg the degrees.
     * @return the radians.
     */
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /**
     * This function converts radians to decimal degrees.
     *
     * @param rad The radians.
     * @return The degrees.
     */
    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
