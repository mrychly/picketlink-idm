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
package org.jboss.picketlink.idm.internal.ldap;

import static org.jboss.picketlink.idm.internal.ldap.LDAPConstants.CN;
import static org.jboss.picketlink.idm.internal.ldap.LDAPConstants.MEMBER;
import static org.jboss.picketlink.idm.internal.ldap.LDAPConstants.OBJECT_CLASS;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;

import org.jboss.picketlink.idm.model.Role;

/**
 * Implementation of {@link Role} for storage in ldap
 *
 * @author anil saldhana
 * @since Aug 31, 2012
 */
public class LDAPRole extends DirContextAdaptor implements Role {

    private String roleName, roleDNSuffix;

    public LDAPRole() {
        Attribute oc = new BasicAttribute(OBJECT_CLASS);
        oc.add("top");
        oc.add("groupOfNames");
        attributes.put(oc);
    }

    public void setRoleDNSuffix(String rdns) {
        this.roleDNSuffix = rdns;
    }

    public String getDN() {
        return CN + EQUAL + roleName + COMMA + roleDNSuffix;
    }

    public void setName(String roleName) {
        this.roleName = roleName;
        Attribute theAttribute = attributes.get(CN);
        if (theAttribute == null) {
            attributes.put(CN, roleName);
        } else {
            theAttribute.set(0, roleName);
        }
        attributes.put(MEMBER, SPACE_STRING); // Dummy member for now
    }

    @Override
    public String getName() {
        return roleName;
    }

    public void addUser(LDAPUser user) {
        Attribute memberAttribute = attributes.get(MEMBER);
        if (memberAttribute != null) {
            if (memberAttribute.contains(SPACE_STRING)) {
                memberAttribute.remove(SPACE_STRING);
            }
        } else {
            memberAttribute = new BasicAttribute(OBJECT_CLASS);
            memberAttribute.add("inetOrgPerson");
            memberAttribute.add("organizationalPerson");
            memberAttribute.add("person");
            memberAttribute.add("top");
        }
        memberAttribute.add(user.getDN());
    }

    public void removeUser(LDAPUser user) {
        Attribute memberAttribute = attributes.get(MEMBER);
        if (memberAttribute != null) {
            memberAttribute.remove(user.getDN());
        }
    }

    public static LDAPRole create(Attributes attributes, String roleDNSuffix) {
        LDAPRole role = new LDAPRole();
        role.setRoleDNSuffix(roleDNSuffix);

        try {
            // Get the common name
            Attribute cn = attributes.get(CN);
            role.setName((String) cn.get());
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        return role;
    }
}