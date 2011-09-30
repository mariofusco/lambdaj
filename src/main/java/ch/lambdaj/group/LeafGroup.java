// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;

import java.util.*;

/**
 * A leaf group is a group that doesn't contain other groups.
 * @author Mario Fusco
 */
class LeafGroup<T> implements Group<T> {

	private final List<T> list;
	private final Map<String, Object> headMap;
    private final Object key;
	
	@SuppressWarnings("unchecked")
	LeafGroup(GroupItem<T> groupItem, String childrenNodeName) {
        key = groupItem.getGroupKey();
		headMap = new TreeMap<String, Object>(groupItem);
		list = (List<T>)headMap.remove(childrenNodeName);
	}

    /**
     * Returns the key of this group
     */
    public Object key() {
        return key;
    }
	
    /**
     * Returns all the items in the subgroup indexed with the given key
     * @param key The key that identifies the searched subgroup
     * @return all the object in the subgroup with the given key or an empty List if such group doesn't exist or is empty
     */
	public List<T> find(String key) {
		return list;
	}

    /**
     * Returns all the items in the subgroup indexed with the given key
     * @param key The key that identifies the searched subgroup
     * @return all the object in the subgroup with the given key or an empty List if such group doesn't exist or is empty
     */
	public List<T> find(Object key) {
		return find(key.toString());
	}
	
    /**
     * Returns all the items in this group
     */
	public List<T> findAll() {
		return list;
	}

    /**
     * Returns the first item in this group
     */
    public T first() {
        return list.get(0);
    }

    /**
     * Returns the subgroup indexed with the given key
     * @param key The key that identifies the searched subgroup
     * @return the subgroup with the given key or null if such a group doesn't exist
     */
	public Group<T> findGroup(String key) {
		return this;
	}
	
    /**
     * Returns the subgroup indexed with the given key
     * @param key The key that identifies the searched subgroup
     * @return the subgroup with the given key or null if such a group doesn't exist
     */
	public Group<T> findGroup(Object key) {
		return findGroup(key.toString());
	}
	
    /**
     * Returns all the subgroups of this group or an empty one if this group is a leaf
     * @return the list of all the subgroups of this group
     */
	public List<Group<T>> subgroups() {
		return new LinkedList<Group<T>>();
	}

    /**
     * Returns how many items are present in this group.
     */
	public int getSize() {
		return list.size();
	}

    /**
     * Returns true if this group is a leaf or false if it has further subgroups
     */
	public boolean isLeaf() {
		return true;
	}

    /**
     * Returns the set of the keys of the subgroups of this group
     */
	public Set<String> keySet() {
		return new HashSet<String>();
	}

    /**
     * Returns the value of the tag with the given key
     * @param key The key of the request tag value
     */
	public String getHeadValue(String key) {
		Object value = headMap.get(key);
		return value == null ? "" : value.toString();
	}

    /**
     * Returns the set of headers used to tag this group
     */
	public Set<String> getHeads() {
		return headMap.keySet();
	}
}
