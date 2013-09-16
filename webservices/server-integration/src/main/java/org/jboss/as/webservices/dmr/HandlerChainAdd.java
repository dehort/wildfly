/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.jboss.as.webservices.dmr;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.webservices.WSMessages.MESSAGES;
import static org.jboss.as.webservices.dmr.Constants.ENDPOINT_CONFIG;
import static org.jboss.as.webservices.dmr.Constants.HANDLER_CHAIN;
import static org.jboss.as.webservices.dmr.Constants.PROTOCOL_BINDINGS;

import java.util.List;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.webservices.service.HandlerChainService;
import org.jboss.as.webservices.util.WSServices;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.wsf.spi.metadata.config.AbstractCommonConfig;

/**
 * @author <a href="mailto:ropalka@redhat.com">Richard Opalka</a>
 * @author <a href="mailto:alessio.soldano@jboss.com">Alessio Soldano</a>
 */
final class HandlerChainAdd extends AbstractAddStepHandler {

    static final HandlerChainAdd INSTANCE = new HandlerChainAdd();

    private HandlerChainAdd() {
        // forbidden instantiation
    }

    @Override
    protected void rollbackRuntime(final OperationContext context, final ModelNode operation, final ModelNode model, final List<ServiceController<?>> controllers) {
        super.rollbackRuntime(context, operation, model, controllers);
        if (!context.isBooting()) {
            context.revertReloadRequired();
        }
    }

    @Override
    protected void performRuntime(final OperationContext context, final ModelNode operation, final ModelNode model, final ServiceVerificationHandler verificationHandler, final List<ServiceController<?>> newControllers) throws OperationFailedException {
        //modify the runtime if we're booting, otherwise set reload required and leave the runtime unchanged
        if (context.isBooting()) {
            final String protocolBindings = getAttributeValue(operation, PROTOCOL_BINDINGS);
            final PathAddress address = PathAddress.pathAddress(operation.require(OP_ADDR));
            final PathElement confElem = address.getElement(address.size() - 2);
            final String configType = confElem.getKey();
            final String configName = confElem.getValue();
            final String handlerChainType = address.getElement(address.size() - 1).getKey();
            final String handlerChainId = address.getElement(address.size() - 1).getValue();

            final HandlerChainService<AbstractCommonConfig> service = new HandlerChainService<AbstractCommonConfig>(handlerChainType, handlerChainId, protocolBindings);
            final ServiceTarget target = context.getServiceTarget();
            final ServiceName configServiceName = ((ENDPOINT_CONFIG.equals(configType) ? WSServices.ENDPOINT_CONFIG_SERVICE : WSServices.CLIENT_CONFIG_SERVICE)).append(configName);
            if (context.getServiceRegistry(false).getService(configServiceName) == null) {
                throw MESSAGES.missingConfig(configName);
            }

            final ServiceName handlerChainServiceName = configServiceName.append(HANDLER_CHAIN).append(handlerChainId);
            final ServiceBuilder<?> handlerChainServiceBuilder = target.addService(handlerChainServiceName, service);
            handlerChainServiceBuilder.addDependency(configServiceName, AbstractCommonConfig.class, service.getAbstractCommonConfig());
            handlerChainServiceBuilder.setInitialMode(ServiceController.Mode.ACTIVE).install();
        } else {
            context.reloadRequired();
        }
    }

    private static String getAttributeValue(final ModelNode node, final String propertyName) {
        return node.hasDefined(propertyName) ? node.get(propertyName).asString() : null;
    }

    @Override
    protected void populateModel(final ModelNode operation, final ModelNode model) throws OperationFailedException {
        Attributes.PROTOCOL_BINDINGS.validateAndSet(operation, model);
    }

}
