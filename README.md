Geolocation Viewtool (com.aquent.plugins.maxmind)
=================================================
A plugin for dotCMS that adds a viewtool enabling the use of MaxMind's (http://www.maxmind.com/) Java based GeoIP API (https://github.com/maxmind/geoip-api-java) and databases for geolocation functionality. The plugin will support both Lite and full versions of their location databases. You can download the Lite databases for free at http://dev.maxmind.com/geoip/geolite.

Installation
==================================
* Download and extract the plugin package to your {DOTCMS_ROOT}/plugins directory
* Shut down your server ({DOTCMS_ROOT}/bin/shutdown.[sh|bat])
* On your server, run 'ant clean-plugins deploy-plugins'
* Start your server back up ({DOTCMS_ROOT}/bin/startup.[sh|bat])

Usage:
======
```velocity
#set($ip = "199.196.240.241")
#set($loc = $geoip.getLocationMap($ip))
#set($locObj = $geoip.getLocation($ip))

<p> 
  <b>IP: $ip</b> <br />
  $loc <br />
  Country Code: $loc.countryCode <br />
  Country Name: $loc.countryName <br />
  Region: $loc.region <br />
  City: $loc.city <br />
  Postal Code: $loc.postalCode <br />
  Latitutde: $loc.latitude <br />
  Longitude: $loc.longitude <br />
  DMA Code: $loc.dma_code <br />
  Area Code: $loc.area_code <br />
  Metro Code: $loc.metro_code
</p>

#set($loc2 = $geoip.getLocationMap('213.52.50.8'))
#set($loc2Obj = $geoip.getLocation('213.52.50.8'))

<p> 
  <b>IP: 213.52.50.8</b> <br />
  $loc <br />
  Country Code: $loc2.countryCode <br />
  Country Name: $loc2.countryName <br />
  Region: $loc2.region <br />
  City: $loc2.city <br />
  Postal Code: $loc2.postalCode <br />
  Latitutde: $loc2.latitude <br />
  Longitude: $loc2.longitude <br />
  DMA Code: $loc2.dma_code <br />
  Area Code: $loc2.area_code <br />
  Metro Code: $loc2.metro_code
</p>

<p> Distance = $locObj.distance($loc2Obj) </p>
```