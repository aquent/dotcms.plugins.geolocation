package com.aquent.viewtools;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
		Logger.info(this, "MaxMind GeoIP Viewtool Starting Up");
		
		// Get the filename from the plugin properties
		String dbFileName = "";
		try {
			dbFileName = pluginAPI.loadProperty("com.aquent.plugins.maxmind", "maxmind.dbFileName");
		} catch (Exception e) {
			Logger.error(this,"Unable to load plugin property - maxmind.dbFileName", e);
			return;
		}
		Logger.info(this, "DB File = "+dbFileName);
		
		// Get the assets path
		String dbPath = "";
		if (UtilMethods.isSet(Config.getStringProperty("ASSET_REAL_PATH"))) {
			dbPath = Config.getStringProperty("ASSET_REAL_PATH") + File.separator + dbFileName;
		} else {
			dbPath = Config.CONTEXT.getRealPath(File.separator + Config.getStringProperty("ASSET_PATH") + File.separator + dbFileName);
		}
		Logger.info(this, "DB Path = "+dbPath);
		
		// Setup the LookupService
		try {
			cityLookup = new LookupService(dbPath, LookupService.GEOIP_MEMORY_CACHE);
		} catch (Exception e) {
			Logger.error(this,"Unable to get a LookupService",e);
			return;
		}
		
		// A flag to let the viewtool know we are good to go
		inited = true;
		Logger.info(this, "Maxmind GeoIP Viewtool Started");
	}
	
	/**
	 * This method takes the ip and returns a map, which contains the following properties:
	 * countryCode, countryName, region, city, postalCode, latitude, longitude, dma_code, area_code, metro_code
	 * 
	 * @param ip the ip to lookup
	 * @return the corresponding Location map or an empty map if the viewtool is not inited.
	 */
	public Map<String, Object> getLocationMap(String ip) {
		Logger.info(this, "Maxmind GeoIP Viewtool - getLocationMap called with ip "+ip);
		
		// Get the Location from the LookupService
		Location loc = null;
		Map<String, Object> locMap = new HashMap<String, Object>();
		if(inited) {
			loc = cityLookup.getLocation(ip);
			locMap.put("countryCode", loc.countryCode);
			locMap.put("countryName", loc.countryName);
			locMap.put("region", loc.region);
			locMap.put("city", loc.city);
			locMap.put("postalCode", loc.postalCode);
			locMap.put("latitude", loc.latitude);
			locMap.put("longitude", loc.longitude);
			locMap.put("dma_code", loc.dma_code);
			locMap.put("area_code", loc.area_code);
			locMap.put("metro_code", loc.metro_code);
		} else {
			Logger.info(this,"Attempt to Call getLocationMap and not inited");
		}
		
		Logger.info(this, "The Location = " + loc.toString());
		
		return locMap;
	}
	
	/**
	 * This method takes the ip and returns the MaxMind Location object
	 * 
	 * @param ip the ip to lookup
	 * @return the corresponding MaxMind Location object or null if the viewtool is not inited.
	 */
	public Location getLocation(String ip) {
	Logger.info(this, "Maxmind GeoIP Viewtool - getLocation called with ip "+ip);
		
		// Get the Location from the LookupService
		Location loc = null;
		if(inited) {
			loc = cityLookup.getLocation(ip);
		} else {
			Logger.info(this,"Attempt to Call getLocation and not inited");
		}
		
		Logger.info(this, "The Location = " + loc.toString());
		
		return loc;	
	}

}
