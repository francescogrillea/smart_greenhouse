CONTIKI_PROJECT = coap-server
all: $(CONTIKI_PROJECT)

CONTIKI = ../../..
CFLAGS += -DPROJECT_CONF_PATH=\"project-conf.h\"
#COAP
MODULES_REL += ./resources
include $(CONTIKI)/Makefile.dir-variables
MODULES += $(CONTIKI_NG_APP_LAYER_DIR)/coap
include $(CONTIKI)/Makefile.include






