// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;

import java.util.*;

/**
 * @author Mario Fusco
 */
public class GroupItem<T> extends TreeMap<String, Object> implements Iterable<T> {

	private static final long serialVersionUID = 1L;
	
	private static final String CHILDREN_NODE = "children";
	private String childrenNodeName = CHILDREN_NODE;

	private boolean leaf = true;

    private final Object groupKey;

	GroupItem() {
        groupKey = null;
    }

	GroupItem(Object groupKey, String childrenNodeName) {
        this.groupKey = groupKey;
		if (childrenNodeName != null) this.childrenNodeName = childrenNodeName;
	}

    Object getGroupKey() {
        return groupKey;
    }

	private String getChildrenNodeName() {
		return childrenNodeName;
	}
	
	@SuppressWarnings("unchecked")
	private List<T> getChildren() {
		List<T> children = (List<T>) get(getChildrenNodeName());
		if (children == null) {
			children = new LinkedList<T>();
			put(getChildrenNodeName(), children);
		}
		return children;
	}

    /**
     * Convert this GroupItem in the corresponding Group
     * @return The Group wrapping this GroupItem
     */
	@SuppressWarnings("unchecked")
	public Group<T> asGroup() {
		return leaf ? new LeafGroup<T>(this, getChildrenNodeName()) : (Group<T>)get(getChildrenNodeName());
	}

    /**
     * {@inheritDoc}
     */
	public Iterator<T> iterator() {
		return asList().iterator();
	}
	
	List<T> asList() {
		if (leaf) return getChildren();
		List<T> leafs = new LinkedList<T>();
		for (T item : asGroup().findAll()) { leafs.add(item); }
		return leafs;
	}

	void addChild(T child) {
		if (!leaf) throw new IllegalStateException("cannot add a child to a non-leaf group");
		getChildren().add(child);
	}

	void setChildren(GroupImpl<T> children) {
		leaf = false;
        children.setKey(getGroupKey());
		put(getChildrenNodeName(), children);
	}
}
