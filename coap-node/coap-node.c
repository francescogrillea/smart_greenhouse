#include "contiki.h"
#include "contiki-net.h"
#include "coap-engine.h"
#include "coap-blocking-api.h"
#include <stdio.h>
#include "net/routing/routing.h"
#include "net/ipv6/uip.h"
#include "net/ipv6/uip-ds6.h"
#include "net/ipv6/uip-debug.h"
#include "net/ipv6/uiplib.h"
#include "sys/etimer.h"
#include "os/dev/leds.h"

// Server IP and resource path
#define SERVER_EP "coap://[fd00::1]"
#define TOGGLE_INTERVAL 10
char *service_url = "/registration";
#define SIZE_ADDR 50
#define SIZE_JSON 200

// flag to exit the while cycle and start the server tasks
static bool registered = false;

// Define the resource
extern coap_resource_t res_tent;
static struct etimer et;
static coap_endpoint_t server_ep;
static coap_message_t request[1]; /* This way the packet can be treated as pointer as usual. */

// utility
static uint8_t state;
static char ip_addr_str[SIZE_ADDR];
static char json_message[SIZE_JSON];

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
    PROCESS_BEGIN();

    ///////////////// COAP CLIENT //////////////

    leds_on(LEDS_RED);
    
    // obtain the ip address of the node
    while(1){
        printf("while\n");
        etimer_set(&et, CLOCK_SECOND*5);
        PROCESS_WAIT_EVENT_UNTIL(etimer_expired(&et));
        state = uip_ds6_if.addr_list[1].state;
        if(uip_ds6_if.addr_list[1].isused && (state==ADDR_TENTATIVE || state == ADDR_PREFERRED)){
            uiplib_ipaddr_snprint(ip_addr_str, SIZE_ADDR, &uip_ds6_if.addr_list[1].ipaddr);
            sprintf(json_message, "{\"ip\":\"%s\"}", ip_addr_str);
                if((int)strlen(ip_addr_str)!=0){
                break;
            }
        }
    }
    printf("ip: %s\n",ip_addr_str);
    
    // Populate the coap_endpoint_t data structure
    coap_endpoint_parse(SERVER_EP, strlen(SERVER_EP),&server_ep);
    
    
    etimer_set(&et, TOGGLE_INTERVAL * CLOCK_SECOND);
    while(registered == false){
        PROCESS_YIELD();
        if(etimer_expired(&et)){
            printf("-- timer expired -- \n");
            // Prepare the message
            coap_init_message(request, COAP_TYPE_CON,COAP_POST, 0);
            coap_set_header_uri_path(request, service_url);

            // PER IL JSON!!!!
            coap_set_header_content_format(request, APPLICATION_JSON);

            // Set the payload (if needed)
            coap_set_payload(request, (uint8_t *)json_message,strlen(json_message));

            // Issue the request in a blocking manner
            // The client will wait for the server to reply(or the transmission to timeout)
            COAP_BLOCKING_REQUEST(&server_ep, request,client_chunk_handler);
            printf("-- request sent --\n");
            etimer_set(&et, TOGGLE_INTERVAL * CLOCK_SECOND);
        }
    }
    //////////////////// COAP SERVER //////////////////

    // Activation of a resource
    coap_activate_resource(&res_tent, "tent");
    
    PROCESS_END();
}