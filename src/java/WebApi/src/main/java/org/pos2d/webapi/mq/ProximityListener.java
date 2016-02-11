/**
 * 
 */
package org.pos2d.webapi.mq;

import java.util.List;

import org.pos2d.webapi.dao.TransformerDAO;
import org.pos2d.webapi.entities.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author soumik
 *
 */
public class ProximityListener {
	
	@Autowired
    private TransformerDAO xfrDAO;
	
	@Transactional
	public void listen(String data) {
		List<Transformer> xfrsNeedingService = xfrDAO.getBySuburbAndStatus(city, suburb, status)
    }

}
