1. Create a border router
2. Set the Border-Router IP on mqtt-server (not sure)
3. Expose the border router and activate the tunslip
4. Create the mqtt-server mote
5. Subscribe to a topic using mosquitto_sub -h IP_BROKER -t TOPIC
6. Publish on a topic using mosquitto_pub -h IP_BROKER -t TOPIC -m MESSAGE
