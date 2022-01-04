# Environmental-Monitoring-Android-App
This android app, in combination with the wireless sensor network, is designed to remotly monitor the air quality, the environmental parameters as well as the level of radiation of its surronding area. Data is stored in remote server and retrieved later upon request.

This app communicates with remote server (Raspberry Pi server) which implement a web server (based on Node-Red platform), a database server (InfluxDB) and a MQTT communication server (Mosquito broker).

Sensor data are presented in real-time. User can also visualize historical data in graph.

Here is the screenshot of the differents Android UI screen
![Image1](https://user-images.githubusercontent.com/23704606/147907432-4559b731-3aba-4477-83f0-477bc79bf701.png)

Codes and schematic related to wireless sensors (esp8266 based) can be found here:
https://github.com/tambatra/esp8266-Environmental-Monitoring

