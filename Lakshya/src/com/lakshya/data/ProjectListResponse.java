package com.lakshya.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectListResponse {

	private List<Project> projects = new ArrayList<Project>();
	private String success;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The projects
	 */
	public List<Project> getProjects() {
		return projects;
	}

	/**
	 * 
	 * @param projects
	 * The projects
	 */
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	/**
	 * 
	 * @return
	 * The success
	 */
	public String getSuccess() {
		return success;
	}

	/**
	 * 
	 * @param success
	 * The success
	 */
	public void setSuccess(String success) {
		this.success = success;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
