package org.lacsd.common.values;
/******************************************************************************
//* Copyright (c) 2013 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		PairVO.java
//* Revision: 		1.0
//* Author:			hho@lacsd.org
//* Created On: 	Aug 2, 2013
//* Modified By:    
//* Modified On:    
//* 
//* Description:	Generic type for the key/value object
/******************************************************************************/

public interface PairVO<K, V> {
  public K getKey();
  public V getValue();
}
