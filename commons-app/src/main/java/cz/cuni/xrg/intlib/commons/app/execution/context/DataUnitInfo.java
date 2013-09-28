package cz.cuni.xrg.intlib.commons.app.execution.context;

import cz.cuni.xrg.intlib.commons.data.DataUnitType;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Holds information about single
 * {@link cz.cuni.xrg.intlib.commons.data.DataUnit} context.
 * 
 * @author Petyr
 * 
 */
@Entity
@Table(name = "exec_dataunit_info")
public class DataUnitInfo implements Serializable {

	/**
	 * Primary key.
	 */
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_exec_dataunit_info")
	@SequenceGenerator(name = "seq_exec_dataunit_info", allocationSize = 1)
	private Long id;

	/**
	 * Index of DataUnit. Used to determine folder.
	 */
	@Column(name = "idx")
	private Integer index;

	/**
	 * Name of DataUnit given to the DataUnit by DPU or changed by user (on the
	 * edge).
	 */
	@Column(name = "name")
	private String name;

	/**
	 * DataUnit type.
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "type")
	private DataUnitType type;

	/**
	 * True if use as input otherwise false.
	 */
	@Column(name = "is_input")
	private boolean isInput;

	/**
	 * Empty constructor because of JAP.
	 */
	public DataUnitInfo() { }

	/**
	 * 
	 * @param name Name of DataUnit.
	 * @param index Index of data unit.
	 * @param type Type of DataUnit.
	 * @param isInput Is used as input?
	 */
	public DataUnitInfo(Integer index,
			String name,
			DataUnitType type,
			boolean isInput) {
		this.index = index;
		this.name = name;
		this.type = type;
		this.isInput = isInput;
	}

	public Long getId() {
		return id;
	}

	public Integer getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public DataUnitType getType() {
		return type;
	}

	public boolean isInput() {
		return isInput;
	}
        
    @Override
    public String toString() {
        return name;
    }

}