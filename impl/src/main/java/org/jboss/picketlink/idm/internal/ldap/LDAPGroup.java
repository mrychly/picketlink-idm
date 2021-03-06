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

import org.jboss.picketlink.idm.model.Group;

/**
 * LDAP Representation of the {@link Group}
 *
 * @author anil saldhana
 * @since Sep 4, 2012
 */
public class LDAPGroup extends DirContextAdaptor implements Group {

    public final String COMMA = ",";
    private LDAPGroup parent;
    private String groupName;

    private String groupDNSuffix;

    public LDAPGroup() {
        Attribute oc = new BasicAttribute(OBJECT_CLASS);
        oc.add("top");
        oc.add("groupOfNames");
        attributes.put(oc);
    }

    public String getDN() {
        return CN + EQUAL + groupName + COMMA + groupDNSuffix;
    }

    public void addRole(LDAPRole role) {
        Attribute memberAttribute = attributes.get(MEMBER);
        if (memberAttribute != null) {
            if (memberAttribute.contains(SPACE_STRING)) {
                memberAttribute.remove(SPACE_STRING);
            }
        } else {
            memberAttribute = new BasicAttribute(OBJECT_CLASS);
            memberAttribute.add("top");
            memberAttribute.add("groupOfNames");
        }
        memberAttribute.add(role.getDN());
    }

    public void removeRole(LDAPRole role) {
        Attribute memberAttribute = attributes.get(MEMBER);
        if (memberAttribute != null) {
            memberAttribute.remove(role.getDN());
        }
    }

    @Override
    public String getId() {
        return null;
    }

    public void setName(String name) {
        this.groupName = name;
        Attribute theAttribute = attributes.get(CN);
        if (theAttribute == null) {
            attributes.put(CN, groupName);
        } else {
            theAttribute.set(0, groupName);
        }
        attributes.put(MEMBER, SPACE_STRING); // Dummy member for now
    }

    @Override
    public String getName() {
        return groupName;
    }

    public void setParentGroup(Group parent) {
        if (parent instanceof LDAPGroup == false) {
            throw new RuntimeException("Wrong type:" + parent.getClass());
        }
        LDAPGroup parentGroup = (LDAPGroup) parent;
        this.parent = parentGroup;
    }

    @Override
    public Group getParentGroup() {
        return parent;
    }

    public void addChildGroup(LDAPGroup childGroup) {
        // Deal with attributes
        Attribute memberAttribute = attributes.get(MEMBER);
        if (memberAttribute != null) {
            if (memberAttribute.contains(SPACE_STRING)) {
                memberAttribute.remove(SPACE_STRING);
            }

            memberAttribute.add(CN + "=" + childGroup.getName() + COMMA + groupDNSuffix);
        }
    }

    public String getGroupDNSuffix() {
        return groupDNSuffix;
    }

    public void setGroupDNSuffix(String groupDNSuffix) {
        this.groupDNSuffix = groupDNSuffix;
    }

    public static LDAPGroup create(Attributes attributes, String groupDNSuffix) {
        LDAPGroup ldapGroup = new LDAPGroup();
        ldapGroup.setGroupDNSuffix(groupDNSuffix);

        try {
            // Get the common name
            Attribute cn = attributes.get(CN);
            ldapGroup.setName((String) cn.get());
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        return ldapGroup;
    }
}