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
package org.jboss.picketlink.idm.spi;

import java.util.List;
import java.util.Map;

import org.jboss.picketlink.idm.model.Group;
import org.jboss.picketlink.idm.model.Membership;
import org.jboss.picketlink.idm.model.Role;
import org.jboss.picketlink.idm.model.User;
import org.jboss.picketlink.idm.query.GroupQuery;
import org.jboss.picketlink.idm.query.MembershipQuery;
import org.jboss.picketlink.idm.query.Range;
import org.jboss.picketlink.idm.query.RoleQuery;
import org.jboss.picketlink.idm.query.UserQuery;

/**
 * IdentityStore representation providing minimal SPI
 *
 */
public interface IdentityStore {
    // TODO: Javadocs
    // TODO: Exceptions

    // TODO: control hooks, events
    // TODO: authentication, password strenght, salted password hashes

    // User

    User createUser(String name);

    void removeUser(User user);

    User getUser(String name);

    // Group

    Group createGroup(String name, Group parent);

    void removeGroup(Group group);

    Group getGroup(String name);

    // Role

    Role createRole(String name);

    void removeRole(Role role);

    Role getRole(String role);

    // Memberships

    Membership createMembership(Role role, User user, Group group);

    void removeMembership(Role role, User user, Group group);

    Membership getMembership(Role role, User user, Group group);

    // Queries

    List<User> executeQuery(UserQuery query, Range range);

    List<Group> executeQuery(GroupQuery query, Range range);

    List<Role> executeQuery(RoleQuery query, Range range);

    List<Membership> executeQuery(MembershipQuery query, Range range);

    // Attributes

    // User

    /**
     * Set attribute with given name and values. Operation will overwrite any previous values. Null value or empty array will
     * remove attribute.
     *
     * @param user
     * @param name of attribute
     * @param values to be set
     */
    void setAttribute(User user, String name, String[] values);

    /**
     * @param user Remove attribute with given name
     *
     * @param name of attribute
     */
    void removeAttribute(User user, String name);

    /**
     * @param user
     * @param name of attribute
     * @return attribute values or null if attribute with given name doesn't exist
     */
    String[] getAttributeValues(User user, String name);

    /**
     * @param user
     * @return map of attribute names and their values
     */
    Map<String, String[]> getAttributes(User user);

    // Group

    /**
     * Set attribute with given name and values. Operation will overwrite any previous values. Null value or empty array will
     * remove attribute.
     *
     * @param group
     * @param name of attribute
     * @param values to be set
     */
    void setAttribute(Group group, String name, String[] values);

    /**
     * Remove attribute with given name
     *
     * @param group
     * @param name of attribute
     */
    void removeAttribute(Group group, String name);

    /**
     * @param group
     * @param name of attribute
     * @return attribute values or null if attribute with given name doesn't exist
     */
    String[] getAttributeValues(Group group, String name);

    /**
     * @param group
     * @return map of attribute names and their values
     */
    Map<String, String[]> getAttributes(Group group);

    // Role

    /**
     * Set attribute with given name and values. Operation will overwrite any previous values. Null value or empty array will
     * remove attribute.
     *
     * @param role
     * @param name of attribute
     * @param values to be set
     */
    void setAttribute(Role role, String name, String[] values);

    /**
     * Remove attribute with given name
     *
     * @param role
     * @param name of attribute
     */
    void removeAttribute(Role role, String name);

    /**
     * @param role
     * @param name of attribute
     * @return attribute values or null if attribute with given name doesn't exist
     */
    String[] getAttributeValues(Role role, String name);

    /**
     * @param role
     * @return map of attribute names and their values
     */
    Map<String, String[]> getAttributes(Role role);

}
