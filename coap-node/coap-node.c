#include "contiki.h"
#include "coap-engine.h"
#include "coap-blocking-api.h"
#include <stdio.h>
#include "net/ipv6/uip.h"
#include "net/ipv6/uip-ds6.h"
#include "net/ipv6/uip-debug.h"
#include "net/ipv6/uiplib.h"
#include "sys/etimer.h"
#include "os/dev/leds.h"

// Server IP and resource path
#define SERVER_EP "coap://[fd00::1]:5683"
char *service_url = "/registration";

// flag to exit the while cycle and start the server tasks
static bool registered = false;

// Define the resource
extern coap_resource_t res_tent;

// Define a handler to handle the response from the server
void client_chunk_handler(coap_message_t *response){
    if(response == NULL) {
        puts("Request timed out");
        return;
    }
    registered = true;
}

#define LOG_MODULE "App"

PROCESS(client, "client");
AUTOSTART_PROCESSES(&client);
PROCESS_THREAD(client, ev, data){
    static coap_endpoint_t server_ep;
    static coap_message_t request[1]; /* This way the packet can be treated as pointer as usual. */
    static struct etimer et;
    size_t size_addr = 50;
    size_t size_json_message = 200;
    char ip_addr_str[size_addr];
    char json_message[size_json_message];
    uint8_t state;

    PROCESS_BEGIN();

    ///////////////// COAP CLIENT //////////////

    leds_on(LEDS_RED);
    
    // obtain the ip address of the node
    while(1){
        etimer_set(&et, CLOCK_SECOND*5);
        PROCESS_WAIT_EVENT_UNTIL(etimer_expired(&et));
        state = uip_ds6_if.addr_list[1].state;
        if(uip_ds6_if.addr_list[1].isused && (state==ADDR_TENTATIVE || state == ADDR_PREFERRED)){
            uiplib_ipaddr_snprint(ip_addr_str, size_addr, &uip_ds6_if.addr_list[1].ipaddr);
        }
        sprintf(json_message, "{\"ip\":\"%s\"}", ip_addr_str);
        if((int)strlen(ip_addr_str)!=0){
            break;
        }
    }
    
    // Populate the coap_endpoint_t data structure
    coap_endpoint_parse(SERVER_EP, strlen(SERVER_EP),&server_ep);
    
    // Prepare the message
    coap_init_message(request, COAP_TYPE_CON,COAP_POST, 0);
    coap_set_header_uri_path(request, service_url);
    
    // PER IL JSON!!!!
    coap_set_header_content_format(request, APPLICATION_JSON);

    // Set the payload (if needed)
    coap_set_payload(request, (uint8_t *)json_message,strlen(json_message));
    
    // Issue the request in a blocking manner
    // The client will wait for the server to reply(or the transmission to timeout)
    while(registered == false){
        COAP_BLOCKING_REQUEST(&server_ep, request,client_chunk_handler);
    }
    
    //////////////////// COAP SERVER //////////////////

    // Activation of a resource
    coap_activate_resource(&res_tent, "tent");
    
    PROCESS_END();
}