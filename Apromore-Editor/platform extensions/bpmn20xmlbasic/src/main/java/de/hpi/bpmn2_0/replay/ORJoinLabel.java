/*
 * Copyright © 2009-2016 The Apromore Initiative.
 *
 * This file is part of "Apromore".
 *
 * "Apromore" is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * "Apromore" is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.
 * If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package de.hpi.bpmn2_0.replay;

import de.hpi.bpmn2_0.model.connector.SequenceFlow;
import java.util.Set;

/*
* ORJoinLabel represents set of incoming edges of an OR-join that an external edge can reach
* Thus, each edge with a path to one or more input edges of an OR-join has a label
* Multiple edges may share the same label because they can reach the same incoming edges of an OR-join
* Each label has a counter which counts the number of tokens residing on all edges sharing the same label
*/
public class ORJoinLabel {
    private Set<SequenceFlow> edges;
    private ORJoinCounter counter = new ORJoinCounter();
    private ORJoinLabelColor color = ORJoinLabelColor.NONE;
    

    
    //Each added edge is an incoming edge of an OR-Join
    public ORJoinLabel(Set<SequenceFlow> edges) {
        this.edges = edges;
    }
    
    public Set<SequenceFlow> getEdges() {
        return this.edges;
    }
    
    public ORJoinCounter getCounter() {
        return counter;
    }
    
    public boolean contains(SequenceFlow edge) {
        return this.edges.contains(edge);
    }
    
    public ORJoinLabelColor getColor() {
        return color;
    }
    
    public void setColor(ORJoinLabelColor color) {
        this.color = color;
    }
    
    public boolean isRed() {
        return (this.color == ORJoinLabelColor.RED);
    }
    
    public boolean isBlue() {
        return (this.color == ORJoinLabelColor.BLUE);
    }
    
    public void reset() {
        counter.reset();
    }
}