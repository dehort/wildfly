/*
* JBoss, Home of Professional Open Source.
* Copyright 2012, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.remoting;

import static org.jboss.as.remoting.SaslResource.REUSE_SESSION_ATTRIBUTE;
import static org.jboss.as.remoting.SaslResource.SASL_CONFIG_PATH;
import static org.jboss.as.remoting.SaslResource.SERVER_AUTH_ATTRIBUTE;

import org.jboss.as.controller.transform.description.RejectAttributeChecker;
import org.jboss.as.controller.transform.description.ResourceTransformationDescriptionBuilder;

/**
 *
 * @author <a href="http://jmesnil.net">Jeff Mesnil</a> (c) 2012 Red Hat, inc
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class SaslResourceTransformers {

    static void registerTransformers(ResourceTransformationDescriptionBuilder builder) {
        ResourceTransformationDescriptionBuilder sasl = builder.addChildResource(SASL_CONFIG_PATH).getAttributeBuilder()
        .addRejectCheck(RejectAttributeChecker.SIMPLE_EXPRESSIONS, SERVER_AUTH_ATTRIBUTE, REUSE_SESSION_ATTRIBUTE).end();


        SaslPolicyResourceTransformers.registerTransformers(sasl);
        PropertyResourceTransformers.registerTransformers(sasl);
    }
}