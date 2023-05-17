#include "contiki.h"
#include "coap-engine.h"
#include "coap-blocking-api.h"
#include <stdio.h>
#include <stdlib.h>

#define SERVER_EP "coap://[fd00::201:1:1:1]:5683" // if built in with a border router
//#define SERVER_EP "coap://[fd00::202:2:2:2]:5683" // if on sensor node

static const char *service_url = "/hello";
static struct etimer timer;

// Define a handler to handle the response from the server
static void client_chunk_handler(coap_message_t *response){
    const uint8_t *chunk;
    if(response == NULL) {
        puts("Request timed out");
        return;
    }
    int len = coap_get_payload(response, &chunk);
    printf("|%.*s", len, (char *)chunk);
}


PROCESS(coap_client_example, "CoapClientExample");
AUTOSTART_PROCESSES(&coap_client_example);


PROCESS_THREAD(coap_client_example, ev, data){

    static coap_endpoint_t server_ep;
    static coap_message_t request[1];

    PROCESS_BEGIN();
    printf("Inizio\n");

    etimer_set(&timer, CLOCK_SECOND * 5);

    // Populate the coap_endpoint_t data structure
    coap_endpoint_parse(SERVER_EP, strlen(SERVER_EP), &server_ep);


    // TODO - add a timer in while true
    while(1){

        PROCESS_WAIT_EVENT_UNTIL(etimer_expired(&timer));

        // Prepare the message
        coap_init_message(request, COAP_TYPE_CON, COAP_GET, 0);
        coap_set_header_uri_path(request, service_url);
        
        // Set the payload (if needed)
        const char msg[] = "Toggle!";
        coap_set_payload(request, (uint8_t *)msg, sizeof(msg) - 1);

        // Issue the request in a blocking manner 
        // The client will wait for the server to reply (or the transmission to timeout)
        COAP_BLOCKING_REQUEST(&server_ep, request, client_chunk_handler);   // block execution of the program until client recieve a response from the server

        printf("\nDone\n\n");

        etimer_reset(&timer);
    }

    PROCESS_END();

}
