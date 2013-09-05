package cz.cuni.xrg.intlib.commons.app.dpu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Represent the DPU instance pipeline placement in DB.
 * 
 * @author Petyr
 *
 */
@Entity
@Table(name = "dpu_instance")
public class DPUInstanceRecord extends DPURecord {
		
	/**
	 * Template used for creating this instance. 
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name="dpu_id")
	private DPUTemplateRecord template;

	/**
	 * DPURecord tool tip.
	 */
	@Column(name="tool_tip")
	private String toolTip;	
	
	/**
	 * Empty ctor because of JPA.
	 */
	public DPUInstanceRecord() {}
	
	/**
	 * Copy constructor. Creates a copy of given <code>DPUInstanceRecord</code>.
	 * Primary key {@link #id} of newly created object is <code>null</code>.
	 * Copying is NOT propagated on {@link #template}, original reference is
	 * preserved.
	 * 
	 * @param dpuInstance 
	 */
	public DPUInstanceRecord(DPUInstanceRecord dpuInstance) {
		super(dpuInstance);
		template = dpuInstance.getTemplate();
		toolTip = dpuInstance.getToolTip();
	}
	
	/**
	 * Create new DPUInstanceRecord with given name and type.
	 * @param name
	 * @param type
	 */
	public DPUInstanceRecord(String name, DPUType type) {
		super(name, type);
		toolTip = null;
	}
	
	/**
	 * Create instance based on given template.
	 * @param template
	 */
	public DPUInstanceRecord(DPUTemplateRecord template) {
		// construct DPURecord
		super(template);
		// and set out variables
		this.template = template;
		this.toolTip = null;
	}
	
	public DPUTemplateRecord getTemplate() {
		return template;
	}

	public void setTemplate(DPUTemplateRecord template) {
		this.template = template;
	}

	public String getToolTip() {
		return toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}
}
