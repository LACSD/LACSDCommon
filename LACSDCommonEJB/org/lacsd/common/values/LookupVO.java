package org.lacsd.common.values;

import java.util.ArrayList;

/******************************************************************************
//* Copyright (c) 2013 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LookupVO.java
//* Revision: 		1.0
//* Author:			hho@lacsd.org
//* Created On: 	Aug 2, 2013
//* Modified By:    
//* Modified On:    
//* 
//* Description:	generic implementation key/value object.  Using this object for
//                  to store key/value in drop down box 
/******************************************************************************/


public class LookupVO<K, V> implements PairVO<K, V>,java.io.Serializable {

  /**
	 * 
	 */
  private static final long serialVersionUID = -1287946925294688139L;
  private K key;
  private V value;
  private ArrayList<LookupVO<K, V>> lookupVOs;

  public LookupVO(K key, V value) {
  	this.key = key;
  	this.value = value;
  }

  public K getKey()	{ return key; }
  public V getValue() { return value; }

public ArrayList<LookupVO<K, V>> getLookupVOs() {
	return lookupVOs;
}

public void setLookupVOs(ArrayList<LookupVO<K, V>> lookupVOs) {
	this.lookupVOs = lookupVOs;
}
    
}