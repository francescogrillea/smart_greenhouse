#include "contiki.h"
#include "coap-engine.h"
#include "coap-blocking-api.h"
#include <stdio.h>
// Server IP and resource path
#define SERVER_EP "coap://[fd00::1]:5683"
char *service_url = "/registration";

// Define a handler to handle the response from the server
void client_chunk_handler(coap_message_t *response)
{
const uint8_t *chunk;
if(response == NULL) {
puts("Request timed out");
return;
}
int len = coap_get_payload(response, &chunk);
printf("%.*s\n", len, (char *)chunk);
}

#define LOG_MODULE "App"

PROCESS(client, "client");
AUTOSTART_PROCESSES(&client);
PROCESS_THREAD(client, ev, data){
    static coap_endpoint_t server_ep;
    static coap_message_t request[1]; /* This way the packet can be treated as pointer as usual. */
    // qua dentro poi ci andrà messo l'ip del sender
    static const char json_message []= "{\"value\":10}";

    PROCESS_BEGIN();
    
    // Populate the coap_endpoint_t data structure
    
    coap_endpoint_parse(SERVER_EP, strlen(SERVER_EP),&server_ep);
    
    // Prepare the message
    coap_init_message(request, COAP_TYPE_CON,COAP_POST, 0);
    coap_set_header_uri_path(request, service_url);
    // PER IL JSON!!!!
    coap_set_header_content_format(request, APPLICATION_JSON);
    
    // Set the payload (if needed)
    coap_set_payload(request, (uint8_t *)json_message,sizeof(json_message) - 1);
    
    // Issue the request in a blocking manner
    // The client will wait for the server to reply(or the transmission to timeout)
    while(1){
        
    COAP_BLOCKING_REQUEST(&server_ep, request,client_chunk_handler);
    }
    // da qua in poi metteremo il codice del coap server.
    
    PROCESS_END();
}