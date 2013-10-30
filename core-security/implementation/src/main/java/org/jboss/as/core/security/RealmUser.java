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

package org.jboss.as.core.security;



/**
 * The Principal used to represent the name of an authenticated user.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class RealmUser extends AbstractRealmPrincipal implements AccountPrincipal, org.jboss.as.core.security.api.UserPrincipal {

    private static final long serialVersionUID = 5391073820551736954L;
    // https://bugzilla.redhat.com/show_bug.cgi?id=1017856 in EAP 6.x make these protected for force use
    // of legacy RealmUser
    protected RealmUser(String realm, String name) {
        super(realm, name);
    }

    // https://bugzilla.redhat.com/show_bug.cgi?id=1017856 in EAP 6.x make these protected for force use
    // of legacy RealmUser
    protected RealmUser(String name) {
        super(name);
    }

}