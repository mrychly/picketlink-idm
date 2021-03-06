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
package org.jboss.picketlink.idm.internal;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jboss.picketlink.idm.internal.jpa.DatabaseGroup;
import org.jboss.picketlink.idm.internal.jpa.DatabaseMembership;
import org.jboss.picketlink.idm.internal.jpa.DatabaseRole;
import org.jboss.picketlink.idm.internal.jpa.DatabaseUser;
import org.jboss.picketlink.idm.internal.jpa.JPACallback;
import org.jboss.picketlink.idm.internal.jpa.JPATemplate;
import org.jboss.picketlink.idm.internal.jpa.NamedQueries;
import org.jboss.picketlink.idm.model.Group;
import org.jboss.picketlink.idm.model.IdentityType;
import org.jboss.picketlink.idm.model.Membership;
import org.jboss.picketlink.idm.model.Role;
import org.jboss.picketlink.idm.model.User;
import org.jboss.picketlink.idm.query.GroupQuery;
import org.jboss.picketlink.idm.query.MembershipQuery;
import org.jboss.picketlink.idm.query.Range;
import org.jboss.picketlink.idm.query.RoleQuery;
import org.jboss.picketlink.idm.query.UserQuery;
import org.jboss.picketlink.idm.spi.IdentityStore;

/**
 * An implementation of IdentityStore backed by a JPA datasource
 *
 * @author Shane Bryzak
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 */
public class JPAIdentityStore implements IdentityStore {

    private JPATemplate jpaTemplate;

    @Override
    public User createUser(String name) {
        final DatabaseUser newUser = new DatabaseUser(name);

        persist(newUser);

        return newUser;
    }

    @Override
    public void removeUser(final User user) {
        remove(user);
    }

    @Override
    public User getUser(final String name) {
        final String namedQueryName = NamedQueries.USER_LOAD_BY_KEY;

        return (User) findIdentityTypeByKey(name, namedQueryName);
    }

    @Override
    public Group createGroup(String name, Group parent) {
        DatabaseGroup newGroup = new DatabaseGroup(name);

        newGroup.setParentGroup((DatabaseGroup) parent);

        persist(newGroup);

        return newGroup;
    }

    @Override
    public void removeGroup(Group group) {
        remove(group);
    }

    @Override
    public Group getGroup(String group) {
        return (Group) findIdentityTypeByKey(group, NamedQueries.GROUP_LOAD_BY_KEY);
    }

    @Override
    public Role createRole(String name) {
        DatabaseRole newRole = new DatabaseRole(name);

        persist(newRole);

        return newRole;
    }

    @Override
    public void removeRole(Role role) {
        remove(role);
    }

    @Override
    public Role getRole(String role) {
        return (Role) findIdentityTypeByKey(role, NamedQueries.ROLE_LOAD_BY_KEY);
    }

    @Override
    public Membership createMembership(Role role, User user, Group group) {
        DatabaseMembership newMembership = new DatabaseMembership(role, user, group);

        persist(newMembership);

        return newMembership;
    }

    @Override
    public void removeMembership(Role role, User user, Group group) {
        Membership membership = getMembership(role, user, group);

        if (membership != null) {
            remove(membership);
        }
    }

    @Override
    public Membership getMembership(final Role role, final User user, final Group group) {
        return (Membership) executeOperation(new JPACallback() {

            @Override
            public Object execute(EntityManager entityManager) {
                Query query = entityManager.createNamedQuery(NamedQueries.MEMBERSHIP_LOAD_BY_KEY);

                query.setParameter("role", role);
                query.setParameter("user", user);
                query.setParameter("group", group);

                Membership loadedMembership = null;

                try {
                    loadedMembership = (Membership) query.getSingleResult();
                } catch (NoResultException nre) {
                    // TODO: what to do when this happens
                }

                return loadedMembership;
            }
        });
    }

    @Override
    public List<User> executeQuery(UserQuery query, Range range) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Group> executeQuery(GroupQuery query, Range range) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Role> executeQuery(RoleQuery query, Range range) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Membership> executeQuery(MembershipQuery query, Range range) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAttribute(User user, String name, String[] values) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeAttribute(User user, String name) {
        // TODO Auto-generated method stub

    }

    @Override
    public String[] getAttributeValues(User user, String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, String[]> getAttributes(User user) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAttribute(Group group, String name, String[] values) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeAttribute(Group group, String name) {
        // TODO Auto-generated method stub

    }

    @Override
    public String[] getAttributeValues(Group group, String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, String[]> getAttributes(Group group) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAttribute(Role role, String name, String[] values) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeAttribute(Role role, String name) {
        // TODO Auto-generated method stub

    }

    @Override
    public String[] getAttributeValues(Role role, String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, String[]> getAttributes(Role role) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setJpaTemplate(JPATemplate jpaTemplate) {
        this.jpaTemplate = jpaTemplate;
    }

    /**
     * <p>
     * Executes the {@link JPACallback} instance.
     * </p>
     *
     * @param callback
     * @return
     */
    private Object executeOperation(JPACallback callback) {
        return this.jpaTemplate.execute(callback);
    }

    /**
     * <p>
     * Persists a specific instance.
     * </p>
     *
     * @param entity
     */
    private void persist(final Object entity) {
        JPACallback callback = new JPACallback() {

            @Override
            public Object execute(EntityManager entityManager) {
                entityManager.persist(entity);
                return null;
            }
        };

        executeOperation(callback);
    }

    /**
     * <p>
     * Removes a specific instance.
     * </p>
     *
     * @param entity
     */
    private void remove(final Object entity) {
        executeOperation(new JPACallback() {

            @Override
            public Object execute(EntityManager entityManager) {
                entityManager.remove(entity);
                return null;
            }
        });
    }

    /**
     * <p>
     * Find a instance with the given name and using the specified named query.
     * </p>
     *
     * @param name
     * @param namedQueryName
     * @return
     */
    private IdentityType findIdentityTypeByKey(final String name, final String namedQueryName) {
        return (IdentityType) executeOperation(new JPACallback() {

            @Override
            public Object execute(EntityManager entityManager) {
                Query query = entityManager.createNamedQuery(namedQueryName);

                query.setParameter("key", name);

                Object loadedUser = null;

                try {
                    loadedUser = query.getSingleResult();
                } catch (NoResultException nre) {
                    // TODO: what to do when this happens
                }

                return loadedUser;
            }
        });
    }
}
