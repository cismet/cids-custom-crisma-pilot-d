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
public final class ShakemapEditor extends NotEditableEditor {

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getId() {
        return "shakemap_editor";
    }

    @Override
    public String getDisplayName() {
        return "Shakemap Editor";
    }
}
