/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.cascadeeffects;

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
public final class IgnitionFeature extends DefaultStyledFeature {

    //~ Instance fields --------------------------------------------------------

    private final transient Image image;
    private final FeatureAnnotationSymbol symbol;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EpicenterFeature object.
     */
    public IgnitionFeature() {
        image = ImageUtilities.loadImage(IgnitionFeature.class.getPackage().getName().replaceAll("\\.", "/")
                        + "/fire_32.png");
        symbol = new FeatureAnnotationSymbol(image);
        symbol.setSweetSpotX(0.5);
        symbol.setSweetSpotY(1);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public FeatureAnnotationSymbol getPointAnnotationSymbol() {
        return symbol;
    }
}
