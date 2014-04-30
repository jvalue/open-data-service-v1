package org.jvalue.ods.data;

public class OdsView {
	private String idPath;
	private String viewName;
	private String function;

	public OdsView(String idPath, String viewName, String function) {
		super();
		this.idPath = idPath;
		this.viewName = viewName;
		this.function = function;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getIdPath() {
		return idPath;
	}

	public void setIdPath(String idPath) {
		this.idPath = idPath;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}
}
