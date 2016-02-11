/**
 * 
 */
package org.pos2d.webapi.dao;

import java.util.List;

import org.pos2d.webapi.beans.TransformerStatusBean;
import org.pos2d.webapi.entities.Transformer;

/**
 * @author soumik
 *
 */
public interface TransformerDAO {
	public List<Transformer> getBySuburbAndStatus(String city, String suburb, String status);
	public List<Transformer> getBySuburb(String city, String suburb);
	public void createXfrStatus(TransformerStatusBean bean, String data);
}
