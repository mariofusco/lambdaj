// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import ch.lambdaj.group.*;
import static ch.lambdaj.collection.LambdaCollections.with;

import java.util.*;

/**
 * A wrapper of a group that returns LambdaLists instead of Lists in order to allow a fluent interface programming
 * @author Mario Fusco
 */
public class LambdaGroup<T> implements Group<T> {

    private final Group<T> group;

    LambdaGroup(Group<T> group) {
        this.group = group;
    }

    /**
     * Returns the key of this group
     */
    public Object key() {
        return group.key();
    }

    /**
     * Returns the set of the keys of the subgroups of this group
     */
    public Set<String> keySet() {
        return group.keySet();
    }

    /**
     * Returns the subgroup indexed with the given key
     *
     * @param key The key that identifies the searched subgroup
     * @return the subgroup with the given key or null if such a group doesn't exist
     */
    public LambdaGroup<T> findGroup(String key) {
        return new LambdaGroup<T>(group.findGroup(key));
    }

    /**
     * Returns the subgroup indexed with the given key
     *
     * @param key The key that identifies the searched subgroup
     * @return the subgroup with the given key or null if such a group doesn't exist
     */
    public LambdaGroup<T> findGroup(Object key) {
        return new LambdaGroup<T>(group.findGroup(key));
    }

    /**
     * Returns all the subgroups of this group or an empty one if this group is a leaf
     *
     * @return the list of all the subgroups of this group
     */
    public LambdaList<Group<T>> subgroups() {
        return with(group.subgroups());
    }

    /**
     * Returns all the items in the subgroup indexed with the given key
     *
     * @param key The key that identifies the searched subgroup
     * @return all the object in the subgroup with the given key or an empty List if such group doesn't exist or is empty
     */
    public LambdaList<T> find(String key) {
        return with(group.find(key));
    }

    /**
     * Returns all the items in the subgroup indexed with the given key
     *
     * @param key The key that identifies the searched subgroup
     * @return all the object in the subgroup with the given key or an empty List if such group doesn't exist or is empty
     */
    public LambdaList<T> find(Object key) {
        return with(group.find(key));
    }

    /**
     * Returns all the items in this group
     */
    public LambdaList<T> findAll() {
        return with(group.findAll());
    }

    /**
     * Returns the first item in this group
     */
    public T first() {
        return group.first();
    }

    /**
     * Returns how many items are present in this group.
     */
    public int getSize() {
        return group.getSize();
    }

    /**
     * Returns true if this group is a leaf or false if it has further subgroups
     */
    public boolean isLeaf() {
        return group.isLeaf();
    }

    /**
     * Returns the set of headers used to tag this group
     */
    public Set<String> getHeads() {
        return group.getHeads();
    }

    /**
     * Returns the value of the tag with the given key
     *
     * @param key The key of the request tag value
     */
    public String getHeadValue(String key) {
        return group.getHeadValue(key);
    }
}
