// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;

import java.util.*;

/**
 * The standard LambdaJ implementation for the Group interface
 * @author Mario Fusco
 */
class GroupImpl<T> extends LinkedList<GroupItem<T>> implements Group<T> {

	private static final long serialVersionUID = 1L;

	private final Map<String, GroupItem<T>> groupsMap = new HashMap<String, GroupItem<T>>();

	private final GroupCondition<?> groupCondition;

    private Object key;

	GroupImpl(GroupCondition<?> groupCondition) {
		this.groupCondition = groupCondition;
	}

	void addItem(T item) {
		GroupItem<T> groupItem = findOrCreate(item, groupCondition.getGroupValue(item));
		groupItem.addChild(item);
	}

	private GroupItem<T> findOrCreate(T item, Object key) {
        String keyAsString = key == null ? "" : key.toString();
		GroupItem<T> groupItem = groupsMap.get(keyAsString);
		return groupItem != null ? groupItem : create(item, key, keyAsString);
	}

	private GroupItem<T> create(T item, Object key, String keyAsString) {
        GroupItem<T> groupItem = groupCondition.create(item, key, keyAsString);
        groupsMap.put(keyAsString, groupItem);
        add(groupItem);
        return groupItem;
	}

    /**
     * Returns the key of this group
     */
    public Object key() {
        return key;
    }

    void setKey(Object key) {
        this.key = key;
    }

    /**
     * Returns the set of the keys of the subgroups of this group
     */
	public Set<String> keySet() {
		return groupsMap.keySet();
	}

    /**
     * Returns the subgroup indexed with the given key
     * @param key The key that identifies the searched subgroup
     * @return the subgroup with the given key or null if such a group doesn't exist
     */
	public Group<T> findGroup(String key) {
		GroupItem<T> groupItem = groupsMap.get(key);
		return groupItem == null ? null : groupItem.asGroup();
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
	@SuppressWarnings("unchecked")
	public List<Group<T>> subgroups() {
        List<Group<T>> resultList = new LinkedList<Group<T>>();
        for (GroupItem<T> groupItem : this) { resultList.add(groupItem.asGroup()); }
        return resultList;
	}
	
    /**
     * Returns all the items in the subgroup indexed with the given key
     * @param key The key that identifies the searched subgroup
     * @return all the object in the subgroup with the given key or an empty List if such group doesn't exist or is empty
     */
	public List<T> find(String key) {
		GroupItem<T> groupItem = groupsMap.get(key);
		return groupItem == null ? new LinkedList<T>() : groupItem.asList();
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
		List<T> allItems = new LinkedList<T>();
		for (GroupItem<T> groupItem : this) { allItems.addAll(groupItem.asList()); }
		return allItems;
	}

    /**
     * Returns the first item in this group
     */
    public T first() {
        return get(0).asList().get(0);
    }
	
    /**
     * Returns how many items are present in this group.
     */
	public int getSize() {
		return findAll().size();
	}
	
    /**
     * Returns true if this group is a leaf or false if it has further subgroups
     */
	public boolean isLeaf() {
		return false;
	}
	
    /**
     * Returns the set of headers used to tag this group
     */
	public Set<String> getHeads() {
		return new HashSet<String>();
	}

    /**
     * Returns the value of the tag with the given key
     * @param key The key of the request tag value
     */
	public String getHeadValue(String key) {
		return "";
	}
}
