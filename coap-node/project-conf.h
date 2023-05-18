#ifndef __PROJECT_CONF_H
#define __PROJECT_CONF_H

///////////////////////// COAP //////////////////////

// Set the max response payload before enable fragmentation:
#undef REST_MAX_CHUNK_SIZE
#define REST_MAX_CHUNK_SIZE 64
// Set the maximum number of CoAP concurrent transactions:
#undef COAP_MAX_OPEN_TRANSACTIONS
#define COAP_MAX_OPEN_TRANSACTIONS 4
// save some memory for the sky platform
#undef NBR_TABLE_CONF_MAX_NEIGHBORS
#define NBR_TABLE_CONF_MAX_NEIGHBORS 10
#undef UIP_CONF_MAX_ROUTES
#define UIP_CONF_MAX_ROUTES 10
#undef UIP_CONF_BUFFER_SIZE
#define UIP_CONF_BUFFER_SIZE 240

////////////////////////////////////////////////


#undef QUEUEBUF_CONF_NUM
#define QUEUEBUF_CONF_NUM	32

#undef IEEE802154_CONF_PANID
#define IEEE801254_CONF_PANID 0x0011

#undef IEEE802154_CONF_DEFAULT_CHANNEL
#define IEEE802154_CONF_DEFAULT_CHANNEL 15

#endif
