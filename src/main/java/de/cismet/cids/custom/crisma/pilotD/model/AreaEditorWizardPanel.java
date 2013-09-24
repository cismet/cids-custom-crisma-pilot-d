/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.model;

import com.vividsolutions.jts.geom.Geometry;

import org.openide.WizardDescriptor;

import java.awt.Component;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cismap.commons.features.Feature;

import de.cismet.commons.gui.wizard.AbstractWizardPanel;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class AreaEditorWizardPanel extends AbstractWizardPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static final String PROP_AREAS = "__prop_evac_time_minutes__";

    //~ Instance fields --------------------------------------------------------

    private final AreaEditor areaEditor;
    private transient List<Area> areas;
    private CidsBean worldstate;

    private final String stepName;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AreaEditorWizardPanel object.
     *
     * @param  areaEditor  DOCUMENT ME!
     * @param  stepName    DOCUMENT ME!
     */
    public AreaEditorWizardPanel(final AreaEditor areaEditor, final String stepName) {
        this.areaEditor = areaEditor;
        this.stepName = stepName;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getStepName() {
        return stepName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public AreaEditor getAreaEditor() {
        return areaEditor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public CidsBean getWorldstate() {
        return worldstate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  worldstate  DOCUMENT ME!
     */
    public void setWorldstate(final CidsBean worldstate) {
        this.worldstate = worldstate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  a  DOCUMENT ME!
     */
    public void addArea(final Area a) {
        areas.add(a);

        changeSupport.fireChange();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<Area> getAreas() {
        return areas;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  areas  DOCUMENT ME!
     */
    public void setAreas(final List<Area> areas) {
        this.areas = areas;
    }

    @Override
    protected Component createComponent() {
        return new AreaEditorVisualPanel(this);
    }

    @Override
    protected void read(final WizardDescriptor wizard) {
        setWorldstate((CidsBean)wizard.getProperty(ExecModelWizardAction.PROP_WORLDSTATE));
        final List<Area> areas = (List)wizard.getProperty(stepName);
        if (areas == null) {
            setAreas(new ArrayList<Area>());
        } else {
            setAreas(areas);
        }

        ((AreaEditorVisualPanel)getComponent()).init();
    }

    @Override
    protected void store(final WizardDescriptor wizard) {
        wizard.putProperty(stepName, getAreas());
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static final class Area {

        //~ Instance fields ----------------------------------------------------

        Feature f;
        Object value;
        String name;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Area object.
         *
         * @param  g  DOCUMENT ME!
         */
        public Area(final Feature g) {
            this(g, null, "new Area");
        }

        /**
         * Creates a new Area object.
         *
         * @param  g      DOCUMENT ME!
         * @param  value  DOCUMENT ME!
         */
        public Area(final Feature g, final Object value) {
            this(g, value, "new Area");
        }

        /**
         * Creates a new Area object.
         *
         * @param  g      DOCUMENT ME!
         * @param  value  DOCUMENT ME!
         * @param  name   DOCUMENT ME!
         */
        public Area(final Feature g, final Object value, final String name) {
            this.f = g;
            this.value = value;
            this.name = name;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Feature getF() {
            return f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  f  DOCUMENT ME!
         */
        public void setF(final Feature f) {
            this.f = f;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Object getValue() {
            return value;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  value  DOCUMENT ME!
         */
        public void setValue(final Object value) {
            this.value = value;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getName() {
            return name;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  name  DOCUMENT ME!
         */
        public void setName(final String name) {
            this.name = name;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class AreaEditor extends JPanel {

        //~ Instance fields ----------------------------------------------------

        Area area;

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  a  DOCUMENT ME!
         */
        public void setArea(final Area a) {
            this.area = a;
        }
        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Area getArea() {
            return area;
        }
    }
}
