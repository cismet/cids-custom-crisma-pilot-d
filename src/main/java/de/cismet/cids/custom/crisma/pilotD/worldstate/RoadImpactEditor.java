/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.worldstate;

import de.cismet.cids.custom.crisma.worldstate.editor.NotEditableEditor;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class RoadImpactEditor extends NotEditableEditor {

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getId() {
        return "road_impact_editor";
    }

    @Override
    public String getDisplayName() {
        return "Road Impact Editor";
    }
}
