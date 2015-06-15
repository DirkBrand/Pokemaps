package com.pokemaps.pokemaps.data;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

public class User implements Serializable {

	/**
	 * Model class for user database table
	 */
	private static final long serialVersionUID = 241975373462354427L;
	public final static String USER_ID_FIELD_NAME = "user_id";
	public final static String USER_NAME_FIELD_NAME = "user_name";
	public static final int MALE = 0;
	public static final int FEMALE = 1;

	@DatabaseField(generatedId = true, columnName = USER_ID_FIELD_NAME)
	private int id;

	@DatabaseField(columnName = USER_NAME_FIELD_NAME)
	private String name;

	@DatabaseField(columnName = "sex")
	private int sex;

	@DatabaseField(columnName = "added_date")
	public Date addedDate;

	// Default constructor is needed for the SQLite, so make sure you also have
	// it
	public User() {
	}

	// For our own purpose, so it's easier to create a StudentDetails object
	public User(final String name) {
		this.name = name;
		this.addedDate = new Date();
	}

	// For our own purpose, so it's easier to create a StudentDetails object
	public User(final String name, int sex) {
		this.name = name;
		this.sex = sex;
		this.addedDate = new Date();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getSex() {
		return sex;
	}

	public void setName(String name) {
		this.name = name;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return name;
	}

}
