package dataObject;

import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.Direction;

import com.google.appengine.api.datastore.Key;

import exception.OperationException;
import factory.PMF;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Operation
{
	@SuppressWarnings("unchecked")
	public static Operation getOperation(Key controlID,Key dataTypeID)
	{
		Operation returnOperation=null;
		Operation iOperation;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Operation.class);
		try
		{
			List<Operation> results = (List<Operation>) query.execute();
			Iterator<Operation> it=results.iterator();
			while(it.hasNext())
			{
				iOperation=it.next();
				if(iOperation.getControlID().equals(controlID) && iOperation.getDataTypeID().equals(dataTypeID))
				{
					returnOperation=iOperation;
					break;
				}
			}
		}finally
		{
			//noting to do
		}
		return returnOperation;
	}
	@SuppressWarnings("unchecked")
	public static Element getOperationListXML()
	{
		DocumentBuilder db;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Operation.class);
		try
		{
			db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
			return null;
		}
		Document XMLDoc=db.newDocument();
		Element root=XMLDoc.createElement("operation");
		try
		{
			List<Operation> results = (List<Operation>) query.execute();
			if (results.iterator().hasNext())
			{
				for (Operation e : results)
				{
					Element operationItem=XMLDoc.createElement("row");

					Element controlCommand=XMLDoc.createElement("control_command");
					controlCommand.appendChild(XMLDoc.createTextNode(e.getControl().getCommand()));
					
					Element controlParam=XMLDoc.createElement("control_parameter");
					controlParam.appendChild(XMLDoc.createTextNode(e.getControl().getParameter()));
					
					Element dataTypeID=XMLDoc.createElement("dataTypeID");
					dataTypeID.appendChild(XMLDoc.createTextNode(e.getDataType().getTypeName()));
					
					Element direction=XMLDoc.createElement("direction");
					direction.appendChild(XMLDoc.createTextNode(e.getDirection().toString()));
					
					operationItem.appendChild(controlCommand);
					operationItem.appendChild(controlParam);
					operationItem.appendChild(dataTypeID);
					operationItem.appendChild(direction);
					root.appendChild(operationItem);
				}
			}
		}
		finally
		{
			//do nothing
		}
		return root;
	}
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key operationID;
	@Persistent
	private Key controlID;
	@Persistent
	private Key dataTypeID;
	@Persistent
	private Direction direction;
	//private Control control;
	//private DataType targetDataType;
	/**
	 * @param control
	 * @param targetDataType
	 * @param direction
	 */
	public Operation(Key controlID,Key dataTypeID,
			Direction direction)
	{
		super();
		this.controlID = controlID;
		this.dataTypeID = dataTypeID;
		this.direction = direction;
	}
	public void saveAsNew() throws OperationException
	{
		if(Operation.getOperation(controlID, dataTypeID)==null && operationID==null)
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			pm.makePersistent(this);
		}
		else if(operationID!=null)
		{
			throw new OperationException(OperationException.PrimaryKeyNotNull);
		}
		else
		{
			throw new OperationException(OperationException.OperationAlreadyExist);
		}
	}
	/**
	 * @return the operationID
	 */
	public Key getOperationID()
	{
		return operationID;
	}
	/**
	 * @return the direction
	 */
	public Direction getDirection()
	{
		return direction;
	}
	/**
	 * @param operationID the operationID to set
	 */
	public void setOperationID(Key operationID)
	{
		this.operationID = operationID;
	}
	/**
	 * @param direction the direction to set
	 */
	public void setDirection(Direction direction)
	{
		this.direction = direction;
	}
	public boolean equals(Operation o)
	{
		if(o.controlID.equals(this.controlID) && o.dataTypeID.equals(this.dataTypeID))
			return true;
		else
			return false;
	}
	/**
	 * @return the controlID
	 */
	public Key getControlID()
	{
		return controlID;
	}
	public Control getControl()
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		return pm.getObjectById(Control.class, controlID);
	}
	/**
	 * @return the dataTypeID
	 */
	public Key getDataTypeID()
	{
		return dataTypeID;
	}
	public DataType getDataType()
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		return pm.getObjectById(DataType.class, dataTypeID);
	}
	/**
	 * @param controlID the controlID to set
	 */
	public void setControlID(Key controlID)
	{
		this.controlID = controlID;
	}
	/**
	 * @param dataTypeID the dataTypeID to set
	 */
	public void setDataTypeID(Key dataTypeID)
	{
		this.dataTypeID = dataTypeID;
	}
	
	
}
