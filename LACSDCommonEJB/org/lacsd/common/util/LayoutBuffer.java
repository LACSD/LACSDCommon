package org.lacsd.common.util;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		LayoutBuffer.java
//* Revision: 		1.0
//* Author:			M Feinberg
//* Created On: 	07-08-2003
//* Modified by:	
//* Modified On:	
//*					
//* Description:	The LayoutBuffer is a utility to layout fields in a string 
//* 				format. Fields are added to an instance in the order in which
//* 				they should appear in the layout.  
//* 
//* 				Each field has a type, length, and pad character.
//* 					
//* 				<code>PICX</code> 	fields are left justified and padded on 
//* 									the right with spaces.
//* 
//* 				<code>PIC9</code>	fields are right justified and padded on 
//* 									the left with 0's.
//* 
//* 				<code>PICS9</code>	fields are right justified, padded on the 
//* 									left with 0's with a sign byte to the right.
//* 
//* 				Values are accessed and mutated from the buffer using 
//* 				<code>getString</code> and <code>setString</code>.
/******************************************************************************/

import java.util.ArrayList;

public class LayoutBuffer {

public final static int FIELDTYPE_PIC9  = 0;	  //	CONSTANT
public final static int FIELDTYPE_PICX  = 1;	  //	CONSTANT
public final static int FIELDTYPE_PICS9 = 2;    //  CONSTANT

public final static int TYPE_UPPER_CASE   = 11;    //  CONSTANT
public final static int TYPE_LOWER_CASE   = 12;    //  CONSTANT
public final static int TYPE_MIXED_CASE   = 13;    //  CONSTANT

private final static LayoutBufferField initField = new LayoutBufferField();
private final static char[] defaultPadding = { '0', ' ', '0' };
private char[] buffer;
private ArrayList<LayoutBufferField> fields;
private boolean trim;

/**
 * Inner class - LayoutBufferField
*/
private static class LayoutBufferField {
	int startingPosition;
	int length;
	int type;
	char pad;
}

/**
 * LayoutBuffer constructor initializes the internal buffer.
 * @param int length
*/
public LayoutBuffer(int length) {
	super();
	this.setBufferLength(length);
}

/**
 * Set buffer lenth
 * @param int length
*/
private void setBufferLength(int length) {
	this.buffer = new char[length];
	for (int i = 0; i < length; i++)
		this.buffer[i] = ' ';
	this.fields = new ArrayList<LayoutBufferField>();
}

/**
 * Add a field to the layout format.
 * 
 * Answers with the field index that will be assigned to the field
 * being added.
 * 
 * @param int type
 * @param int length
 * @return int
*/
public int addField(int type, int length) {
	return this.addField(type, length, defaultPadding[type]);
}

/**
 * Add a field to the layout format.
 * 
 * Answers with the field index that will be assigned to the field
 * being added.
 * 
 * @param int type
 * @param int length
 * @param char pad
 * @return int
*/
public int addField(int type, int length, char pad) {
	LayoutBufferField lastField, newField;
	if (this.fields.isEmpty()) {
		lastField = initField;
	}
	else {
		lastField = (LayoutBufferField) this.fields.get(this.fields.size() - 1);
	}

	newField = new LayoutBufferField();
	newField.startingPosition = lastField.startingPosition + lastField.length;
	newField.length = length;
	newField.type = type;
	newField.pad = pad;
	this.fields.add(newField);
	return this.fields.size() - 1;
}

/**
 * Update this object from a string (opposite of this.toString).
 * @param java.lang.String value
*/
public void fromString(String value) {
	value.getChars(0, value.length(), this.buffer, 0);
}

/**
 * Get the string associated with the indicated field.
 * @param int fieldIndex
 * @return java.lang.String
*/
public String getString(int fieldIndex) {
	LayoutBufferField field = (LayoutBufferField) this.fields.get(fieldIndex);
	String answer = String.valueOf(this.buffer, field.startingPosition, field.length);
	if (this.isTrim()) {
		answer = answer.trim();
	}
	return answer;
}

/**
 * Accessor for trim.
 * @return boolean
*/
public boolean isTrim() {
	return trim;
}

/**
 * Mutator for trim.
 * @param boolean newTrim
*/
public void setTrim(boolean newTrim) {
	trim = newTrim;
}
	
/**
 * Answers with the length of the buffer.
 * @return int
*/
public int length() {
	return this.buffer.length;
}

/**
 * Set the field for the given value.
 * If the value's length is longer than the field of the
 * index, the value will be truncated!!!
 * @param int fieldIndex
 * @param String value
 * @param int changeCase
 * @return void
*/
public void setString(int fieldIndex, String value, int changeCase) {
    
	if (value == null) value = "";                 // use empty string if null.

    if (changeCase == TYPE_UPPER_CASE) {        // this condition is the first check
        value = value.toUpperCase();                // because it's used most often
    } else if (changeCase == TYPE_MIXED_CASE) {
        // no change to value
	} else if (changeCase == TYPE_LOWER_CASE) {
        value = value.toLowerCase();
    }

	this.setFormattedString(fieldIndex, value);
}

/**
 * Set the field for the given value.
 * If the value's length is longer than the field of the
 * index, the value will be truncated!!!
 * String value defaults to UPPER CASE
 * @param int fieldIndex
 * @param String value
 * @return void
*/
public void setString(int fieldIndex, String value) {
    this.setString(fieldIndex, value, TYPE_UPPER_CASE);
}

/**
 * Set the field for the given value.
 * If the value's length is longer than the field of the
 * index, the value will be truncated!!!
 * @param fieldIndex int
 * @param value java.lang.String
 * @return void
*/
private void setFormattedString(int fieldIndex, String value) {
	
	LayoutBufferField field = (LayoutBufferField) this.fields.get(fieldIndex);
	int fieldBufferPosition, stringPosition;
	char fieldBuffer[] = new char[field.length];
	
	if (field.type == FIELDTYPE_PIC9 || field.type == FIELDTYPE_PICS9) {
		value = value.trim();
	}
	
	if (field.type == FIELDTYPE_PICS9) {
		
		try {	// put the sign at the end of the number.
			char sign;
			Integer integerValue = new Integer(value);
			sign = (integerValue.intValue() < 0 ? '-' : '+');
			integerValue = new Integer(Math.abs(integerValue.intValue()));
			value = integerValue.toString() + sign;
		}
		catch (Exception x) {
			//	Error will occur on Stored Procedure Side (COBOL or DB2)
		}
	}
	
	if (field.type == FIELDTYPE_PIC9 || field.type == FIELDTYPE_PICS9) {
		stringPosition = value.length() - 1;
		for (fieldBufferPosition = fieldBuffer.length - 1; fieldBufferPosition >= 0; fieldBufferPosition--, stringPosition--) {
			if (stringPosition < 0) {
				fieldBuffer[fieldBufferPosition] = field.pad;
			}
			else {
				fieldBuffer[fieldBufferPosition] = value.charAt(stringPosition);
			}
		}
	}		
	else {
		stringPosition = 0;
		for (fieldBufferPosition = 0; fieldBufferPosition < fieldBuffer.length; fieldBufferPosition++, stringPosition++) {
			if (stringPosition >= value.length()) {
				fieldBuffer[fieldBufferPosition] = field.pad;
			}
			else {
				fieldBuffer[fieldBufferPosition] = value.charAt(stringPosition);
			}
		}
	}
	
	int bufferPosition;
	for (bufferPosition = field.startingPosition, fieldBufferPosition = 0; bufferPosition < this.buffer.length && fieldBufferPosition < fieldBuffer.length; bufferPosition++, fieldBufferPosition++) {
		this.buffer[bufferPosition] = fieldBuffer[fieldBufferPosition];
	}
}


/**
 * Returns a string representation of the object
 * @return String
*/
public String toString() {
	return new String(this.buffer);
}

/**
 * Resets the buffer
 * @param int length
*/
public void reset(int length) {
	this.fields.clear();
	this.setBufferLength(length);
}

}
