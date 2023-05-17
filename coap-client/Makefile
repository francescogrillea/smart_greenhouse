CONTIKI_PROJECT = application_example
all: $(CONTIKI_PROJECT)

CONTIKI = ../../..

# path to project-conf.h
CFLAGS += -DPROJECT_CONF_H=\"project-conf.h\"

# path to the resource directory (default directory below)
MODULES_REL += ./resources

# Include RPL BR module
include $(CONTIKI)/Makefile.dir-variables
MODULES += $(CONTIKI_NG_SERVICES_DIR)/rpl-border-router
# Include webserver module
MODULES_REL += webserver
# Include optional target-specific module
include $(CONTIKI)/Makefile.identify-target
MODULES_REL += $(TARGET)
MODULES += $(CONTIKI_NG_APP_LAYER_DIR)/coap

include $(CONTIKI)/Makefile.include
include $(CONTIKI)/Makefile.dir-variables
