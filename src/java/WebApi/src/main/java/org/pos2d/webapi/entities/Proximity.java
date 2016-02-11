/**
 * 
 */
package org.pos2d.webapi.entities;

import java.util.Calendar;
import java.util.Set;

/**
 * @author soumik
 *
 */
public class Proximity {
	private long gid;
	private Transformer transformer;
	private Calendar eventOn;
	private Set<ProximityDetail> proximityDetails;
}
