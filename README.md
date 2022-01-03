# Environmental-Monitoring-Android-App
This Android App is used to monitor environmental parameters data from remote sensors. Parameters includes but not limited to temperature, humidity, air quality, level of Ionizing radiation, ...

This app communicates with remote server (Raspberry Pi server) which implement a web server (based on Node-Red platform), a database server (InfluxDB) and a MQTT communication server (Mosquito broker).

Sensor data are presented in real-time. User can also visualize historical data in graph.
