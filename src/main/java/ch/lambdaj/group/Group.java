// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;

import java.util.*;

/**
 * A set of object (optionally hierarchically) grouped on the values of one or more of their properties 
 * @author Mario Fusco
 */
public interface Group<T> {
	
    /**
     * Returns the key of this group
     */
    Object key();

	/**
	 * Returns the set of the keys of the subgroups of this group
	 */
	Set<String> keySet();

	/**
	 * Returns the subgroup indexed with the given key
	 * @param key The key that identifies the searched subgroup
	 * @return the subgroup with the given key or null if such a group doesn't exist
	 */
	Group<T> findGroup(String key);
	
	/**
	 * Returns the subgroup indexed with the given key
	 * @param key The key that identifies the searched subgroup
	 * @return the subgroup with the given key or null if such a group doesn't exist
	 */
	Group<T> findGroup(Object key);
	
	/**
	 * Returns all the subgroups of this group or an empty one if this group is a leaf 
	 * @return the list of all the subgroups of this group
	 */
	List<Group<T>> subgroups();

	/**
	 * Returns all the items in the subgroup indexed with the given key
	 * @param key The key that identifies the searched subgroup
	 * @return all the object in the subgroup with the given key or an empty List if such group doesn't exist or is empty
	 */
	List<T> find(String key);
	
	/**
	 * Returns all the items in the subgroup indexed with the given key
	 * @param key The key that identifies the searched subgroup
	 * @return all the object in the subgroup with the given key or an empty List if such group doesn't exist or is empty
	 */
	List<T> find(Object key);

	/**
	 * Returns all the items in this group
	 */
	List<T> findAll();

    /**
     * Returns the first item in this group
     */
    T first();

	/**
	 * Returns how many items are present in this group, including the ones in the subgruoups at any level 
	 */
	int getSize();
	
	/**
	 * Returns true if this group is a leaf or false if it has further subgroups
	 */
	boolean isLeaf();

	/**
	 * Returns the set of headers used to tag this group
	 */
	Set<String> getHeads();

	/**
	 * Returns the value of the tag with the given key
	 * @param key The key of the request tag value
	 */
	String getHeadValue(String key);
}
