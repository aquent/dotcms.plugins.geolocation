Geolocation Viewtool
=================================================
A plugin for dotCMS that adds a viewtool enabling the use of MaxMind's (http://www.maxmind.com/) Java based GeoIP API (https://github.com/maxmind/geoip-api-java) and databases for geolocation functionality. The plugin will support both Lite and full versions of their location databases. You can download the Lite databases for free at http://dev.maxmind.com/geoip/geolite.

Installation
-----
* Add a text host vairiable called "Maxmind DB Filename" (maxmindDbFilename)
* Download the Maxmind City Lite or Paid Database and put it in your assets directory 
* See the note below about updating your maxmind database automatically
* On your Default Host set the value of the field to the name of the file you placed in your assets directory
* Navigate to the dotCMS Dynamic plugins page: "System" > "Dynamic Plugins"
* Click on "Upload plugin" and select the .jar file located in the "build/jar/" folder
* Click on "Exported Packages" and add the following to the bottom of the list
```
,com.dotmarketing.beans,
com.dotmarketing.plugin.business
```

Compatibility
-----
Known to work with 2.5.1. Should be fully 2.3+ compatible though.

Location Database Updates
-----
As MaxMind occasionally updates their location databases, you may find it helpful to set up a process to automatically pull fresh copies of the database rather than doing it manually. MaxMind provides instructions for setting up a cron job using a license key to automate that process at http://www.maxmind.com/en/license_key. This works for both paid and lite versions of the database.

Usage
-----
```velocity
#set($ip     = "199.196.240.241")
## OR, UNCOMMENT TO TRY PULLING THE USER'S IP DYNAMICALLY
##set($ip    = $session.getAttribute("clickstream").remoteAddress)
#set($loc    = $geoip.getLocationMap($ip))
#set($locObj = $geoip.getLocation($ip))

<dl> 
  <dt>IP:</dt>
  <dd>${ip}</dd>
  <dt>${loc}</dt>
  <dd></dd>
  <dt>Country Code:</dt>
  <dd>${loc.countryCode}</dd>
  <dt>Country Name:</dt>
  <dd>${loc.countryName}</dd>
  <dt>Region:</dt>
  <dd>${loc.region}</dd>
  <dt>City:</dt>
  <dd>${loc.city}</dd>
  <dt>Postal Code:</dt>
  <dd>${loc.postalCode}</dd>
  <dt>Latitutde:</dt>
  <dd>${loc.latitude}</dd>
  <dt>Longitude:</dt>
  <dd>${loc.longitude}</dd>
  <dt>DMA Code:</dt>
  <dd>${loc.dma_code}</dd>
  <dt>Area Code:</dt>
  <dd>${loc.area_code}</dd>
  <dt>Metro Code:</dt>
  <dd>${loc.metro_code}</dd>
</dl>

#set($loc2    = $geoip.getLocationMap('213.52.50.8'))
#set($loc2Obj = $geoip.getLocation('213.52.50.8'))

<dl> 
  <dt>IP:</dt>
  <dd>213.52.50.8}</dd>
  <dt>${loc}</dt>
  <dd></dd>
  <dt>Country Code:</dt>
  <dd>${loc2.countryCode}</dd>
  <dt>Country Name:</dt>
  <dd>${loc2.countryName}</dd>
  <dt>Region:</dt>
  <dd>${loc2.region}</dd>
  <dt>City:</dt>
  <dd>${loc2.city}</dd>
  <dt>Postal Code:</dt>
  <dd>${loc2.postalCode}</dd>
  <dt>Latitutde:</dt>
  <dd>${loc2.latitude}</dd>
  <dt>Longitude:</dt>
  <dd>${loc2.longitude}</dd>
  <dt>DMA Code:</dt>
  <dd>${loc2.dma_code}</dd>
  <dt>Area Code:</dt>
  <dd>${loc2.area_code}</dd>
  <dt>Metro Code:</dt>
  <dd>${loc2.metro_code}</dd>
</dl>

## If you have two Location Objects you can get the distance using the Location Objects:
<p>Distance between ${esc.d}loc and ${esc.d}loc2 = ${locObj.distance($loc2Obj)} miles</p>

## If you have one Location Object and a set of lat,long for a second location you can get the distance using the viewtool helper method:
<p>Distance between ${esc.d}loc and ${esc.d}loc2 = ${geoip.distance($locObj, $loc2.latitude, $loc2.longitude)} miles</p>

## If you have two sets of lat,long you can use the viewtool helper method as well:
<p>Distance between ${esc.d}loc and ${esc.d}loc2 = ${geoip.distance($loc.latitude, $loc.longitude, $loc2.latitude, $loc2.longitude)} miles</p>
```

Building
--------
* Install Gradle (if not already installed)
* gradle jar 