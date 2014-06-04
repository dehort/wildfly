/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2014, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.test.integration.ejb.mdb.vaultedproperties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
  * @author <a href="http://jmesnil.net/">Jeff Mesnil</a> (c) 2014 Red Hat inc.
 */
@MessageDriven(activationConfig = {
        // value was put in the vault by org.jboss.as.test.integration.ejb.mdb.vaultedproperties.MDBWithVaultedPropertiesTestCase.StoreVaultedPropertyTask
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "${VAULT::messaging::destination::1}")
})
public class MDB implements MessageListener {


    @Resource(lookup = "java:/JmsXA")
    private ConnectionFactory factory;

    private Connection connection;
    private Session session;

    @Override
    public void onMessage(Message message) {
        try {
            final Destination replyTo = message.getJMSReplyTo();
            // ignore messages that need no reply
            if (replyTo == null) {
                return;
            }
            final MessageProducer replyProducer = session.createProducer(replyTo);
            final Message replyMsg = session.createTextMessage(((TextMessage) message).getText());
            replyMsg.setJMSCorrelationID(message.getJMSMessageID());
            replyProducer.send(replyMsg);
            replyProducer.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    protected void preDestroy() throws JMSException {
        session.close();
        connection.close();
    }

    @PostConstruct
    protected void postConstruct() throws JMSException {
        connection = factory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }
}
