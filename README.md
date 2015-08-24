Geolocation Viewtool
=================================================
A plugin for dotCMS that adds a viewtool enabling the use of MaxMind's (http://www.maxmind.com/) Java based GeoIP2 API (https://github.com/maxmind/GeoIP2-java) and databases for geolocation functionality. The plugin will support both GeoLite2 City and the downloaded paid database versions. You can download the GeoLite2 databases for free at http://dev.maxmind.com/geoip/geoip2/geolite2/.

Installation
-----
* Add a text host vairiable called "Maxmind DB Filename" (maxmindDbFilename)
* Download the Maxmind GeoLite2 City or Paid City Database and put it in your assets directory 
* See the note below about updating your maxmind database automatically
* On your Default Host set the value of the field to the name of the file you placed in your assets directory
* Navigate to the dotCMS Dynamic plugins page: "System" > "Dynamic Plugins"
* Click on "Upload plugin" and select the .jar file located in the "build/jar/" folder

Compatibility
-----
dotCMS 3x.

Location Database Updates
-----
As MaxMind occasionally updates their location databases, you may find it helpful to set up a process to automatically pull fresh copies of the database rather than doing it manually. MaxMind provides instructions for setting up a cron job using a license key to automate that process at http://dev.maxmind.com/geoip/geoipupdate/. This works for both paid and lite versions of the database.

Usage
-----
```velocity
#set($ip     = "199.196.240.241")
## OR, UNCOMMENT TO TRY PULLING THE USER'S IP DYNAMICALLY
##set($ip    = $session.getAttribute("clickstream").remoteAddress)
#set($loc    = $geoip.getLocation($ip))

<dl> 
  <dt>IP:</dt>
  <dd>${ip}</dd>
  <dt>Object:</dt>
  <dd>${loc}</dd>
  <dt>Country:</dt>
  <dd>${loc.getCountry()}</dd>
  <dt>Location:</dt>
  <dd>${loc.getLocation()}</dd>
  <dt>City:</dt>
  <dd>${loc.getCity()}</dd>
  <dt>Postal:</dt>
  <dd>${loc.getPostal()}</dd>
  <dt>Latitutde:</dt>
  <dd>${loc.getLocation().getLatitude()}</dd>
  <dt>Longitude:</dt>
  <dd>${loc.getLocation().getLongitude()}</dd>
  <dt>Subdivisions:</dt>
  <dd>${loc.getSubdivisions()}</dd>
  <dt>Continent:</dt>
  <dd>${loc.getContinent()}</dd>
  <dt>Traits:</dt>
  <dd>${loc.getTraits()}</dd>
</dl>

#set($loc2 = $geoip.getLocation('213.52.50.8'))

<dl> 
  <dt>IP:</dt>
  <dd>${ip}</dd>
  <dt>Object:</dt>
  <dd>${loc}</dd>
  <dt>Country:</dt>
  <dd>${loc.getCountry()}</dd>
  <dt>Location:</dt>
  <dd>${loc.getLocation()}</dd>
  <dt>City:</dt>
  <dd>${loc.getCity()}</dd>
  <dt>Postal:</dt>
  <dd>${loc.getPostal()}</dd>
  <dt>Latitutde:</dt>
  <dd>${loc.getLocation().getLatitude()}</dd>
  <dt>Longitude:</dt>
  <dd>${loc.getLocation().getLongitude()}</dd>
  <dt>Subdivisions:</dt>
  <dd>${loc.getSubdivisions()}</dd>
  <dt>Continent:</dt>
  <dd>${loc.getContinent()}</dd>
  <dt>Traits:</dt>
  <dd>${loc.getTraits()}</dd>
</dl>

## If you have two Location Objects you can get the distance using the Location Objects:
<p>Distance between ${esc.d}loc and ${esc.d}loc2 = ${geoip.distance($loc, $loc2)} miles</p>

## If you have one Location Object and a set of lat,long for a second location you can get the distance using the viewtool helper method:
<p>Distance between ${esc.d}loc and ${esc.d}loc2 = 
${geoip.distance($loc, 
                 $loc2.getLocation().getLatitude(), 
                 $loc2.getLocation().getLongitude())} miles</p>

## If you have two sets of lat,long you can use the viewtool helper method as well:
<p>Distance between ${esc.d}loc and ${esc.d}loc2 = 
${geoip.distance($loc.getLocation().getLatitude(), 
                 $loc.getLocation().getLongitude(), 
                 $loc2.getLocation().getLatitude(), 
                 $loc2.getLocation().getLongitude())} miles</p>
```

Building
--------
* Install Gradle (if not already installed)
* gradle jar 