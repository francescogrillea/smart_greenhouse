#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "coap-engine.h"
#include "os/dev/leds.h"
#include "contiki.h"

static void res_put_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);


RESOURCE(res_tent,
         "title=\"Hello world: ?len=0..\";rt=\"Text\"",
         NULL,
         NULL,
         res_put_handler,
         NULL);


static void res_put_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset){

  if(request->payload_len > 0) {
    /* Get the payload data */
    const char* payload = (const char*)request->payload;
    printf("Received: %s\n", payload);

    /* Parse the payload to determine the LED state */
    if(strcmp(payload, "up") == 0) {
      /* Turn on the GREEN_LEDS */
      leds_off(LEDS_RED);
      leds_off(LEDS_YELLOW);
      leds_on(LEDS_GREEN);
    } else if(strcmp(payload, "down") == 0) {
      leds_off(LEDS_RED);
      leds_off(LEDS_GREEN);
      leds_on(LEDS_YELLOW);
    }

    /* Set the response code to indicate success */
    coap_set_status_code(response, CHANGED_2_04);
  } else {
    /* Set the response code to indicate bad request */
    coap_set_status_code(response, BAD_REQUEST_4_00);
  }

}


