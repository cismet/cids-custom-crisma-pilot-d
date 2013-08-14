/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.model;

import org.openide.util.ImageUtilities;

import java.awt.Image;

import de.cismet.cismap.commons.features.DefaultStyledFeature;
import de.cismet.cismap.commons.gui.piccolo.FeatureAnnotationSymbol;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class EpicenterFeature extends DefaultStyledFeature {

    //~ Instance fields --------------------------------------------------------

    private final transient Image image;
    private final FeatureAnnotationSymbol symbol;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EpicenterFeature object.
     */
    public EpicenterFeature() {
        image = ImageUtilities.loadImage(EpicenterFeature.class.getPackage().getName().replaceAll("\\.", "/")
                        + "/epicenter_32.png");
        symbol = new FeatureAnnotationSymbol(image);
        symbol.setSweetSpotX(0.5);
        symbol.setSweetSpotY(0.5);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public FeatureAnnotationSymbol getPointAnnotationSymbol() {
        return symbol;
    }
}
