package org.jvalue.ods.data;

/**
 * The Class OdsView.
 */
public class OdsView {
	
	/** The id path. */
	private String idPath;
	
	/** The view name. */
	private String viewName;
	
	/** The function. */
	private String function;

	/**
	 * Instantiates a new ods view.
	 *
	 * @param idPath the id path
	 * @param viewName the view name
	 * @param function the function
	 */
	public OdsView(String idPath, String viewName, String function) {
		super();
		this.idPath = idPath;
		this.viewName = viewName;
		this.function = function;
	}

	/**
	 * Gets the view name.
	 *
	 * @return the view name
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * Sets the view name.
	 *
	 * @param viewName the new view name
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * Gets the id path.
	 *
	 * @return the id path
	 */
	public String getIdPath() {
		return idPath;
	}

	/**
	 * Sets the id path.
	 *
	 * @param idPath the new id path
	 */
	public void setIdPath(String idPath) {
		this.idPath = idPath;
	}

	/**
	 * Gets the function.
	 *
	 * @return the function
	 */
	public String getFunction() {
		return function;
	}

	/**
	 * Sets the function.
	 *
	 * @param function the new function
	 */
	public void setFunction(String function) {
		this.function = function;
	}
}
