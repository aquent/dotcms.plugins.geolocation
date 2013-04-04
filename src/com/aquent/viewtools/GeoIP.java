package com.aquent.viewtools;

import java.io.File;

import org.apache.velocity.tools.view.tools.ViewTool;

import com.dotmarketing.business.APILocator;
import com.dotmarketing.plugin.business.PluginAPI;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

/**
 * 
 * @author cfalzone
 *
 * This viewtool provides a method to interface with the MaxMind GoeIP City database.
 * You need to specify the maxmind.dbFileName property in your plugin.properties.
 * The plugin assumes you are storing the dbFile in the assets directory.
 * A Free Version of the MaxMind GeoIP City Database can be grabbed from:
 * http://dev.maxmind.com/geoip/geolite
 * 
 */
public class GeoIP implements ViewTool {

	private LookupService cityLookup;
	private boolean inited = false;
	PluginAPI pluginAPI = APILocator.getPluginAPI(); 
	
	/**
	 * Sets up the viewtool.  This viewtool should be application scoped.
	 */
	@Override
	public void init(Object initData) {
		
		// Get the filename from the plugin properties
		String dbFileName = "";
		try {
			dbFileName = pluginAPI.loadPluginConfigProperty("com.aquent", "maxmind.dbFileName");
		} catch (Exception e) {
			Logger.error(this,"Unable to load plugin property - maxmind.dbFileName", e);
			return;
		}
		
		// Get the assets path
		String dbPath = "";
		if (UtilMethods.isSet(Config.getStringProperty("ASSET_REAL_PATH"))) {
			dbPath = Config.getStringProperty("ASSET_REAL_PATH") + File.separator + dbFileName;
		} else {
			dbPath = Config.CONTEXT.getRealPath(File.separator + Config.getStringProperty("ASSET_PATH") + File.separator + dbFileName);
		}
		
		// Setup the LookupService
		try {
			cityLookup = new LookupService(dbPath, LookupService.GEOIP_MEMORY_CACHE);
		} catch (Exception e) {
			Logger.error(this,"Unable to get a LookupService",e);
			return;
		}
		
		// A flag to let the viewtool know we are good to go
		inited = true;
	}
	
	/**
	 * This method takes the ip and returns the corresponding MaxMind Location object, which contains the following properties
	 * countryCode, countryName, region, city, postalCode, latitude, longitude, dma_code, area_code, metro_code
	 * 
	 * @param ip the ip to lookup
	 * @return the corresponding MaxMind Location object or null if the viewtool is not inited.
	 */
	public Location getLocationMap(String ip) {
		
		// Get the Location from the LookupService
		Location loc = null;
		if(inited) {
			loc = cityLookup.getLocation(ip);
		} else {
			Logger.info(this,"Attempt to Call getLocation and not inited");
		}
		
		return loc;
	}

}
