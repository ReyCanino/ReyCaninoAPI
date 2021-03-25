package edu.escuelaing.reycanino.rabbit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class RMQInitializer implements InitializingBean {

    @Autowired
    private ReceiverRMQ receiver;

    @Override
    public void afterPropertiesSet() throws Exception {
        receiver.receiveMesssage();
    }
}