/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014, Red Hat, Inc., and individual contributors
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

package com.redhat.gss.extension;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.parsing.ParseUtils;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.dmr.ModelNode;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.util.List;

public final class RedhatAccessPluginSubsystemParser implements
        XMLStreamConstants, XMLElementReader<List<ModelNode>>,
        XMLElementWriter<SubsystemMarshallingContext> {

    static final RedhatAccessPluginSubsystemParser INSTANCE = new RedhatAccessPluginSubsystemParser();

    public static RedhatAccessPluginSubsystemParser getInstance() {
        return INSTANCE;
    }

    public void readElement(final XMLExtendedStreamReader reader,
            final List<ModelNode> list) throws XMLStreamException {
        ParseUtils.requireNoAttributes(reader);
        ParseUtils.requireNoContent(reader);

        final ModelNode subsystem = Util.createAddOperation(PathAddress
                .pathAddress(RedhatAccessPluginExtension.SUBSYSTEM_PATH));
        list.add(subsystem);
    }

    /** {@inheritDoc} */
    public void writeContent(final XMLExtendedStreamWriter writer,
            final SubsystemMarshallingContext context)
            throws XMLStreamException {
        context.startSubsystemElement(
                com.redhat.gss.extension.Namespace.CURRENT.getUriString(),
                false);
        writer.writeEndElement();
    }
}
